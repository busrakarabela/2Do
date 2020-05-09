package com.example.a2do.ui.tag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a2do.R
import com.example.a2do.base.BaseFragment
import com.example.a2do.databinding.FragmentTagBinding
import kotlinx.android.synthetic.main.fragment_tag.view.*

class TagFragment: BaseFragment<FragmentTagBinding,TagViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_tag

    override fun getViewModel(): Class<TagViewModel> = TagViewModel::class.java

    protected var isempty:Boolean=true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mview = inflater.inflate(R.layout.fragment_tag, container, false)

        if(isempty){
            mview.tag_warning.visibility=View.VISIBLE
            mview.tag_list.visibility=View.GONE

        }else{
            mview.tag_warning.visibility=View.GONE
            mview.tag_list.visibility=View.VISIBLE


        }

        return mview
    }
}