package com.ues.boletos.services

import android.content.ContentValues
import android.content.Context
import android.widget.Toast
import com.ues.boletos.DBHelper
import com.ues.boletos.models.NewUser

class UserService(private val context: Context) {
    private val dbHelper: DBHelper = DBHelper(context)
    fun insertUsuario(user: NewUser): Boolean {
        val db = dbHelper.writableDatabase
        return try{
            val values = ContentValues().apply {
                put("email", user.email)
                put("nombre", user.nombre)
                put("apellido", user.apellido)
                put("telefono", user.telefono)
                put("fecha_nacimiento", user.fechaNacimiento)
                put("sexo", user.sexo)
                put("password", user.password)
                put("rol_id", 2)
                //obtener fecha actual en formato yyyy-MM-dd HH:mm:ss
                put("created_at", "datetime('now')")
            }
            val newRowId = db.insertOrThrow("users", null, values)
            newRowId != -1L
        } catch (e: Exception) {
            Toast.makeText(context, "Error al insertar usuario: ${e.message}", Toast.LENGTH_SHORT)
                .show()
            false
        } finally {
            db.close()
        }
    }

    fun verificarUsuario(email: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        var cursor: android.database.Cursor? = null
        return try {
            cursor = db.rawQuery(
                "SELECT * FROM users WHERE email = ? AND password = ?",
                arrayOf(email, password)
            )
            val result = cursor.count > 0
            result
        } catch (e: Exception) {
            Toast.makeText(context, "Error al verificar usuario: ${e.message}", Toast.LENGTH_SHORT)
                .show()
            false
        } finally {
            cursor?.close()
            db.close()
        }
    }

    fun existeUsuario(email: String): Boolean {
        val db = dbHelper.readableDatabase
        var cursor: android.database.Cursor? = null
        return try {
            cursor = db.rawQuery("SELECT id FROM users WHERE email = ?", arrayOf(email))
            val result = cursor.count > 0
            result
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Error al comprobar existencia de usuario: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
            false
        } finally {
            cursor?.close()
            db.close()
        }
    }
}
