package com.example.followme

import Robot
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.followme.Retrofit.RetrofitService
import com.example.followme.Retrofit.RoboterAPI
import java.io.*

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val btRoute = findViewById<ImageButton>(R.id.btRoute)
        val btHome = findViewById<ImageButton>(R.id.btHome)
        val btAlerts = findViewById<ImageButton>(R.id.btAlerts)
        val btAccount = findViewById<ImageButton>(R.id.btAccount)
        val retrofitService = RetrofitService()
        val api = retrofitService.getRetrofit().create(RoboterAPI::class.java)


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


        // ***** Roboter hinzufügen *****

        val etID = findViewById<EditText>(R.id.etID)
        val btAddRoboter = findViewById<Button>(R.id.btAddRoboter)
        val name = ""

        // Schaltfläche "Roboter anlegen" wurde betätigt
        btAddRoboter.setOnClickListener {
            // Holen Sie die eingegebene ID aus der TextView
            val id = etID.text.toString().toIntOrNull()

            // Überprüfen Sie, ob die eingegebene ID gültig ist
            if (id != null) {
                // Überprüfen Sie, ob das Token bereits in der Liste der gespeicherten Roboter vorhanden ist
                if (HomeActivity.robotsUser.any { it == id }) {
                    // Informieren Sie den Benutzer, dass das Token bereits vorhanden ist
                    Toast.makeText(this, "Dieses Token ist bereits vorhanden.", Toast.LENGTH_SHORT).show()
                } else {
                    // Fordern Sie den Benutzer auf, einen Namen für den neuen Roboter einzugeben
                    showNameInputDialog { name ->
                        if (name != null) {
                            val robots = Robot(R.drawable.a, name, ">", 5, 5, false, false)
                            HomeActivity.newArrayList.add(robots)
                            Toast.makeText(this, "Roboter '$name' mit Token '$id' wurde erfolgreich angelegt.", Toast.LENGTH_SHORT).show()
                            HomeActivity.updateRecyclerView()

                            // write in file
                            saveRoboterInFile(id)

                        }
                        /*if (name != null) {
                            // Create a new Roboter object
                            val roboter = Roboter()
                            roboter.setId(id)
                            roboter.setName(name)

                            api.saveRoboter(roboter).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    if (response.isSuccessful) {
                                        // The robot was successfully saved on the server.
                                        Toast.makeText(this@AddActivity, "Roboter '$name' mit Token '$id' wurde erfolgreich angelegt.", Toast.LENGTH_SHORT).show()
                                        HomeActivity.updateRecyclerView()
                                    } else {
                                        // The server responded with an error.
                                        Toast.makeText(this@AddActivity, "Server error: ${response.errorBody()}", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    // There was a network error.
                                    Toast.makeText(this@AddActivity, "Network error: $t", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }*/
                    }
                }
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


    // Schreibe content in datei
    fun saveRoboterInFile(ID: Int) {
        val data = "$ID"
        val file = File(this.filesDir, "RobotsUserX.csv")

        try {
            file.appendText(data + "\n")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}