package com.example.followme.Entity

/**
 * Klasse um Roboter zu speichern
 * wird für die Darstellung, als auch für das Abspeichern der einzelnen Roboter benutzt
 */
class Roboter {

    /**
     * Identifier für die einzelnene Roboter
     */
    private var id: Int? = null

    private var adresse: String? = null

    private var name: String? = null

    private var isFollowing: Boolean = false

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

    fun getIsFollowing(): Boolean {
        return isFollowing;
    }

    fun setIsFollowing(isFollowing: Boolean) {
        this.isFollowing = isFollowing
    }



}