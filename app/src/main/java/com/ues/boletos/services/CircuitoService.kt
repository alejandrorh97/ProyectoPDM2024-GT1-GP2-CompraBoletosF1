package com.ues.boletos.services

import android.content.Context
import com.ues.boletos.DBHelper
import com.ues.boletos.models.Circuito

class CircuitoService(private val dbHelper: DBHelper) {
    fun getCircuitos(): ArrayList<Circuito> {
        val db = dbHelper.readableDatabase
        val circuitos = ArrayList<Circuito>()
        var cursor: android.database.Cursor? = null
        return try {
            cursor = db.rawQuery("SELECT * FROM circuitos", null)
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val longitud = cursor.getFloat(cursor.getColumnIndexOrThrow("longitud"))
                val curvas = cursor.getInt(cursor.getColumnIndexOrThrow("curvas"))
                val ubicacion = cursor.getString(cursor.getColumnIndexOrThrow("ubicacion"))
                val urlGoogleMaps =
                    cursor.getString(cursor.getColumnIndexOrThrow("url_google_maps"))
                circuitos.add(Circuito(id, nombre, longitud, curvas, ubicacion, urlGoogleMaps))
            }
            circuitos
        } catch (e: Exception) {
            ArrayList()
        } finally {
            cursor?.close()
            db.close()
        }
    }

    fun getCircuitoById(id: Int): Circuito? {
        val db = dbHelper.readableDatabase
        var cursor: android.database.Cursor? = null
        return try {
            cursor = db.rawQuery("SELECT * FROM circuitos WHERE id = $id", null)
            if (cursor.moveToNext()) {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val longitud = cursor.getFloat(cursor.getColumnIndexOrThrow("longitud"))
                val curvas = cursor.getInt(cursor.getColumnIndexOrThrow("curvas"))
                val ubicacion = cursor.getString(cursor.getColumnIndexOrThrow("ubicacion"))
                val urlGoogleMaps =
                    cursor.getString(cursor.getColumnIndexOrThrow("url_google_maps"))
                Circuito(id, nombre, longitud, curvas, ubicacion, urlGoogleMaps)
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

    fun updateCircuit(circuito: Circuito): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            db.execSQL("UPDATE circuitos SET nombre = '${circuito.nombre}', longitud = ${circuito.longitud}, curvas = ${circuito.curvas}, ubicacion = '${circuito.ubicacion}', url_google_maps = '${circuito.urlGoogleMaps}' WHERE id = ${circuito.id}")
            true
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }
}