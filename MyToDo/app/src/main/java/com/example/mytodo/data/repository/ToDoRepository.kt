package com.example.mytodo.data.repository

import androidx.lifecycle.LiveData
import com.example.mytodo.data.ToDoDao
import com.example.mytodo.data.models.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {

    val getAllData:LiveData<List<ToDoData>> = toDoDao.getAllData()
    val sortByHigt: LiveData<List<ToDoData>> = toDoDao.sortByHigh()
    val sortByLow: LiveData<List<ToDoData>> = toDoDao.sortByLow()

    suspend fun insertData(toDoData: ToDoData){
        toDoDao.insertData(toDoData)
    }

    suspend fun updataData(toDoData: ToDoData){
        toDoDao.updateData(toDoData)
    }

    suspend fun deteleData(toDoData: ToDoData){
        toDoDao.deleteData(toDoData)
    }

    suspend fun deleteall(){
        toDoDao.deleteall()
    }

    fun searchDatabase(searchQuery:String):LiveData<List<ToDoData>>{
        return toDoDao.searchDatabase(searchQuery)
    }
}