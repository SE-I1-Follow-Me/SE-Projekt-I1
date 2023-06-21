/**
 * Datenklasse um Roboter lokal zu handhaben, wichtig für die Anzeige der Roboter
 */
data class Robot(
    var id: Int,
    var ivRoboter : Int,
    var robotName : String,
    var tvPfeil : String,
    var gps_v : Int,
    var inet_v : Int,
    var followme : Boolean,
    var isMarked : Boolean
)
//Selbsterklärend, aber so sieht ein Robot-Objekt aus