package com.ues.boletos.services

import com.ues.boletos.DBHelper
import com.ues.boletos.models.Piloto

class PilotoService(private val dbHelper: DBHelper) {
    fun getPilotos(): ArrayList<Piloto> {
        val db = dbHelper.readableDatabase
        val pilotos = ArrayList<Piloto>()
        var cursor: android.database.Cursor? = null
        return try {
            cursor = db.rawQuery("SELECT * FROM pilotos", null)
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val usuario_id = cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id"))
                val equipo_id = cursor.getInt(cursor.getColumnIndexOrThrow("equipo_id"))
                val apodo = cursor.getString(cursor.getColumnIndexOrThrow("apodo"))
                val esta_activo = cursor.getInt(cursor.getColumnIndexOrThrow("esta_activo")) == 1
                pilotos.add(Piloto(id, usuario_id, equipo_id, apodo, esta_activo, null))
            }
            pilotos
        } catch (e: Exception) {
            ArrayList()
        } finally {
            cursor?.close()
            db.close()
        }
    }

    fun getPilotosByEquipo(equipo_id: Int): ArrayList<Piloto> {
        val db = dbHelper.readableDatabase
        val pilotos = ArrayList<Piloto>()
        var cursor: android.database.Cursor? = null
        return try {
            val userService = UserService(dbHelper)
            cursor = db.rawQuery("SELECT * FROM pilotos WHERE equipo_id = $equipo_id", null)
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val usuario_id = cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id"))
                val apodo = cursor.getString(cursor.getColumnIndexOrThrow("apodo"))
                val esta_activo = cursor.getInt(cursor.getColumnIndexOrThrow("esta_activo")) == 1
                val usuario = userService.getUserSimpleDataById(usuario_id)
                pilotos.add(Piloto(id, usuario_id, equipo_id, apodo, esta_activo, usuario))

            }
            pilotos
        } catch (e: Exception) {
            ArrayList()
        } finally {
            cursor?.close()
            db.close()
        }
    }

    fun updatePiloto(piloto: Piloto): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            db.execSQL("UPDATE pilotos SET usuario_id = ${piloto.usuario_id}, equipo_id = ${piloto.equipo_id}, apodo = '${piloto.apodo}', esta_activo = ${if (piloto.esta_activo) 1 else 0} WHERE id = ${piloto.id}")
            true
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    fun createPiloto(piloto: Piloto): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            db.execSQL("INSERT INTO pilotos (usuario_id, equipo_id, apodo, esta_activo) VALUES (${piloto.usuario_id}, ${piloto.equipo_id}, '${piloto.apodo}', ${if (piloto.esta_activo) 1 else 0})")
            true
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }
}