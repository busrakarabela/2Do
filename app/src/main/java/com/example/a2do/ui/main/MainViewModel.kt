package com.example.a2do.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a2do.model.Note

class MainViewModel:ViewModel() {

    val newNoteLiveData= MutableLiveData<Note>()
    val allNoteLiveData= MutableLiveData<ArrayList<Note>>()
}