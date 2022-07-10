package com.example.mytodo.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mytodo.R
import com.example.mytodo.data.models.Priority
import com.example.mytodo.data.models.ToDoData
import com.example.mytodo.data.viewModel.ToDoViewModel
import com.example.mytodo.databinding.FragmentUpdateBinding
import com.example.mytodo.fragments.ShareViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*


class UpdateFragment : Fragment() {

    // 获取ListFragment点击itme的数据流
    private val args by navArgs<UpdateFragmentArgs>()
    //
    private val mShareViewModel: ShareViewModel by viewModels()

    private val mToDoViewModel: ToDoViewModel by viewModels()

    private var _binding:FragmentUpdateBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // binding
        // val view = inflater.inflate(R.layout.fragment_update, container, false)
        _binding = FragmentUpdateBinding.inflate(inflater,container,false)
        // 绑定修改界面的数据
        binding.args = args
        // 显示菜单
        setHasOptionsMenu(true)

//        // 绑定修改界面的数据
//        view.up_title_et.setText(args.currentitem.title)
//        view.up_description_et.setText(args.currentitem.description)
//        view.up_priorities_spinner.setSelection(mShareViewModel.parsePrioritytoInt(args.currentitem.priority))
        // 设置单选框的颜色
        // upPrioritiesSpinner是xml的控件id "up_priorities_spinner"
        binding.upPrioritiesSpinner.onItemSelectedListener = mShareViewModel.listtener

        return binding.root
    }

    // 重写菜单方法
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.up_menu,menu)
    }

    // 菜单点击事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_up -> updateItem()
            R.id.menu_del -> removeItem()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun updateItem() {

        val title = up_title_et.text.toString()
        val priority = up_priorities_spinner.selectedItem.toString()
        val description = up_description_et.text.toString()
        val v_if_null = mShareViewModel.v_if_null(title,description)

        if (v_if_null){
            // 保存文本数据
            val updatebase = ToDoData(
                args.currentitem.id,
                title,
                mShareViewModel.parsePriority(priority),
                description
            )
            // 更新到数据库
            mToDoViewModel.updataData(updatebase)
            Toast.makeText(requireContext(),"更新成功！",Toast.LENGTH_SHORT).show()
            // 更新完成后跳转至listFragment
            findNavController().navigate(R.id.action_updateFragment_to_listFragment2)
        }else{
            Toast.makeText(requireContext(),"请填写所有字段！",Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeItem() {
        val builder = AlertDialog.Builder(requireContext())
        //builder.setNegativeButton("否"){_,_ -> }
        builder.setNeutralButton("取消"){_,_ ->}
        builder.setPositiveButton("是"){ _,_ ->
            mToDoViewModel.deteleData(args.currentitem)
            Toast.makeText(
                requireContext(),
                "删除成功：${args.currentitem.title}",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment2)
        }

        builder.setTitle("删除 '${args.currentitem.title}'?")
        builder.setMessage("确定删除 '${args.currentitem.title}'?")
        builder.create().show()
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setPositiveButton("Yes") { _, _ ->
//            mToDoViewModel.deteleData(args.currentitem)
//            Toast.makeText(
//                requireContext(),
//                "Successfully Removed: ${args.currentitem.title}",
//                Toast.LENGTH_SHORT
//            ).show()
//            findNavController().navigate(R.id.action_updateFragment_to_listFragment2)
//        }
//        builder.setNegativeButton("No") { _, _ -> }
//        builder.setTitle("Delete '${args.currentitem.title}'?")
//        builder.setMessage("Are you sure you want to remove '${args.currentitem.title}'?")
//        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}