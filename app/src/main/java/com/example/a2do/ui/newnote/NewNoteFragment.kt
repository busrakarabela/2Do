package com.example.a2do.ui.newnote

import android.Manifest
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.example.a2do.R
import com.example.a2do.base.BaseFragment
import com.example.a2do.databinding.FragmentNewnoteBinding
import com.example.a2do.model.AlarmTime
import com.example.a2do.model.Note
import com.example.a2do.model.NoteList
import com.example.a2do.ui.main.MainViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_newnote.*
import kotlinx.android.synthetic.main.fragment_newnote.view.*
import kotlinx.android.synthetic.main.popup_location.*
import kotlinx.android.synthetic.main.popup_noti.*
import kotlinx.android.synthetic.main.popup_noti.btn_cancel
import kotlinx.android.synthetic.main.popup_noti.btn_saveDate
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewNoteFragment(note: Note): BaseFragment<FragmentNewnoteBinding,NewNoteViewModel>() {

    override fun getLayoutRes(): Int =R.layout.fragment_newnote
    override fun getViewModel(): Class<NewNoteViewModel> = NewNoteViewModel::class.java

    protected var  notes: ArrayList<Note> = ArrayList()
    protected var noteLis:NoteList= NoteList(emptyList())
    protected var noteitem=note
    lateinit var time:AlarmTime

    private val alarmtime=Calendar.getInstance()


    private lateinit var dialogNoti: Dialog
    private lateinit var dialogLoc:Dialog

    private lateinit var allnote:NoteList


    var mCalendar = Calendar.getInstance()
    var selectCalendar=Calendar.getInstance()
    var selectYear:Int=0
    var selectMonth:Int=0
    var selectDay:Int=0
    var selectHour:Int=0
    var selectMinute:Int=0
    var selectDate:String? = null
    var selectHours:String?= null
    var formatter: DateFormat = SimpleDateFormat("dd.MM.yyyy")
    var formathour: DateFormat= SimpleDateFormat("HH:mm")

    var alarmSet:Boolean=false

     var PERMISSION_ID:Int=42

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mview = inflater.inflate(R.layout.fragment_newnote, container, false)

        mview.btn_save.setOnClickListener {
            if(mview.edt_title.text.length==0){
                Toast.makeText(activity,"Başlık alanı boş", Toast.LENGTH_SHORT).show()
            }else if(mview.edt_note.text.length==0){
                Toast.makeText(activity,"Not alanı boş", Toast.LENGTH_SHORT).show()
            }else{
                noteitem.baslik=edt_title.text.toString()
                noteitem.note=edt_note.text.toString()

                if(alarmSet){
                    noteitem.alarmTime=alarmtime
                }

                if(notelist==null){
                    notes.add(noteitem)
                    noteLis.notes=notes

                }else{
                    allnote=Gson().fromJson<NoteList>(sharedPref!!.getString("noteList", null),NoteList::class.java)
                    notes.addAll(allnote.notes)
                    notes.add(noteitem)
                    noteLis.notes  =notes
                }


                editor!!.putBoolean("isEmpty",false)
                editor!!.putString("noteList", Gson().toJson(noteLis))
                editor!!.commit()



                var mViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
                mViewModel.allNoteLiveData.postValue(notes)
            }
        }

        mview.edt_reminders.keyListener=null
        mview.edt_location.keyListener=null


        mview.edt_reminders.setOnClickListener {
            dialogNoti.show()
        }
        mview.edt_location.setOnClickListener {
            dialogLoc.show()

        }



        dialogNoti = showRemindersDialog()
        dialogLoc= showLocationDialog()

        return mview
    }

    private fun showRemindersDialog(): Dialog {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup_noti)
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.window!!.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        dialog.dt_tarih.keyListener = null
        dialog.dt_saat.keyListener=null
        //dialog.window!!.setBackgroundDrawableResource(R.color.trasparent)


        dialog.dt_tarih.setOnClickListener {
            // val intent = Intent(context,SearchFragment::class.java)
            val year = mCalendar.get(Calendar.YEAR)
            val month = mCalendar.get(Calendar.MONTH)
            val day = mCalendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(context!!,
                android.R.style.ThemeOverlay_Material_Dialog_Alert,
                DatePickerDialog.OnDateSetListener {
                        datePicker, year, monthOfYear, dayOfMonth ->

                    mCalendar.set(Calendar.YEAR,year)
                    mCalendar.set(Calendar.MONTH,monthOfYear)
                    mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                    selectYear=mCalendar.get(Calendar.YEAR)
                    selectMonth=mCalendar.get(Calendar.MONTH)+1
                    selectDay=mCalendar.get(Calendar.DAY_OF_MONTH)

                    selectDate=formatter.format(mCalendar.time)
                    dialog.dt_tarih.setText(selectDate.toString())


                    editor!!.putString("selectDate",("$dayOfMonth,$monthOfYear,$year"))
                    editor!!.putString("selectDate",selectDate)
                    editor!!.commit()

                },year,month,day)

            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()

        }
        dialog.dt_saat.setOnClickListener {
            val hour=mCalendar.get(Calendar.HOUR_OF_DAY)
            val minute=mCalendar.get(Calendar.MINUTE)

            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)


                selectHour=cal.get(Calendar.HOUR_OF_DAY)
                selectMinute=cal.get(Calendar.MINUTE)

                selectHours=formathour.format(cal.time)
                dialog.dt_saat.setText(selectHours.toString())
            }
            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()

        }

        dialog.btn_saveDate.setOnClickListener {

            if(selectYear!=0 || selectMonth!=0  || selectDay!=0 ||  selectHour!=0 || selectMinute!=0){

                time=AlarmTime(selectYear,selectMonth,selectDay,selectHour,selectMinute)
                val time:String=
                    "$selectDay.$selectMonth.$selectYear - $selectHour:$selectMinute"
                edt_reminders.setText(time)


                //val dtStart = "2020-05-09T17:18:00Z"
                val x="$selectYear-$selectMonth-$selectDay"+"T$selectHour:$selectMinute:00Z"
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                try {
                  //  val date = format.parse(dtStart)
                    val xdate=format.parse(x)
                    alarmtime.time = xdate
                  //  System.out.println(date)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

                alarmSet=true
                dialog.dismiss()

            }else{

                Toast.makeText(activity,"Tarih seçin", Toast.LENGTH_SHORT).show()
            }


        }


        dialog.btn_cancel.setOnClickListener {
            dialog.dismiss()
        }
        return dialog
    }


    private fun showLocationDialog():Dialog{

        val dialogg = Dialog(context!!)
        dialogg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogg.setCancelable(false)
        dialogg.setContentView(R.layout.popup_location)
        dialogg.window!!.setGravity(Gravity.CENTER)
        dialogg.window!!.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)


        //dialog.window!!.setBackgroundDrawableResource(R.color.trasparent)
        dialogg.btn_cancel.setOnClickListener {
            dialogg.dismiss()
        }

        dialogg.dt_konum.keyListener=null
        dialogg.dt_konum.setOnClickListener {

        }
        return dialogg

    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(activity!!,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }



}