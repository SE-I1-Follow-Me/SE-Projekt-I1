package com.example.followme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val btRoute = findViewById<ImageButton>(R.id.btRoute)
        val btHome = findViewById<ImageButton>(R.id.btHome)
        val btAlerts = findViewById<ImageButton>(R.id.btAlerts)
        val btAccount = findViewById<ImageButton>(R.id.btAccount)

        btRoute.setOnClickListener {

            val intent = Intent(this, RouteActivity::class.java)
            startActivity(intent)
        }
        btHome.setOnClickListener {

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        btAlerts.setOnClickListener {

            val intent = Intent(this, AlertsActivity::class.java)
            startActivity(intent)
        }
        btAccount.setOnClickListener {

            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }
    }
}