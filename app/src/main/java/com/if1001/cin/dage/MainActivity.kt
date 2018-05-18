package com.if1001.cin.dage

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_login_spotify.setOnClickListener(View.OnClickListener {
            Log.d("login_debug: ", "click login")
            val i = Intent(applicationContext, HomeActivity::class.java)
            applicationContext.startActivity(i)
        })
    }
}
