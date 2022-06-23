package com.example.mytodo.data

import android.content.Context
import androidx.room.*
import com.example.mytodo.data.models.ToDoData

@Database(entities = [ToDoData::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ToDoDatebase:RoomDatabase() {

    abstract fun toDoDao():ToDoDao

    companion object{

        @Volatile
        private var INSTANCE:ToDoDatebase? = null

        fun getDatabase(context: Context):ToDoDatebase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatebase::class.java,
                    "todo_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}