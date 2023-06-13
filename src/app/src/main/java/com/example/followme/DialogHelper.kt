package com.example.followme

import android.content.Context
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AlertDialog.*
import java.io.File

object DialogHelper {
        fun showIdInputDialog(context: Context, callback: (Int?) -> Unit) {
        val builder = Builder(context)
        builder.setTitle("ID eingeben")

        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK") { dialog, which ->
            val idString = input.text.toString()
            val id = idString.toIntOrNull()
            if (id != null) {
                dialog.dismiss()
                callback(id)
            } else {
                callback(null)
            }
        }
        builder.setNegativeButton("Abbrechen") { dialog, which ->
            dialog.cancel()
            callback(null)
        }

        builder.show()
    }

    fun handleIdInput(context: Context, id: Int?) {
        if (id != null && !HomeActivity.robotsUser.contains(id)) {
            Toast.makeText(context, "Roboter '$id' wurde erfolgreich angelegt.", Toast.LENGTH_SHORT).show()
            saveRoboterInFile(context, id)
        } else {
            Toast.makeText(context, "ID ungültig oder schon hinzugefügt", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveRoboterInFile(context: Context, ID: Int) {
        val data = "$ID"
        val file = File(context.filesDir, "RobotsUserX.csv")

        try {
            file.appendText(data + "\n")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}