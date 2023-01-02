package com.example.follow_me

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        var btlogin = findViewById(R.id.btlogin) as Button
        btlogin.setOnClickListener {
            val intentGoToMainActivity = Intent(this, MainActivity::class.java)
            startActivity(intentGoToMainActivity);
        }

    }




}