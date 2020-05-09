package com.example.a2do.base

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.a2do.model.Note
import com.example.a2do.model.NoteList
import com.example.a2do.util.Constants
import com.google.gson.Gson

abstract  class BaseFragment <DB: ViewDataBinding,VM: ViewModel>: Fragment() {

    lateinit var dataBinding: DB
    lateinit var viewModel: VM

    protected var sharedPref: SharedPreferences? = null
    protected var editor: SharedPreferences.Editor? = null
    protected var notelist: NoteList?= null
    protected var isEmpty:Boolean=true

    @LayoutRes
    abstract fun getLayoutRes():Int
    abstract fun getViewModel():Class<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(getViewModel())

        sharedPref = activity!!.getSharedPreferences(Constants.PREFS_FILENAME, Context.MODE_PRIVATE)
        editor = sharedPref?.edit()
        editor!!.commit()




    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(),container,false)
        return dataBinding.root
    }

    override fun onResume() {
        super.onResume()

        isEmpty=sharedPref!!.getBoolean("isEmpty",true)
        notelist= Gson().fromJson<NoteList>(sharedPref!!.getString("noteList",null),NoteList::class.java)

    }

}