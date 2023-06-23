package com.example.followme

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.followme.Entity.Roboter
import com.example.followme.Retrofit.RetrofitService
import com.example.followme.Retrofit.RoboterAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        val btAddRoboter = findViewById<Button>(R.id.btAddRoboter)
        val etID = findViewById<EditText>(R.id.etID)
        val etDeleteID = findViewById<EditText>(R.id.etDeleteID)
        val btDeleteRoboter = findViewById<Button>(R.id.btDeleteRoboter)
        val retrofitService = RetrofitService()
        //neue API-Schnittstelle wird instanziert
        val api = retrofitService.getRetrofit().create(RoboterAPI::class.java)

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

        // Schaltfläche "Roboter anlegen" wurde betätigt
        btAddRoboter.setOnClickListener {
            // Holen Sie die eingegebene ID aus der TextView
            val id = etID.text.toString().toIntOrNull()

            // Überprüfen Sie, ob die eingegebene ID gültig ist
            if (id != null) {
                // Überprüfen Sie, ob das Token bereits in der Liste der gespeicherten Roboter vorhanden ist
//                if (HomeActivity.robotsUser.any { it == id }) {
//                    // Informieren Sie den Benutzer, dass das Token bereits vorhanden ist
//                    Toast.makeText(this, "Dieser Token ist bereits vorhanden.", Toast.LENGTH_SHORT).show()
//                } else {
                    // Fordern Sie den Benutzer auf, einen Namen für den neuen Roboter einzugeben
                    showNameInputDialog { name ->
                        if (name != null) {
                            // Create a new Roboter object
                            val roboter = Roboter()
                            roboter.setId(id)
                            roboter.setName(name)

                            api.saveRoboter(roboter).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    if (response.isSuccessful) {
                                        // The robot was successfully saved on the server.
                                        Toast.makeText(this@AccountActivity, "Roboter '$name' mit Token '$id' wurde erfolgreich angelegt.", Toast.LENGTH_SHORT).show()
                                        HomeActivity.updateRecyclerView()
                                    } else {
                                        // The server responded with an error.
                                        Toast.makeText(this@AccountActivity, "Server error: ${response.errorBody()}", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    // There was a network error.
                                    Toast.makeText(this@AccountActivity, "Network error: $t", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    }
                //}
            } else {
                // Informieren Sie den Benutzer, dass eine ungültige ID eingegeben wurde
                Toast.makeText(this, "Ungültige ID eingegeben", Toast.LENGTH_SHORT).show()
            }
        }

        btDeleteRoboter.setOnClickListener {
            // Holen Sie die eingegebene ID aus der TextView
            val id = etDeleteID.text.toString().toIntOrNull()

            // Überprüfen Sie, ob die eingegebene ID gültig ist
            if (id != null) {
                api.deleteRoboter(id).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            // The robot was successfully deleted on the server.
                            Toast.makeText(this@AccountActivity, "Roboter mit Token '$id' wurde erfolgreich gelöscht.", Toast.LENGTH_SHORT).show()
                            HomeActivity.updateRecyclerView()
                        } else {
                            // The server responded with an error.
                            Toast.makeText(this@AccountActivity, "Server error: ${response.errorBody()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        // There was a network error.
                        Toast.makeText(this@AccountActivity, "Network error: $t", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                // Informieren Sie den Benutzer, dass eine ungültige ID eingegeben wurde
                Toast.makeText(this, "Ungültige ID eingegeben", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showNameInputDialog(callback: (String?) -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Namen eingeben")

        // Set up the input
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK") { dialog, which ->
            val name = input.text.toString()
            // Prüfen, ob die ID gültig ist (z. B. Länge überprüfen)
            if (name.isNotBlank()) {
                dialog.dismiss()
                Toast.makeText(this, "Name eingegeben: $name", Toast.LENGTH_SHORT).show()
                callback(name)
                // return@setPositiveButton name
            } else {
                Toast.makeText(this, "Bitte Namen eingeben", Toast.LENGTH_SHORT).show()
                callback(null)
            }
        }
        builder.setNegativeButton("Abbrechen") { dialog, which ->
            dialog.cancel()
            callback(null)
        }

        builder.show()
    }
}