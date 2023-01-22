package com.example.followme
import com.example.followme.HomeActivity
import MyAdapter
import Robot
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.followme.Entity.Roboter
import com.example.followme.Retrofit.RetrofitService
import com.example.followme.Retrofit.RoboterAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class HomeActivity : AppCompatActivity() {

    lateinit var newRecyclerView: RecyclerView
    lateinit var newArrayList: java.util.ArrayList<Robot>

    lateinit var robotlist: java.util.ArrayList<Roboter>

    lateinit var robotName: Array<String>
    lateinit var tvPfeil: Array<String>
    lateinit var ivRoboter: Array<Int>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        robotlist = arrayListOf<Roboter>()
        loadRoboter()

        tvPfeil = arrayOf(
            ">",
        )
        ivRoboter = arrayOf(
            R.drawable.a
        )

        newRecyclerView = findViewById(R.id.rvRobots)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<Robot>()
        getUserData()

        newRecyclerView.adapter = MyAdapter(newArrayList)

        // create variables for the OnClickListener
        val btRoute = findViewById<ImageButton>(R.id.btRoute)
        val btAdd = findViewById<ImageButton>(R.id.btAdd)
        val btAlerts = findViewById<ImageButton>(R.id.btAlerts)
        val btAccount = findViewById<ImageButton>(R.id.btAccount)

        // direct to the different activities
        btAdd.setOnClickListener {

            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
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

    private fun loadRoboter() {
        val retrofitService = RetrofitService()
        val roboterAPI = retrofitService.getRetrofit().create(RoboterAPI::class.java)
        val call = roboterAPI.getRobotList()
        call.enqueue(object : Callback<ArrayList<Roboter>> {
            override fun onResponse(
                call: Call<ArrayList<Roboter>>,
                response: Response<ArrayList<Roboter>>
            ) {
                if (response.isSuccessful) {
                    robotlist = response.body()?.apply { }!!
                }
            }

            override fun onFailure(call: Call<ArrayList<Roboter>>, t: Throwable) {
                Toast.makeText(
                    this@HomeActivity,
                    "Laden der Roboter fehlgeschlagen",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getUserData() {
        var i: Int = 0
        for (robot in robotlist) {
            val robots = Robot(ivRoboter[0], robotlist[i].getName().toString(), tvPfeil[0])
            newArrayList.add(robots)
            i += 1
        }
    }
}