package com.example.a2do.base.notification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.a2do.R

import kotlinx.android.synthetic.main.result_activity.*

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)

        if (intent.getBooleanExtra("notification", false)) { //Just for confirmation
            txtTitleView.text = intent.getStringExtra("title")
            txtMsgView.text = intent.getStringExtra("message")

        }
    }
}