package com.example.followme

import android.content.Context
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog.*
import java.io.File

object DialogHelper {

        // Funktion öffnet ein Dialogfenster zur Eingabe eines Integerwertes
        fun showIdInputDialog(context: Context, callback: (Int?) -> Unit) {
        val builder = Builder(context)
        builder.setTitle("ID eingeben") // Title des Fensters

        val input = EditText(context) // Eingabe
        input.inputType = InputType.TYPE_CLASS_NUMBER // Input ist eine Nummer
        builder.setView(input) // Öffne den View für Nummern (Nummernblock)

        // Buttons einrichten
        builder.setPositiveButton("OK") { dialog, which ->
            val idString = input.text.toString()
            val id = idString.toIntOrNull() // id als integerwert
            if (id != null) {
                dialog.dismiss() // Schließe das Fenster
                callback(id) // gib die eingegebene ID zurück
            } else {
                callback(null) // ansonsten gib null zurück
            }
        }
        builder.setNegativeButton("Abbrechen") { dialog, which ->
            dialog.cancel() // Abbruch
            callback(null) //gib null zurück
        }

        builder.show()
    }

    // Funktion um die Eingabe zu verarbeiten
    fun handleIdInput(context: Context, id: Int?) {
        if (id != null) { // Wenn id nicht null ist, ansonsten wird fehler auch angezeigt wenn man abbricht
            if (!HomeActivity.robotsUser.contains(id)) { // Wenn die eingegebene ID noch nicht enthalten ist
                Toast.makeText(
                    context,
                    "Roboter '$id' wurde erfolgreich angelegt.",
                    Toast.LENGTH_SHORT
                ).show()
                saveRoboterInFile(context, id) // Schreibe die ID in die file -> Dieser Roboter wird dann geladen, da er in der file steht
            } else {
                Toast.makeText(context, "ID ungültig oder schon hinzugefügt", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // Funktion um eine ID in der hardcodierten datei einzutragen
    fun saveRoboterInFile(context: Context, ID: Int) {
        val data = "$ID"
        val file = File(context.filesDir, "RobotsUserX.csv") // öffne die Datei

        try {
            file.appendText(data + "\n") // Hänge die id an, wechsle in die nächste Zeile für nächste eingabe
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}