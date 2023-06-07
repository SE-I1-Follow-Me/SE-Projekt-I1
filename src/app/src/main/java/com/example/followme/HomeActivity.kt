package com.example.followme
import MyAdapter
import Robot
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.followme.Entity.Roboter
import com.example.followme.Retrofit.RetrofitService
import com.example.followme.Retrofit.RoboterAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.ArrayList
import kotlin.properties.Delegates

class HomeActivity : AppCompatActivity() {
    //!! Initialisierungen verschiedener wichtiger Variabeln



    // companion ermöglicht es anderen activities darauf zuzugreifen
    companion object {
        //Roboterliste (view, layout, adapter)
        lateinit var rv: RecyclerView
        lateinit var layoutManager: LinearLayoutManager
        lateinit var adapter:MyAdapter


        //Array in dem die Roboter-Objekte gespeichert werden
        lateinit var newArrayList: java.util.ArrayList<Robot>



        fun updateRecyclerView() {
            rv.adapter?.notifyDataSetChanged()
        }

        lateinit var robotlist: java.util.ArrayList<Roboter>

        lateinit var robotsUser: List<Int>
    }



    //einzelne Listen zum Zusammenfügen eines Roboterobjekts

    //Name der von der DB abgerufen wird
    lateinit var robotName: Array<String>
    //statischer Pfeil der am Ende steht
    lateinit var tvPfeil: Array<String>
    //ImageView, damit die Box rund wird
    lateinit var ivRoboter: Array<Int>


    //Toggle
    lateinit var btFollowMe: Button
    var isChecked: Boolean = false
    val colorPressed = Color.parseColor("#fe5454")
    val colorDefault = Color.parseColor("#E8F3EA")

    //Beende Followme
    lateinit var btBeenden: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // FollowMe Liste filtern
        btFollowMe = findViewById<Button>(R.id.btFollowMe)

        //FollowMe Beenden
        btBeenden =findViewById<Button>(R.id.btBeenden)

        //Liste wird erstellt
        robotlist = arrayListOf<Roboter>()
        //Funktion LoadRoboter wird aufgerufen, siehe unten
        loadRoboter()

        tvPfeil = arrayOf(
            ">",
        )

        //siehe drawable a.jpg
        ivRoboter = arrayOf(
            R.drawable.a
        )


        newArrayList = arrayListOf<Robot>()

        //kreiert die einzelnen  <Roboter> Objekte
        initRecyclerView()
        //initContent()


        // create variables for the OnClickListener
        val btRoute = findViewById<ImageButton>(R.id.btRoute)
        val btAdd = findViewById<ImageButton>(R.id.btAdd)
        val btAlerts = findViewById<ImageButton>(R.id.btAlerts)
        val btAccount = findViewById<ImageButton>(R.id.btAccount)

        // direct to the different activities
        btAdd.setOnClickListener {

            showIdInputDialog { id ->
                if (id != null) {
                    // val robots = Robot(R.drawable.a, id, ">", 5, 5, false, false)
                    // HomeActivity.newArrayList.add(robots)
                    Toast.makeText(
                        this,
                        "Roboter '$id' wurde erfolgreich angelegt.",
                        Toast.LENGTH_SHORT
                    ).show()

                    // write in file
                    saveRoboterInFile(id)

                    // Hole den neuen Roboter
                    getUserData()

                }
            }

            //val intent = Intent(this, AddActivity::class.java)
            //startActivity(intent)
        }
        btRoute.setOnClickListener {

            val intent = Intent(this, RouteActivity::class.java)
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

    //Funktion um die Daten vom Server durch die Rest-API zu bekommen
    private fun loadRoboter() {
        //Retrofit wandelt JSON Format in ein Java Array um
        val retrofitService = RetrofitService()
        //neue API-Schnittstelle wird instanziert
        val roboterAPI = retrofitService.getRetrofit().create(RoboterAPI::class.java)
        //Call wird initialisiert, um später den Call zur API zu machen
        val call = roboterAPI.getRobotList()
        //Call (Abruf) wird als Request eingereiht
        call.enqueue(object : Callback<ArrayList<Roboter>> {
            //Falls die API Verbindung hat und der Server ansprechbar ist, ist es onResponse
            override fun onResponse(
                //Es wird versucht die Roboterliste abzurufen
                call: Call<ArrayList<Roboter>>,
                // Die Antwort sollte ein Array mit den Robotern sein
                response: Response<ArrayList<Roboter>>
            ) {
                //Fall dies Erfolgreich war, werden die Daten in das robotlost - Array geschrieben
                if (response.isSuccessful) {
                    robotlist = response.body()?.apply { }!!
                    //Aufruf nach erhalt der daten
                    getUserData()
                }
            }
            //Falls die API KEINE! Verbindung hat ODER! der Server NICHT! ansprechbar ist, ist es onFailure
            override fun onFailure(call: Call<ArrayList<Roboter>>, t: Throwable) {
                Toast.makeText(
                    this@HomeActivity,
                    //Dieser Fehlertext wird dann angezeigt
                    "Laden der Roboter fehlgeschlagen",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    // Schreibt das Fertige Objekt Roboter, welches dann im Recyclerview angezeigt wird
    private fun getUserData() {

        newArrayList.clear()

        // Lies aus der Datei welche roboter geladen werden sollen
        robotsUser = readDataFromFile("RobotsUserX.csv")

        //Für alle robot-Objekte, die vom Server gesendet wurde, wird ein Roboter-Objekt kreiert
        for (robot in robotlist) {

            // Wenn der Roboter geladen werden soll:
            if (robotsUser.contains(robot.getId())) {
                //Neue variable robots wird erstellt, mit der Box, den Roboter-Name (vom Robot-Objekt vom Server)
                val robots = Robot(ivRoboter[0], robot.getName().toString(), tvPfeil[0], 5, 5, false, false)
                newArrayList.add(robots)
            }

        }
        rv.adapter?.notifyDataSetChanged()
    }



    fun initRecyclerView() {

        rv = findViewById(R.id.rvRobots)
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = MyAdapter(this, newArrayList)
        rv.adapter = adapter

        adapter.setOnItemClickListener(object:MyAdapter.OnItemClickListener{
            override fun setOnClickListener(pos: Int) {
                //Toast.makeText(this@HomeActivity, "${content[pos].robotName} wurde geklickt", Toast.LENGTH_SHORT).show()
                newArrayList[pos].followme = !newArrayList[pos].followme
                adapter.notifyItemChanged(pos)
            }

        })

        btFollowMe.setOnClickListener {
            isChecked = !isChecked
            adapter.filterItems(isChecked)
            val color = if (isChecked) colorPressed else colorDefault
            btFollowMe.backgroundTintList = ColorStateList.valueOf(color)
        }

        btBeenden.setOnClickListener {
            if (isChecked) {
                adapter.endFollowMe()
            } else {
                Toast.makeText(this, "Bitte erst FollowMe aufrufen", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun showIdInputDialog(callback: (Int?) -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ID eingeben")

        // Set up the input
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK") { dialog, which ->
            val idString = input.text.toString()
            // Prüfen, ob die ID gültig ist (z. B. Parsing prüfen)
            val id = idString.toIntOrNull()
            if (id != null) {
                dialog.dismiss()
                Toast.makeText(this, "ID eingegeben: $id", Toast.LENGTH_SHORT).show()
                callback(id)
            } else {
                Toast.makeText(this, "Ungültige ID", Toast.LENGTH_SHORT).show()
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

    fun readDataFromFile(fileName: String): List<Int> {
        val dataList = mutableListOf<Int>()
        val file = File(this.filesDir, fileName)

        if (file.exists()) {
            try {
                file.forEachLine { line ->
                    val value = line.toIntOrNull()
                    if (value != null) {
                        dataList.add(value)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return dataList
    }


}