package com.example.mytodo.fragments.add

import android.icu.text.CaseMap
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mytodo.R
import com.example.mytodo.data.models.Priority
import com.example.mytodo.data.models.ToDoData
import com.example.mytodo.data.viewModel.ToDoViewModel
import com.example.mytodo.fragments.ShareViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*


class AddFragment : Fragment() {
    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mShareViewModel: ShareViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        setHasOptionsMenu(true)

        view.priorities_spinner.onItemSelectedListener = mShareViewModel.listtener

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add){
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu,menu)
    }

    private fun insertDataToDb() {
        val mTitle = title_et.text.toString()
        val mPriority = priorities_spinner.selectedItem.toString()
        val mDescription = description_et.text.toString()

        val vifnull = mShareViewModel.v_if_null(mTitle,mDescription)
        if (vifnull){
            val newData = ToDoData(
                0,
                mTitle,
                mShareViewModel.parsePriority(mPriority),
                mDescription
            )
            mToDoViewModel.insertData(newData)
            Toast.makeText(requireContext(),"添加成功！",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(),"请填写所有字段！",Toast.LENGTH_SHORT).show()
        }
    }

}