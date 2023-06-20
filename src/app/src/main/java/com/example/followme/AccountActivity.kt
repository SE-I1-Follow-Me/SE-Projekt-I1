package com.example.followme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

/**
 * Klasse um die Funktionalität der Activity "Account" zu gewährleisten
 */
class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        val btRoute = findViewById<ImageButton>(R.id.btRoute)
        val btAdd = findViewById<ImageButton>(R.id.btAdd)
        val btAlerts = findViewById<ImageButton>(R.id.btAlerts)
        val btHome = findViewById<ImageButton>(R.id.btHome)


        // !!! Home ist in diesem layout fälschlicherweise add
        btAdd.setOnClickListener {

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        btHome.setOnClickListener {

            DialogHelper.showIdInputDialog(this) { id ->
                if (id != null) {
                    DialogHelper.handleIdInput(this, id)
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)

                }
            }
        }
        btAlerts.setOnClickListener {

            val intent = Intent(this, AlertsActivity::class.java)
            startActivity(intent)
        }
        btRoute.setOnClickListener {

            val intent = Intent(this, RouteActivity::class.java)
            startActivity(intent)
        }
    }
}