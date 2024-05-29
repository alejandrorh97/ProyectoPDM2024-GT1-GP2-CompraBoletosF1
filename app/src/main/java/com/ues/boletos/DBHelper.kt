package com.ues.boletos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "ps19005.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_USUARIOS_TABLE = """
            CREATE TABLE Usuarios (
                idUsuario INTEGER PRIMARY KEY AUTOINCREMENT,
                nombreUsuario TEXT NOT NULL,
                email TEXT NOT NULL,
                password TEXT NOT NULL
            )
        """
        db.execSQL(CREATE_USUARIOS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun insertUsuario(nombreUsuario: String, email: String, password: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombreUsuario", nombreUsuario)
            put("email", email)
            put("password", password)
        }
        db.insert("Usuarios", null, values)
        db.close()
    }

    fun verificarUsuario(email: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM Usuarios WHERE email = '$email' AND password = '$password'"
        val cursor = db.rawQuery(query, null)
        val result = cursor.count > 0
        cursor.close()
        db.close()
        return result
    }

    fun existeUsuario(email: String): Boolean {
        val db = readableDatabase
        val query = "SELECT idUsuario FROM Usuarios WHERE email = '$email'"
        val cursor = db.rawQuery(query, null)
        val result = cursor.count > 0
        cursor.close()
        db.close()
        return result
    }
}