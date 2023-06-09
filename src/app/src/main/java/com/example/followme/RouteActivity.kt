package com.example.followme

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_route.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline
import timber.log.Timber
import java.util.*

class RouteActivity : AppCompatActivity() {

    private var interval: Long = 1000
    private var fastestInterval: Long = 500
    private var maxWaitTime: Long = 1000

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastLocation: Location? = null
    private var locationCallback: LocationCallback

    private val locationRequest = LocationRequest.Builder(interval)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .setMinUpdateIntervalMillis(fastestInterval)
        .setMaxUpdateDelayMillis(maxWaitTime)
        .build()

    private var requestingLocationUpdates = false

    companion object {
        const val REQUEST_CHECK_SETTINGS = 20202
    }

    init {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach { location ->
                    updateLocation(location)
                }
            }
        }
    }

    private val rnd = Random()
    private lateinit var map: MapView
    private var startPoint: GeoPoint = GeoPoint(46.55951, 15.63970)
    private lateinit var mapController: IMapController
    private var marker: Marker? = null
    private var path1: Polyline? = null
    private var endPoint: GeoPoint? = null
    private var pathOverlay: Polyline? = null
    private var pathUpdateTimer: Timer? = null

    private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val allGranted = result.values.all { it }
            Timber.d("Permissions granted: $allGranted")
            if (allGranted) {
                initCheckLocationSettings()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        val br: BroadcastReceiver = LocationProviderChangedReceiver()
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        registerReceiver(br, filter)

        Configuration.getInstance().load(applicationContext, this.getPreferences(Context.MODE_PRIVATE))

        setContentView(R.layout.activity_route)

        map = findViewById(R.id.mapview)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        mapController = map.controller
        val appPerms = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
        )
        permissionsLauncher.launch(appPerms)

        initLocation()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
        if (requestingLocationUpdates) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        map.onPause()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        readLastKnownLocation()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    private fun readLastKnownLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let { updateLocation(it) }
            }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMsg(status: MyEventLocationSettingsChange) {
        if (status.on) {
            initMap()
        } else {
            Timber.i("Stop something")
        }
    }

    private fun initCheckLocationSettings() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            Timber.d("Location settings are OK")
            MyEventLocationSettingsChange.globalState = true
            initMap()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                Timber.d("Location settings are not satisfied, showing the settings dialog")
                try {
                    exception.startResolutionForResult(
                        this@RouteActivity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Timber.d("Error showing the settings dialog")
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.d("onActivityResult for $requestCode result $resultCode")
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                initMap()
            }
        }
    }

    private fun updateLocation(newLocation: Location) {
        lastLocation = newLocation
        startPoint.longitude = newLocation.longitude
        startPoint.latitude = newLocation.latitude
        mapController.setCenter(startPoint)
        getPositionMarker().position = startPoint
        map.invalidate()
    }

    private fun initMap() {
        if (!requestingLocationUpdates) {
            requestingLocationUpdates = true
            startLocationUpdates()
        }
        mapController.setZoom(18.5)
        mapController.setCenter(startPoint)
        map.invalidate()
    }

    private fun getPath(): Polyline {
        if (path1 == null) {
            path1 = Polyline()
            path1!!.outlinePaint.color = Color.RED
            path1!!.outlinePaint.strokeWidth = 10f
            path1!!.addPoint(startPoint.clone())
            map.overlayManager.add(path1)
        }
        return path1!!
    }

    private fun getPositionMarker(): Marker {
        if (marker == null) {
            marker = Marker(map)
            marker!!.title = "Here I am"
            marker!!.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker!!.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_place_24)
            map.overlays.add(marker)
        }
        return marker!!
    }

    fun onClickDraw1(view: View?) {
        startPoint.latitude = startPoint.latitude + (rnd.nextDouble() - 0.5) * 0.001
        mapController.setCenter(startPoint)
        getPositionMarker().position = startPoint
        map.invalidate()
    }

    fun onClickDraw2(view: View?) {
        startPoint.latitude = startPoint.latitude + (rnd.nextDouble() - 0.5) * 0.001
        mapController.setCenter(startPoint)
        val circle = Polygon(map)
        circle.points = Polygon.pointsAsCircle(startPoint, 40.0 + rnd.nextInt(100))
        circle.fillPaint.color = 0x32323232
        circle.outlinePaint.color = Color.GREEN
        circle.outlinePaint.strokeWidth = 2f
        circle.title = "Area X"
        map.overlays.add(circle)
        map.invalidate()
    }

    fun onClickDraw4(view: View?) {
        startPoint.latitude = startPoint.latitude + (rnd.nextDouble() - 0.5) * 0.001
        startPoint.longitude = startPoint.longitude + (rnd.nextDouble() - 0.5) * 0.001
        getPath().addPoint(startPoint.clone())
        map.invalidate()
    }
    private fun drawPathOnMap() {
        if (startPoint == null) {
            // Set the starting location
            startPoint = GeoPoint(startPoint.latitude, startPoint.longitude)
            // Create a new path overlay on the map
            pathOverlay = Polyline()
            pathOverlay!!.outlinePaint.color = Color.RED
            pathOverlay!!.outlinePaint.strokeWidth = 10f
            pathOverlay!!.addPoint(startPoint!!)
            map.overlayManager.add(pathOverlay)
            // Update the map view
            map.invalidate()
        } else {
            if (pathUpdateTimer == null) {
                // Start updating the path every second
                pathUpdateTimer = Timer()
                pathUpdateTimer!!.schedule(object : TimerTask() {
                    override fun run() {
                        updatePath()
                    }
                }, 0, 1000)
            } else {
                // Stop updating the path
                pathUpdateTimer!!.cancel()
                pathUpdateTimer = null
            }
        }
    }

    private fun updatePath() {
        if (endPoint == null) {
            // Set the ending location
            endPoint = GeoPoint(startPoint.latitude, startPoint.longitude)
        } else {
            // Add the new point to the path overlay
            pathOverlay?.addPoint(endPoint!!)
            // Update the map view
            map.invalidate()
            // Set the ending location as the new starting location
            startPoint = endPoint as GeoPoint
            // Set the current location as the new ending location
            endPoint = GeoPoint(startPoint.latitude, startPoint.longitude)
        }
    }

    fun onClickAdd(view: View?){
        val intent = Intent(this, AddActivity::class.java)
        startActivity(intent)
    }
    fun onClickHome(view: View?){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
    fun onClickAlerts(view: View?){
        val intent = Intent(this, AlertsActivity::class.java)
        startActivity(intent)
    }
    fun onClickAccount(view: View?){
        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent)
    }
    fun onClickDraw(view: View?){
        drawPathOnMap()
    }

}

class MyEventLocationSettingsChange (val on: Boolean){
    companion object {
        var globalState = false

        fun setChangeAndPost(_on: Boolean) {
            if (globalState != _on) {
                globalState = _on
                EventBus.getDefault().post(MyEventLocationSettingsChange(_on))
            }
        }
    }
}

class LocationProviderChangedReceiver : BroadcastReceiver() {

    private var isGpsEnabled: Boolean = false
    private var isNetworkEnabled: Boolean = false

    override fun onReceive(context: Context, intent: Intent) {
        intent.action?.let { act ->
            if (act.matches("android.location.PROVIDERS_CHANGED".toRegex())) {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                Timber.i("Location Providers changed, is GPS Enabled: $isGpsEnabled")
                MyEventLocationSettingsChange.setChangeAndPost(isGpsEnabled)
            }
        }
    }
}