package com.example.followme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class AlertsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerts)
        val btRoute = findViewById<ImageButton>(R.id.btRoute)
        val btAdd = findViewById<ImageButton>(R.id.btAdd)
        val btHome = findViewById<ImageButton>(R.id.btHome)
        val btAccount = findViewById<ImageButton>(R.id.btAccount)

        btAdd.setOnClickListener {

            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
        btHome.setOnClickListener {

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        btRoute.setOnClickListener {

            val intent = Intent(this, RouteActivity::class.java)
            startActivity(intent)
        }
        btAccount.setOnClickListener {

            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }
    }
}