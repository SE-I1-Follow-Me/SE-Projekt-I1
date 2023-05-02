package com.example.followme

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration.getInstance
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class RouteActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private lateinit var locationOverlay: MyLocationNewOverlay
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        // This won't work unless you have imported this: org.osmdroid.config.Configuration.*
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, if you abuse osm's
        //tile servers will get you banned based on this string.

        //inflate and create the map
        setContentView(R.layout.activity_route)

        map = findViewById<MapView>(R.id.mapview)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.controller.setZoom(18.0)
        val rotationGestureOverlay = RotationGestureOverlay(map)
        rotationGestureOverlay.isEnabled
        map.setMultiTouchControls(true)
        map.overlays.add(rotationGestureOverlay)
        //Hier wird der Startpunkt sowie der Start-Zoom festgelegt
        val mapController = map.controller
        mapController.setZoom(16.5)
        val startPoint = GeoPoint(51.0405, 13.7322)
        mapController.setCenter(startPoint)

        //val markerHTW = Marker(mapview)
        //val htwGeoPoint = GeoPoint(51.2026, 13.4408);
        //markerHTW.position = htwGeoPoint

        //Hier wird ein Marker festgelegt. Dieser ist aktuell Hard Coded aber kann angepasst werden
        val markerHTW = Marker(map)
        val htwGeoPoint = GeoPoint(51.0405, 13.7322)
        markerHTW.position = htwGeoPoint
        markerHTW.icon= ContextCompat.getDrawable(this, R.drawable.ic_baseline_place_24)
        markerHTW.title = "HTW Dresden"
        markerHTW.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        map.overlays.add(markerHTW)
        map.invalidate()

        var locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map)
        // getlocation()

        map.overlays.add(locationOverlay)
        map.invalidate()

        val btHome = findViewById<ImageButton>(R.id.btHome)
        val btAdd = findViewById<ImageButton>(R.id.btAdd)
        val btAlerts = findViewById<ImageButton>(R.id.btAlerts)
        val btAccount = findViewById<ImageButton>(R.id.btAccount)

        btAdd.setOnClickListener {

            val intent = Intent(this, AddActivity::class.java)
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
    }
    /*
    private fun getlocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission_group.LOCATION) == PackageManager.PERMISSION_GRANTED)
            if (!locationOverlay.isMyLocationEnabled)
                startActivity(Intent(Settings.ACTION_LOCAT))

    }

   private fun requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission_group.LOCATION}, 10)
    }



    private fun onRequestPermissionsResult(int requestCode, String[] permisson, int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permisson, grantResult)
        if (requestCode == 10)
            if (grantResult.length == 1 && grantResult[0] == PackageManager.PERMISSION_GRANTED)
                getlocation()

    }


     */


    override fun onResume() {
        super.onResume()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        //locationOverlay.enableMyLocation()
        map.onResume() //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onPause() {
        super.onPause()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = getDefaultSharedPreferences(this)
        //Configuration.getInstance().save(this, prefs);
        //locationOverlay.disableMyLocation();
        map.onPause()  //needed for compass, my location overlays, v6.0.0 and up
    }
}