package com.example.a2do.ui.trash

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2do.R
import com.example.a2do.base.BaseFragment
import com.example.a2do.databinding.FragmentThrashBinding
import com.example.a2do.model.Note
import com.example.a2do.model.NoteList
import com.example.a2do.ui.home.ThrashAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_thrash.view.*

class TrashFragment : BaseFragment<FragmentThrashBinding, TrashViewModel>(),
    ThrashAdapter.OnNoteListClickListener, ThrashAdapter.ChangeStatus {



    override fun getLayoutRes(): Int = R.layout.fragment_thrash

    override fun getViewModel(): Class<TrashViewModel> = TrashViewModel::class.java

    lateinit var thrashAdapter: ThrashAdapter
    private lateinit var allnote:NoteList
    private var deletedList:ArrayList<Note> = ArrayList()

    protected var isempty:Boolean=true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mview = inflater.inflate(R.layout.fragment_thrash, container, false)


        thrashAdapter= ThrashAdapter()
        thrashAdapter.setOnNoteListClickListener(this)

        thrashAdapter.changeStatus(this)


        isempty=sharedPref!!.getBoolean("isEmpty",true)
        if(isempty){

            mview.trash_warning.visibility=View.VISIBLE
            mview.trash_list.visibility=View.GONE

        }else{


            allnote= Gson().fromJson<NoteList>(sharedPref!!.getString("noteList", null), NoteList::class.java)

            if(allnote.notes.size!=0){

                for(x in 0..allnote.notes.size-1){
                    if(allnote.notes.get(x).isDeleted){
                        deletedList.add(allnote.notes.get(x))
                    }
                }


                if(deletedList.size==0){
                    mview.trash_warning.visibility=View.VISIBLE
                    mview.trash_list.visibility=View.GONE

                }else{
                    mview.trash_warning.visibility=View.GONE
                    mview.trash_list.visibility=View.VISIBLE

                    thrashAdapter.submitList(deletedList)
                    mview.trash_list.adapter=thrashAdapter
                    mview.trash_list.layoutManager= LinearLayoutManager(context)

                }
            }else{

                mview.trash_warning.visibility=View.VISIBLE
                mview.trash_list.visibility=View.GONE

            }

        }

        mview.edt_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString()
                val searchList = deletedList.filter { s ->
                    s.baslik.contains(searchText, false)
                }
                thrashAdapter.submitList(searchList)
            }
        })




        return mview
    }

    override fun onClick(note: Note) {
    }

    override fun changeButton(boolean: Boolean, position: Int) {
    }
}