package com.example.followme

import MyAdapter
import Robot
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {

    lateinit var newRecyclerView: RecyclerView
    lateinit var newArrayList: java.util.ArrayList<Robot>

    lateinit var robotName : Array<String>
    lateinit var tvPfeil : Array<String>
    lateinit var ivRoboter : Array<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        robotName = arrayOf(
            "Roboter 1",
            "Roboter 2",
            "Roboter 3",
            "Roboter 4",
        )
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
    private fun getUserData(){
        for (i in robotName.indices){

            val robots = Robot(ivRoboter[0],robotName[i], tvPfeil[0])
            newArrayList.add(robots)
        }
    }

}