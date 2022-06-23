package com.example.mytodo.fragments

import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mytodo.R
import com.example.mytodo.data.models.Priority
import com.example.mytodo.data.models.ToDoData

class ShareViewModel(application: Application):AndroidViewModel(application) {

    // 创建空的数据对象
    val emptydata: MutableLiveData<Boolean> = MutableLiveData(false)

    // 检查数据库是否为空
    fun checkdataempty(toDoData: List<ToDoData>){
        emptydata.value = toDoData.isEmpty()
    }

    val listtener: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            // 设置单选框的文本颜色
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    0 -> {(parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application,
                        R.color.red))}
                    1 -> {(parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application,
                        R.color.yellow))}
                    2 -> {(parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application,
                        R.color.green))}
                }
            }
        }

     // 判断两个文本框内容是否为空
     fun v_if_null(title: String,descripton:String):Boolean{
        return if (TextUtils.isEmpty(title) || TextUtils.isEmpty(descripton)){
            false
        }else{
            !(title.isEmpty() || descripton.isEmpty())
        }
    }

     fun parsePriority(priority: String): Priority {
        return when(priority){
            "高" -> {
                Priority.HIGH}
            "中" -> {
                Priority.MEDIUM}
            "低" -> {
                Priority.LOW}
            else -> {
                Priority.LOW}
        }
    }
//     fun parsePrioritytoInt(priority: Priority):Int{
//        return when(priority){
//            Priority.HIGH -> 0
//            Priority.MEDIUM -> 1
//            Priority.LOW -> 2
//        }
//    }   // Binding，此方法在BindingAdapters中重写
}