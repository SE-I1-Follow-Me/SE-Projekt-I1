package com.example.followme

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.followme.Entity.Coordinates
import com.example.followme.Entity.Route
import com.example.followme.Retrofit.RetrofitService
import com.example.followme.Retrofit.RoboterAPI
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
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
import org.osmdroid.views.overlay.Polyline
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*


/**
 * Klasse um die Funktionalität der Activity "Home" zu gewährleisten
 */
class RouteActivity : AppCompatActivity() {

    private var interval: Long = 500
    private var fastestInterval: Long = 250
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
        val FOLLOW_ME_IDS: Int = 0
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

    private lateinit var map: MapView
    private var startPoint: GeoPoint = GeoPoint(46.55951, 15.63970)
    private lateinit var mapController: IMapController
    private var marker: Marker? = null
    private var path1: Polyline? = null
    private var routePoint: GeoPoint? = null
    private var routeEndPoint: GeoPoint? = null
    private var pathOverlay: Polyline? = null
    private var pathUpdateTimer: Timer? = null
    private var coordinatesRoute: ArrayList<Coordinates> = ArrayList<Coordinates>()

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
        if(FOLLOW_ME_IDS != 0) {
            drawPathOnMap()
        }
    }

    /**
     * Funktion, die das aktualisieren des Standorts fortsetzt, nachdem erneut auf die Activity gewechselt wurde
     */
    override fun onResume() {
        super.onResume()
        map.onResume()
        if (requestingLocationUpdates) {
            startLocationUpdates()
        }
    }

    /**
     * Funktion, die das aktualisieren des Standorts pausiert, nachdem die Activity gewechselt wurde
     */
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        map.onPause()
    }

    /**
     * Funktion, die den Eventbus regisitriert, nachdem die Activity gestartet wurde
     */
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    /**
     * Funktion, die den Eventbus deregisitriert, nachdem die Activity gewechselt wurde
     */
    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    /**
     * Funktion, die den fusedLocationClient erstellt, um Standortaktualisierungen zu erhalten
     */
    private fun initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        readLastKnownLocation()
    }

    /**
     * Funktion, die die Standortaktualisierungen startet
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    /**
     * Funktion, die die Standortaktualisierungen stoppt
     */
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    /**
     * Funktion, die den letzten Standort ausließt
     */
    @SuppressLint("MissingPermission")
    private fun readLastKnownLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let { updateLocation(it) }
            }
    }

    /**
     * Dummyfunktion, da eine "@Subscribe" Methode von der timber Bibliothek gefordert wird"
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMsg(status: MyEventLocationSettingsChange) {
        if (status.on) {
            initMap()
        } else {
            Timber.i("Stop something")
        }
    }

    /**
     * Funktion um die Berechtigungen, die für das darstellen der Route notwendig sind, zu überprüfen
     */
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

    /**
     * Funktion um die GPS-Koordinaten zu aktualisieren, diese werden in "startpoint" gespeichert
     * @param newLocation neue GPS-Koordinate
     */
    private fun updateLocation(newLocation: Location) {
        lastLocation = newLocation
        startPoint.longitude = newLocation.longitude
        startPoint.latitude = newLocation.latitude
        mapController.setCenter(startPoint)
        getPositionMarker().position = startPoint
        map.invalidate()
    }

    /**
     * Funktion, die die Darstellung der Karte initialisiert
     */
    private fun initMap() {
        if (!requestingLocationUpdates) {
            requestingLocationUpdates = true
            startLocationUpdates()
        }
        mapController.setZoom(18.5)
        mapController.setCenter(startPoint)
        map.invalidate()
    }

    /**
     * Funktion um eine Makierung auf den aktuellen Standort zu setzten
     */
    private fun getPositionMarker(): Marker {
        if (marker == null) {
            marker = Marker(map)
            marker!!.title = "Standort"
            marker!!.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker!!.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_place_24)
            map.overlays.add(marker)
        }
        return marker!!
    }

    /***
     * Funktion, die die Route einzeichnet und speichert
     * nach erneuten aktivieren der Funktion wird eine Route-Objekt mit den entsprechenden Koordinaten
     * erstellt und an den Server gesendet, außerdem wird eine Name vom User gefordert
     */
    private fun drawPathOnMap() {
        if (routePoint == null) {
            // Set the starting location
            routePoint = GeoPoint(startPoint.latitude, startPoint.longitude)
            var temp = Coordinates()
            temp.setLongitude(Gson().toJson(routePoint!!.longitude))
            temp.setLatitude(Gson().toJson(routePoint!!.latitude))
            coordinatesRoute.add(temp)
            // Create a new path overlay on the map
            pathOverlay = Polyline()
            pathOverlay!!.outlinePaint.color = Color.RED
            pathOverlay!!.outlinePaint.strokeWidth = 8f
            pathOverlay!!.addPoint(routePoint!!)
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
                }, 0, 4000)
            } else {
                // Stop updating the path
                //POST der Route rein
                var temp = Route()
                temp.setCoordinates(coordinatesRoute)
                temp.setDrivenBy(FOLLOW_ME_IDS)
                temp.setId(1)
                showNameInputDialog { name ->
                    if (name != null) {
                        temp.setName(name)
                    }
                }
                    val retrofitService = RetrofitService()
                    //neue API-Schnittstelle wird instanziert
                    val api = retrofitService.getRetrofit().create(RoboterAPI::class.java)
                    api.saveRoute(temp).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                // The robot was successfully saved on the server.
                                Toast.makeText(
                                    this@RouteActivity,
                                    "Route '${temp.getName()}' wurde erfolgreich angelegt.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                HomeActivity.updateRecyclerView()
                            } else {
                                // The server responded with an error.
                                Toast.makeText(
                                    this@RouteActivity,
                                    "Server error: ${response.errorBody()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            // There was a network error.
                            Toast.makeText(
                                this@RouteActivity,
                                "Network error: $t",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                    coordinatesRoute.clear()
                    pathUpdateTimer!!.cancel()
                    pathUpdateTimer = null
                    routePoint == null
                }
            }
        }

    /**
     * Hilfsfunktion für drawPathOnMap(), wird benutzt um die Route zu erweitern
     */
    private fun updatePath() {
        if (routeEndPoint == null) {
            // Set the ending location
            routeEndPoint = GeoPoint(startPoint.latitude, startPoint.longitude)
        } else {
            // Add the new point to the path overlay
            pathOverlay?.addPoint(routeEndPoint!!)
            var temp = Coordinates()
            temp.setLongitude(Gson().toJson(routeEndPoint!!.longitude))
            temp.setLatitude(Gson().toJson(routeEndPoint!!.latitude))
            coordinatesRoute.add(temp)
            // Update the map view
            map.invalidate()
            // Set the ending location as the new starting location
            routePoint = routeEndPoint as GeoPoint
            // Set the current location as the new ending location
            routeEndPoint = GeoPoint(startPoint.latitude, startPoint.longitude)
        }
    }

    /**
     * Funktion um ein Fenster zu öffnen
     * @
     */
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

    fun onClickAdd(view: View?){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
    fun onClickHome(view: View?){
        DialogHelper.showIdInputDialog(this) { id ->
            DialogHelper.handleIdInput(this, id)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
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


/**
 * Klasse um zu ermitteln ob sich die Permission geändert haben
 */
class MyEventLocationSettingsChange (val on: Boolean){
    companion object {
        var globalState = false

        /**
         * Funktion um die geänderten Permission zu aktualisieren
         */
        fun setChangeAndPost(_on: Boolean) {
            if (globalState != _on) {
                globalState = _on
                EventBus.getDefault().post(MyEventLocationSettingsChange(_on))
            }
        }
    }
}


/**
 *  Klasse um Permissions abzufragen, falls sich die Art des GPS Signals geändert hat
 */
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
