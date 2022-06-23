package com.example.mytodo.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mytodo.data.ToDoDatebase
import com.example.mytodo.data.models.ToDoData
import com.example.mytodo.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application:Application):AndroidViewModel(application) {

    private val toDoDao = ToDoDatebase.getDatabase(application).toDoDao()
    private val repository:ToDoRepository
    val getAllData:LiveData<List<ToDoData>>
    val sortHigt:LiveData<List<ToDoData>>
    val sortLow:LiveData<List<ToDoData>>


    init {
        repository = ToDoRepository(toDoDao)
        getAllData = repository.getAllData
        sortHigt = repository.sortByHigt
        sortLow = repository.sortByLow
    }

    fun insertData(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertData(toDoData)
        }
    }

    fun updataData(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO){
            repository.updataData(toDoData)
        }
    }

    fun deteleData(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO){
            repository.deteleData(toDoData)
        }
    }

    fun deleteall(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteall()
        }
    }

    fun searchDatabase(searchQuery:String):LiveData<List<ToDoData>>{
        return repository.searchDatabase(searchQuery)
    }

}