package com.example.mytodo.fragments.list

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

// 实现左滑删除列表里的一项
abstract class SwipeToDelete: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false    // 不需要移动，所以返回false
    }
}