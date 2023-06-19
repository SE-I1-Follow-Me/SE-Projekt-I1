package com.example.followme

import android.content.Context
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

        // !!! Home ist in diesem layout fälschlicherweise add
        btHome.setOnClickListener {

            DialogHelper.showIdInputDialog(this) { id ->
                if (id != null) {
                    DialogHelper.handleIdInput(this, id)
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)

                }
            }
        }
        btAdd.setOnClickListener {

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

    private fun openIdInputDialog(context: Context) {
        DialogHelper.showIdInputDialog(context) { id ->
            if (id != null) {
                DialogHelper.handleIdInput(context, id)
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }
    }

}