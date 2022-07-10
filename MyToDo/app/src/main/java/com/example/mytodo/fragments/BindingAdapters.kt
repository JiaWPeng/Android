package com.example.mytodo.fragments

import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import com.example.mytodo.R
import com.example.mytodo.data.models.Priority
import com.example.mytodo.data.models.ToDoData
import com.example.mytodo.fragments.list.ListFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import java.lang.invoke.ConstantCallSite

class BindingAdapters {

    companion object{

        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToFragment(view:FloatingActionButton,navigate:Boolean){
             view.setOnClickListener{
                 if (navigate){
                     view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                 }
             }
        }
        @BindingAdapter("android:listToTest")
        @JvmStatic
        fun listToTest(view: Button,navigate: Boolean){
            view.setOnClickListener {
                if (navigate){
                    view.findNavController().navigate(R.id.action_listFragment_to_blankFragment)
                }
            }
        }

        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptydata(view:View,emptydata:MutableLiveData<Boolean>){
            when(emptydata.value){
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("android:UpSpinner")
        @JvmStatic
        fun upSpinnerInt(view: Spinner,priority: Priority){
            when(priority){
                Priority.HIGH -> {view.setSelection(0)}
                Priority.LOW -> {view.setSelection(2)}
                Priority.MEDIUM -> {view.setSelection(1)}
            }
        }

        @BindingAdapter("android:cardVIewColor")
        @JvmStatic
        fun cardVIewColor(cardView: CardView, priority: Priority){
            when(priority){
                Priority.LOW ->{cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))}
                Priority.HIGH -> {cardView.setCardBackgroundColor(cardView.context.getColor(R.color.red))}
                Priority.MEDIUM -> {cardView.setCardBackgroundColor(cardView.context.getColor(R.color.yellow))}
            }
        }

        @BindingAdapter("android:sendDataListToUpdate")
        @JvmStatic
        fun sendDataListToUpdate(view:ConstraintLayout,currentItem:ToDoData){
            // 从ListFragment跳转到UpdateFragment
            view.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
                view.findNavController().navigate(action)
            }
        }

    }

}