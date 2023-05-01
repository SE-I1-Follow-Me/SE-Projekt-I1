package com.example.followme.Entity


//Roboter-Klasse, die Server gefetcht wird
class Roboter {

    private var id: Int? = null

    private var adresse: String? = null

    private var name: String? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getAdresse(): String? {
        return adresse
    }

    fun setAdresse(adresse: String?) {
        this.adresse = adresse
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

}