package com.example.followme.Entity

/**
 * Klasse um Koordinaten zu speichern
 * wir benutzt um das Route Objekt zu erstellen
 */
class Coordinates {
    private var longitude: String? = null
    private var latitude: String? = null

    fun getLongitude(): String? {
        return longitude
    }

    fun setLongitude(longitude: String?) {
        this.longitude = longitude
    }

    fun getLatitude(): String? {
        return latitude
    }

    fun setLatitude(latitude: String?) {
        this.latitude = latitude
    }

}
