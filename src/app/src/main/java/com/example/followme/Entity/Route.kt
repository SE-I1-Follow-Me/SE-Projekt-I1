package com.example.followme.Entity

/**
 * Klasse um Routen zu speichern
 * wird für die Darstellung, als auch für das Abspeichern der einzelnen Routen benutzt
 */
class Route {

    private var id: Int? = null

    /**
     * Integer, was einen bereits vorhandenen Id des gefahrenen Roboter nimmt
     */
    private var drivenBy: Int? = null

    private var name: String? = null

    private var coordinates: ArrayList<Coordinates> = ArrayList<Coordinates>()


    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getDrivenBy(): Int? {
        return drivenBy
    }

    fun setDrivenBy(drivenBy: Int?) {
        this.drivenBy = drivenBy
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getCoordinates(): List<Coordinates?>? {
        return coordinates
    }

    fun setCoordinates(coordinates: List<Coordinates?>?) {
        this.coordinates = coordinates as ArrayList<Coordinates>
    }

}

