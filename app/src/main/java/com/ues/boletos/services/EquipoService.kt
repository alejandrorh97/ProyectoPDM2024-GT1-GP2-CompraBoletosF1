package com.ues.boletos.services

import com.ues.boletos.DBHelper
import com.ues.boletos.models.Equipo

class EquipoService(private val dbHelper: DBHelper) {
    fun getEquipos():ArrayList<Equipo>{
        val db = dbHelper.readableDatabase
        val equipos = ArrayList<Equipo>()
        var cursor: android.database.Cursor? = null
        return try {
            cursor = db.rawQuery("SELECT * FROM equipos", null)
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val marca = cursor.getString(cursor.getColumnIndexOrThrow("marca"))
                val propietario = cursor.getString(cursor.getColumnIndexOrThrow("propietario"))
                val patrocinador = cursor.getString(cursor.getColumnIndexOrThrow("patrocinador"))
                equipos.add(Equipo(id, nombre, marca, propietario, patrocinador))
            }
            equipos
        } catch (e: Exception) {
            ArrayList()
        } finally {
            cursor?.close()
            db.close()
        }
    }
    fun getEquipoById(id:Int):Equipo?{
        val db = dbHelper.readableDatabase
        var cursor: android.database.Cursor? = null
        return try {
            cursor = db.rawQuery("SELECT * FROM equipos WHERE id = $id", null)
            if (cursor.moveToNext()) {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val marca = cursor.getString(cursor.getColumnIndexOrThrow("marca"))
                val propietario = cursor.getString(cursor.getColumnIndexOrThrow("propietario"))
                val patrocinador = cursor.getString(cursor.getColumnIndexOrThrow("patrocinador"))
                Equipo(id, nombre, marca, propietario, patrocinador)
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
    fun updateEquipo(equipo: Equipo):Boolean{
        val db = dbHelper.writableDatabase
        return try {
            db.execSQL("UPDATE equipos SET nombre = '${equipo.nombre}', marca = '${equipo.marca}', propietario = '${equipo.propietario}', patrocinador = '${equipo.patrocinador}' WHERE id = ${equipo.id}")
            true
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }
    fun deleteEquipo(id:Int):Boolean{
        val db = dbHelper.writableDatabase
        return try {
            db.execSQL("DELETE FROM equipos WHERE id = $id")
            true
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }
    fun createEquipo(equipo: Equipo):Boolean{
        val db = dbHelper.writableDatabase
        return try {
            db.execSQL("INSERT INTO equipos (nombre, marca, propietario, patrocinador) VALUES ('${equipo.nombre}', '${equipo.marca}', '${equipo.propietario}', '${equipo.patrocinador}')")
            true
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }
}