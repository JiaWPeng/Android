package com.example.mytodo.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodo.data.models.ToDoData
import com.example.mytodo.databinding.RowLayoutBinding

class ListAdapter:RecyclerView.Adapter<ListAdapter.MyView>() {

    var datalist = emptyList<ToDoData>()

    // class MyView(itemView:View):RecyclerView.ViewHolder(itemView) {}
    class MyView(private val binding: RowLayoutBinding):RecyclerView.ViewHolder(binding.root){

        // 绑定数据
        fun bind(toDoData: ToDoData){
            binding.toDoData = toDoData
            binding.executePendingBindings()
        }

        // object声明（一个类）是延迟加载的，只有当第一次被访问时才会初始化，所以被用来实现单例
        // companion object是当包含它的类被加载时就初始化了的，这一点和Java的static还是一样的
        // 区别：
        // companion object 就是 Java 中的 static 变量
        // companion object 只能定义在对应的类中
        // object 可以定义在全局也可以在类的内部使用
        // object 就是单例模式的化身
        // object 可以实现 Java 中的匿名类

        // 创建布局
        // inflate
        companion object{
            fun from(parent: ViewGroup): MyView {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(layoutInflater,parent,false)
                return MyView(binding)
            }
        }

    }

    // 一直刷新，显示数据到屏幕
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        // return MyView(LayoutInflater.from(parent.context).inflate(R.layout.row_layout,parent,false))
        return MyView.from(parent)
    }

    // 绑定数据到布局上
    override fun onBindViewHolder(holder: MyView, position: Int) {
        val currentItem = datalist[position]
        holder.bind(currentItem)
    }
//        holder.itemView.title_txt.text = datalist[position].title
//        holder.itemView.description_txt.text = datalist[position].description
//            // 从ListFragment跳转到UpdateFragment
    //        holder.itemView.row_background.setOnClickListener {
//            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(datalist[position])
//            holder.itemView.findNavController().navigate(action)
//        }
//
//        when(datalist[position].priority){
//            Priority.HIGH -> holder.itemView.priorities_cardview.setCardBackgroundColor(ContextCompat.getColor(
//                holder.itemView.context,
//                R.color.red
//            ))
//            Priority.MEDIUM -> holder.itemView.priorities_cardview.setCardBackgroundColor(ContextCompat.getColor(
//                holder.itemView.context,
//                R.color.yellow
//            ))
//            Priority.LOW -> holder.itemView.priorities_cardview.setCardBackgroundColor(ContextCompat.getColor(
//                holder.itemView.context,
//                R.color.green
//            ))
//        }


    override fun getItemCount(): Int {
        return datalist.size
    }

    fun setData(toDoData: List<ToDoData>){
        val toDoDiffUtil = ToDoDiffUtil(datalist,toDoData)
        // 比对是否差异
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.datalist = toDoData
        // 调度更新ListView
        toDoDiffResult.dispatchUpdatesTo(this)
        // 动态更新ListView 比较消耗性能
        // notifyDataSetChanged()
    }

}