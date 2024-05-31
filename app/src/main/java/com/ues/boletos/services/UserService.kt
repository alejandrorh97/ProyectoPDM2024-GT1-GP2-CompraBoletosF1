package com.ues.boletos.services

import android.content.ContentValues
import android.content.Context
import com.ues.boletos.DBHelper
import com.ues.boletos.models.NewUser

class UserService(private val context: Context) {
    private val dbHelper: DBHelper = DBHelper(context)

    fun insertUsuario(user: NewUser) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("email", user.email)
            put("nombre", user.nombre)
            put("apellido", user.apellido)
            put("telefono", user.telefono)
            put("fecha_nacimiento", user.fechaNacimiento)
            put("sexo", user.sexo)
            put("password", user.password)
        }
        db.insert("Usuarios", null, values)
        db.close()
    }

    fun verificarUsuario(email: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM Usuarios WHERE email = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        val result = cursor.count > 0
        cursor.close()
        db.close()
        return result
    }

    fun existeUsuario(email: String): Boolean {
        val db = dbHelper.readableDatabase
        val query = "SELECT id FROM Usuarios WHERE email = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        val result = cursor.count > 0
        cursor.close()
        db.close()
        return result
    }
}
