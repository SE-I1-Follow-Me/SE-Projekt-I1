package com.example.followme.Entity


//Roboter-Klasse, die Server gefetcht wird
class Roboter {

    private var id: Int? = null

    private var Adresse: String? = null

    private var Name: String? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getAdresse(): String? {
        return Adresse
    }

    fun setAdresse(adresse: String?) {
        Adresse = adresse
    }

    fun getName(): String? {
        return Name
    }

    fun setName(name: String?) {
        Name = name
    }

}