package com.example.followme

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat


class LoginActivity : AppCompatActivity() {
    private lateinit var permissionResultLauncher: ActivityResultLauncher<Array<String>>

    private var permissonFINE_LOCATION: Int = -1
    private var permissonINTERNET: Int = -1
    private var permissonNETWORK_STATE: Int = -1
    private var permissonCOARSE_LOCATION: Int = -1
    private var permissonEXTERNAL_STORAGE: Int = -1
    private var permissonBACKGROUND_LOCATION: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        permissionResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            handlePermissionResult(permissions)
        }

        requestPermissions()

        val btLogin = findViewById<Button>(R.id.btLogin)
        btLogin.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handlePermissionResult(permissions: Map<String, Boolean>) {
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] != false) {
            permissonFINE_LOCATION = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        if (permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] != false) {
            permissonEXTERNAL_STORAGE = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
        if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] != false) {
            permissonCOARSE_LOCATION = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
        if (permissions[Manifest.permission.ACCESS_NETWORK_STATE] != false) {
            permissonNETWORK_STATE = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_NETWORK_STATE
            )
        }
        if (permissions[Manifest.permission.INTERNET] != false) {
            permissonINTERNET = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            )
        }
        if (permissions[Manifest.permission.ACCESS_BACKGROUND_LOCATION] != false) {
            permissonBACKGROUND_LOCATION = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    private fun requestPermissions() {
        permissonFINE_LOCATION = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        permissonINTERNET = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.INTERNET
        )
        permissonNETWORK_STATE = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_NETWORK_STATE
        )
        permissonCOARSE_LOCATION = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        permissonEXTERNAL_STORAGE = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        permissonBACKGROUND_LOCATION = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )

        val permissionRequest = ArrayList<String>()

        if (permissonBACKGROUND_LOCATION == -1)
            permissionRequest.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        if (permissonEXTERNAL_STORAGE == -1)
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissonCOARSE_LOCATION == -1)
            permissionRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        if (permissonNETWORK_STATE == -1)
            permissionRequest.add(Manifest.permission.ACCESS_NETWORK_STATE)
        if (permissonINTERNET == -1)
            permissionRequest.add(Manifest.permission.INTERNET)
        if (permissonFINE_LOCATION == -1)
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionRequest.isNotEmpty()) {
            val array = permissionRequest.toTypedArray()
            permissionResultLauncher.launch(array)
        }
    }
}