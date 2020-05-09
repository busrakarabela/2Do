package com.example.a2do.ui.main

import android.os.Bundle
import androidx.navigation.ui.AppBarConfiguration
import androidx.drawerlayout.widget.DrawerLayout
import android.view.Menu
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.example.a2do.R
import com.example.a2do.base.BaseActivity
import com.example.a2do.model.Note
import com.example.a2do.ui.tag.TagFragment
import com.example.a2do.ui.home.HomeFragment
import com.example.a2do.ui.newnote.NewNoteFragment
import com.example.a2do.ui.trash.TrashFragment
import com.example.a2do.util.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.custom_drawer.*
import kotlinx.android.synthetic.main.custom_drawer.view.*

class MainActivity :  BaseActivity<ViewDataBinding, MainViewModel>() {


    override fun getLayoutRes(): Int = R.layout.activity_main
    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java


    //private val mNotificationTime = Calendar.getInstance().timeInMillis + 5000 //Set after 5 seconds from the current time.
    //private var mNotified = false

    var oldFragment:FragmentType? = null
    private var notee:Note=Note("","","","",false,null)

    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        supportFragmentManager.beginTransaction().replace(R.id.frameLayoutMain, HomeFragment()).addToBackStack(Constants.HOME_FRAGMENT).commit()



     //   if (!mNotified) {
      //      NotificationUtils().setNotification(mNotificationTime, this@MainActivity)
      //  }

        val drawerLayout: DrawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.notes,
                R.id.reminders,
                R.id.new_notes,
                R.id.archive,
                R.id.trash
            ), drawerLayout
        )

        btn_menu.setOnClickListener {
            toggleLeftDrawer()
        }

        btn_close.setOnClickListener {
            drawer_layout.closeDrawer(leftDrawerMenu)
        }

        drawerLayout.trash.setOnClickListener {
            setupTrashFragment()
            drawer_layout.closeDrawer(leftDrawerMenu)

        }
        drawerLayout.notes.setOnClickListener {
            setupAllNoteFragment()
            drawer_layout.closeDrawer(leftDrawerMenu)
        }
        drawerLayout.new_notes.setOnClickListener {
            setupNewNoteFragment(notee)
            drawer_layout.closeDrawer(leftDrawerMenu)
        }

        drawerLayout.archive.setOnClickListener {
            setupArchiveFragment()
            drawer_layout.closeDrawer(leftDrawerMenu)
        }

        viewModel.newNoteLiveData.observe(this, Observer {
            setupNewNoteFragment(it)
            oldFragment=FragmentType.HomeFragment

        })
        viewModel.allNoteLiveData.observe(this, Observer {
            setupAllNoteFragment()
            oldFragment=FragmentType.NewNoteFragment

        })

    }

    fun setupNewNoteFragment(note: Note){
        supportFragmentManager.beginTransaction().replace(R.id.frameLayoutMain,NewNoteFragment(note)).addToBackStack(Constants.NEW_NOTE_FRAGMENT).commit()
    }

    fun setupAllNoteFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.frameLayoutMain,HomeFragment()).addToBackStack(Constants.NEW_NOTE_FRAGMENT).commit()

    }
    fun setupTrashFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.frameLayoutMain,TrashFragment()).addToBackStack(Constants.TRASH_FRAGMENT).commit()

    }
    fun setupArchiveFragment(){

        supportFragmentManager.beginTransaction().replace(R.id.frameLayoutMain,TagFragment()).addToBackStack(Constants.ARCHIVE_FRAGMENT).commit()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    fun toggleLeftDrawer() {
        if (drawer_layout.isDrawerOpen(leftDrawerMenu)) {
            drawer_layout.closeDrawer(leftDrawerMenu)

        } else {
            drawer_layout.openDrawer(leftDrawerMenu)
        }
    }

    enum class FragmentType{
        NewNoteFragment,HomeFragment
    }

}
