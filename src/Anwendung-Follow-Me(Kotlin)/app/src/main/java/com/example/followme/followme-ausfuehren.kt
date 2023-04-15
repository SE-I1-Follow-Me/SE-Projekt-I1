package com.example.followme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        val btFollow-me_starten = findViewById<ImageButton>(R.id.btFollow-me_starten)

        btFollow-me_starten.setOnClickListener {
            val mapFragment = MapFragment.newInstance()
            mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment
            mapFragment.getAsyncMap(onMapReadyCallback)
            val layerSetConfiguration = LayerSetConfiguration.Builder()
                    .mapTilesConfiguration(MAP_TILES_SOURCE_ID)
                    .trafficIncidentsTilesConfiguration(TRAFFIC_INCIDENTS_SOURCE_ID)
                    .trafficFlowTilesConfiguration(TRAFFIC_FLOW_SOURCE_ID)
                    .build()
            val fragment = MapFragment.newInstance(mapProperties)
            /** Quelle: https://developer.tomtom.com/maps-android-sdk/documentation/map-display/documentation/map-initialization#getting-started
             * Callback interface executed when the map is ready to be used.
             * The instance of this interface is set to {@link MapFragment},
             * and the {@link OnMapReadyCallback#onMapReady(TomtomMap)} is triggered
             * when the map is fully initialized and not-null.
             */
            /**
             * Callback interface executed when the map is ready to be used.
             * The instance of this interface is set to [MapFragment],
             * and the [OnMapReadyCallback.onMapReady] is triggered
             * when the map is fully initialized and not-null.
             */
            interface OnMapReadyCallback {
                /**
                 * Called when the map is ready to be used.
                 */
                fun onMapReady(@NonNull tomtomMap: TomtomMap?)
            }
            private val onMapReadyCallback = OnMapReadyCallback { tomtomMap ->
                val mapPaddingVertical = resources.getDimension(R.dimen.map_padding_vertical).toDouble()
                val mapPaddingHorizontal = resources.getDimension(R.dimen.map_padding_horizontal).toDouble()

                tomtomMap.uiSettings.currentLocationView.hide()
                tomtomMap.setPadding(
                        mapPaddingVertical, mapPaddingHorizontal,
                        mapPaddingVertical, mapPaddingHorizontal
                )
                tomtomMap.collectLogsToFile(SampleApp.LOG_FILE_PATH)
            }
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                tomtomMap.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
            MapProperties.Builder()
                    .backgroundColor(Color.BLUE)
                    .customStyleUri("asset://styles/style.json")
                    .cameraPosition(cameraPosition)
                    .cameraFocusArea(cameraFocusArea)
                    .padding(MapPadding(50.0, 40.0, 100.0, 80.0))
                    .keys(keysMap)
                    .mapStyleSource(MapStyleSource.STYLE_MERGER)
                    .layerSetConfiguration(layerSetConfiguration)
                    .build()
            val cameraPosition = CameraPosition.builder()
                    .focusPosition(LatLng(12.34, 23.45))
                    .zoom(10.0)
                    .bearing(24.0)
                    .pitch(45.2)
                    .build()
            val cameraFocusArea = CameraFocusArea.Builder(
                    BoundingBox(LatLng(52.407943, 4.808601), LatLng(52.323363, 4.969053))
            )
                    .bearing(24.0)
                    .pitch(45.2)
                    .build()
            val keysMap = mapOf(
                    ApiKeyType.MAPS_API_KEY to "maps-key",
                    ApiKeyType.TRAFFIC_API_KEY to "traffic-key"


            //val intent = Intent(this, AddActivity::class.java)
            //startActivity(intent)
        }

    }
}