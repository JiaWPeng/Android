package com.example.mytodo.fragments.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.mytodo.data.models.ToDoData

class ToDoDiffUtil(
    private val oldList:List<ToDoData>,
    private val newList:List<ToDoData>
) : DiffUtil.Callback(){
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
        // ‘==‘表示比较值，‘===‘表示比较两个对象的地址是否相等
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
                && oldList[oldItemPosition].title == newList[newItemPosition].title
                && oldList[oldItemPosition].priority == newList[newItemPosition].priority
                && oldList[oldItemPosition].description == newList[newItemPosition].description
    }
}