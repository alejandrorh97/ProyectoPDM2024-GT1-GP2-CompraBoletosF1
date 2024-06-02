package com.ues.boletos.services

import android.content.ContentValues
import android.content.Context
import android.widget.Toast
import com.ues.boletos.DBHelper
import com.ues.boletos.models.NewUser
import com.ues.boletos.models.UserSimpleData

class UserService(private val dbHelper: DBHelper) {

    fun getUsersSimpleData():ArrayList<UserSimpleData> {
        val db = dbHelper.readableDatabase
        val users = ArrayList<UserSimpleData>()
        var cursor: android.database.Cursor? = null
        return try {
            cursor = db.rawQuery("SELECT id, nombre, apellido FROM users WHERE rol_id != 1", null)
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
                users.add(UserSimpleData(id, nombre, apellido))
            }
            users
        } catch (e: Exception) {
            ArrayList()
        } finally {
            cursor?.close()
            db.close()
        }
    }

    fun getUserSimpleDataById(id: Int): UserSimpleData? {
        val db = dbHelper.readableDatabase
        var cursor: android.database.Cursor? = null
        return try {
            cursor = db.rawQuery("SELECT nombre, apellido FROM users WHERE id = ?", arrayOf(id.toString()))
            if (cursor.moveToNext()) {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
                UserSimpleData(id, nombre, apellido)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        } finally {
            cursor?.close()
            db.close()
        }
    }

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
                put("rol_id", 3)
            }
            val newRowId = db.insertOrThrow("users", null, values)
            newRowId != -1L
        } catch (e: Exception) {
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
            false
        } finally {
            cursor?.close()
            db.close()
        }
    }

    fun isAdmin(email: String): Boolean {
        val db = dbHelper.readableDatabase
        var cursor: android.database.Cursor? = null
        return try {
            cursor = db.rawQuery(
                "SELECT * FROM users WHERE email = ? AND rol_id = 1",
                arrayOf(email)
            )
            val result = cursor.count > 0
            result
        } catch (e: Exception) {
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
            false
        } finally {
            cursor?.close()
            db.close()
        }
    }
}
