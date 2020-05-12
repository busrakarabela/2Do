package com.example.a2do.ui.home


import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2do.R
import com.example.a2do.base.BaseFragment
import com.example.a2do.databinding.FragmentHomeBinding
import com.example.a2do.model.Note
import com.example.a2do.model.NoteList
import com.example.a2do.ui.main.MainViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.show_note.*
import java.text.SimpleDateFormat


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(),
    AllNoteAdapter.OnNoteListClickListener, AllNoteAdapter.ChangeStatus {


    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun getViewModel(): Class<HomeViewModel> = HomeViewModel::class.java


    private lateinit var homeViewModel: HomeViewModel
    private  var notes:ArrayList<Note> = ArrayList()
    private lateinit var allnote:NoteList
    private  var showlist:ArrayList<Note> = ArrayList()
    private var deletedList:ArrayList<Note> = ArrayList()
    private var archiveList:ArrayList<Note> = ArrayList()
    private var selectedList:ArrayList<Note> = ArrayList()

    lateinit var allNoteAdapter: AllNoteAdapter
    protected var isempty:Boolean=true

    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mview = inflater.inflate(R.layout.fragment_home, container, false)



        mview.btn_add.setOnClickListener {
            //note ekleme butonuna tıkladınğında not ekleme ekranı açılacak
            val note: Note? =null
            var mViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
            mViewModel.newNoteLiveData.postValue(Note("","","","",false,null,""))

        }

        allNoteAdapter= AllNoteAdapter()
        allNoteAdapter.setOnNoteListClickListener(this)

        allNoteAdapter.changeStatus(this)



        isempty=sharedPref!!.getBoolean("isEmpty",true)

        if(isempty){
            mview.note_list.visibility=View.GONE
            mview.warning.visibility=View.VISIBLE

        }else{
            allnote=Gson().fromJson<NoteList>(sharedPref!!.getString("noteList", null),NoteList::class.java)

            if(allnote.notes.size==0){

                mview.note_list.visibility=View.GONE
                mview.warning.visibility=View.VISIBLE

            }else{

                for ((index, value) in allnote.notes.withIndex()) {
                    allnote.notes.get(index).id=(index+1).toString()
                }

                showlist.clear()
                deletedList.clear()

                for(x in 0..allnote.notes.size-1){
                    if(!allnote.notes.get(x).isDeleted){
                            showlist.add(allnote.notes.get(x))

                    }
                }
                for ((index, value) in showlist.withIndex()) {
                    showlist.get(index).type_id=(index+1).toString()
                }


                mview.warning.visibility=View.GONE
                mview.note_list.visibility=View.VISIBLE

                allNoteAdapter.submitList(showlist)
                mview.note_list.adapter=allNoteAdapter
                mview.note_list.layoutManager=LinearLayoutManager(context)
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
                val searchList = showlist.filter { s ->
                    s.baslik.contains(searchText, false)
                }
                allNoteAdapter.submitList(searchList)
            }
        })

        mview.trash_button.setOnClickListener {



            //seçilen elemanları selected liste atıyoruz
            for(i in 0..showlist.size-1){
                if(showlist.get(i).isDeleted){
                    selectedList.add(showlist.get(i))
                }
            }
            //show listi temizle
            showlist.clear()

            //isDeleted ı true olanları show listten kaldırıyoruz
            for(x in 0..allnote.notes.size-1){
                if(!allnote.notes.get(x).isDeleted){
                    showlist.add(allnote.notes.get(x))
                }
            }
            if(selectedList.size==0){
                Toast.makeText(activity,"Silinecek bir öğe seçin", Toast.LENGTH_SHORT).show()
            }else {
                allNoteAdapter.submitList(showlist)
                mview.note_list.adapter=allNoteAdapter
                mview.note_list.layoutManager=LinearLayoutManager(context)


                editor!!.putString("noteList", Gson().toJson(allnote))
                editor!!.commit()

            }
        }




        return mview
    }


    override fun onClick(note: Note) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.show_note)
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.window!!.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        dialog.txt_baslik.keyListener=null
        dialog.txt_icerik.keyListener=null
        dialog.txt_reminders.keyListener=null
        dialog.txt_locaiton.keyListener=null

        dialog.txt_baslik.text=note.baslik
        dialog.txt_icerik.text=note.note

        if(note.location!=null && !note.location.equals("")){
            dialog.txt_locaiton.text=note.location
        }else{
            dialog.txt_locaiton.text="Konum bilgisi yok"
        }


        var date:String? = null
        val format = SimpleDateFormat("dd-MM-yyyy' 'HH:mm")

        if(note.alarmTime!=null){
            date=format.format(note.alarmTime!!.time)
            dialog.txt_reminders.text=date.toString()
        }else{
            dialog.txt_reminders.text="Hatırlatıcı yok"
        }


        dialog.show()




    }

    override fun changeButton(boolean: Boolean, position: Int) {
        showlist.get(position).isDeleted = boolean

    }






}