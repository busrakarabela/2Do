package com.example.a2do.base

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.a2do.model.Note
import com.example.a2do.model.NoteList
import com.example.a2do.util.Constants
import com.google.gson.Gson

abstract  class BaseVMFragment <VM: ViewModel>: Fragment() {

    lateinit var viewModel: VM





    abstract fun getViewModel():Class<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(getViewModel())




    }



}