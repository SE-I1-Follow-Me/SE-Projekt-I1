package com.example.follow_me

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_home.*


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    // private lateinit var robotAdapter: RobotAdapter
    private  var robotadapter : RobotAdapter ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var bottomNav: BottomNavigationView = findViewById(R.id.Bottom_Navigation_View)
        supportFragmentManager




        robotadapter = RobotAdapter(mutableListOf())

        rvRobots.adapter = robotadapter
        rvRobots.layoutManager = LinearLayoutManager(this)






    }


    private val navListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
            var selectedFragment: Fragment? = null

            when (item.itemId) {
                R.id.home -> selectedFragment = HomeFragment()
                R.id.route -> selectedFragment = RouteFragment()
                R.id.add -> selectedFragment = AddFragment()
                R.id.alerts -> selectedFragment = AlertsFragment()
                R.id.account -> selectedFragment = AccountFragment()
            }

            supportFragmentManager.beginTransaction().replace(R.id.Frame_Layout, selectedFragment!!).commit()
            return true
        }
    }

}