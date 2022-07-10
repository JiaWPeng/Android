package com.example.mytodo.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
//import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.example.mytodo.R
import com.example.mytodo.data.models.ToDoData
import com.example.mytodo.data.viewModel.ToDoViewModel
import com.example.mytodo.databinding.FragmentListBinding
import com.example.mytodo.fragments.ShareViewModel
import com.example.mytodo.fragments.list.adapter.ListAdapter
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(),SearchView.OnQueryTextListener {

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mShareViewModel:ShareViewModel by viewModels()

    private var _binding:FragmentListBinding? = null
    private val binding get() = _binding!!

    // 列表适配器
    private val adapter : ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // val view = inflater.inflate(R.layout.fragment_list, container, false)
        // DataBinding
        _binding = FragmentListBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.mShareViewModel = mShareViewModel

        // 设置布局
        setRecyclerView()


        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_settingsFragment)
        }
        // Observer LiveData 实时监测数据
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer {
            mShareViewModel.checkdataempty(it)
            adapter.setData(it)
        })

//        mShareViewModel.emptydata.observe(viewLifecycleOwner,Observer{
//            showEmptydataview(it)
//        }) // dataBinding，此方法在BindingAdapters中重写

//        view.floatingActionButton.setOnClickListener {
//            findNavController().navigate(R.id.action_listFragment_to_addFragment)
//            //Log.e("test", "onCreateView: ", )
//            //findNavController(R.id.nav_host_fragment)
//        }  // dataBinding，此方法在BindingAdapters中重写

        setHasOptionsMenu(true)

        // 隐藏键盘
        //hideKeyboard(requireActivity())
        //return view
        return binding.root
    }

    // 设置视图
    private fun setRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        // 设置布局
        // recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        // recyclerView.layoutManager = GridLayoutManager(requireContext(),3) // 此方法如果有一个格子很大，则并行的格子会有留白
        recyclerView.layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = SlideInUpAnimator().apply{
            addDuration = 300
            removeDuration = 100
            moveDuration = 1000
            changeDuration = 100
        } // 调用Animator接口，实现列表刷新动画

        // 调用滑动删除方法
        swipetoDelete(recyclerView)
    }

    // 创建滑动删除方法
    private fun swipetoDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemDelete = adapter.datalist[viewHolder.adapterPosition]
                // 删除item
                mToDoViewModel.deteleData(itemDelete)
                //Toast.makeText(requireContext(),"删除成功：'${itemDelete.title}'",Toast.LENGTH_SHORT).show()
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                // 调用恢复删除方法
                restoreDeleteData(viewHolder.itemView,itemDelete,viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    // 恢复删除
    private fun restoreDeleteData(view: View,itemDelete: ToDoData,position:Int){
        val snackBar = Snackbar.make(view,"删除 '${itemDelete.title}'",Snackbar.LENGTH_LONG)
        snackBar.setAction("恢复"){
            mToDoViewModel.insertData(itemDelete)
            adapter.notifyItemChanged(position)
        }
        snackBar.show()
    }
//    private fun showEmptydataview(emptydata:Boolean) {
//        if (emptydata){
//            view?.no_data_imageView?.visibility = View.VISIBLE
//            view?.no_data_textView?.visibility = View.VISIBLE
//        }else{
//            view?.no_data_imageView?.visibility = View.INVISIBLE
//            view?.no_data_textView?.visibility = View.INVISIBLE
//        }
//    }   // dataBinding，此方法在BindingAdapters中重写

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu,menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_del_all -> delall()
            //R.id.menu_night -> mToDoViewModel.sortHigt.observe(this,{ AppCompatDelegate.MODE_NIGHT_YES})
            R.id.menu_sort_high -> mToDoViewModel.sortHigt.observe(this, Observer { adapter.setData(it) })
            R.id.menu_sort_low -> mToDoViewModel.sortLow.observe(this,Observer{ adapter.setData(it) })
        }
        return super.onOptionsItemSelected(item)
    }

    // 搜索框方法
    override fun onQueryTextSubmit(search: String?): Boolean {
        if(search != null){
            searchThroughDatabase(search)
        }
        return true
    }
    // 搜索框方法
    override fun onQueryTextChange(search: String?): Boolean {
        if(search != null){
            searchThroughDatabase(search)
        }
        return true
    }

    private fun searchThroughDatabase(search: String) {
        var searchQuery = "%$search%"

        mToDoViewModel.searchDatabase(searchQuery).observe(this,Observer{List ->
            List?.let {
                adapter.setData(it)
            }
        })
    }

    private fun delall() {
        val builder = AlertDialog.Builder(requireContext())
        //builder.setNegativeButton("否"){_,_ -> }
        builder.setNeutralButton("取消"){_,_ ->}
        builder.setPositiveButton("是"){ _,_ ->
            mToDoViewModel.deleteall()
            Toast.makeText(
                requireContext(),
                "成功删除全部",
                Toast.LENGTH_SHORT
            ).show()

        }

        builder.setTitle("删除全部?")
        builder.setMessage("确定删除?")
        builder.create().show()
    }

    // 销毁视图
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}