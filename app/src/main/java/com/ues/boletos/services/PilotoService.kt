package com.ues.boletos.services

import com.ues.boletos.DBHelper
import com.ues.boletos.models.NewPiloto
import com.ues.boletos.models.Piloto
import com.ues.boletos.models.UserSimpleData

class PilotoService(private val dbHelper: DBHelper) {
    fun getPilotos(): ArrayList<Piloto> {
        val db = dbHelper.readableDatabase
        val pilotos = ArrayList<Piloto>()
        var cursor: android.database.Cursor? = null
        return try {
            cursor = db.rawQuery(
                """
            SELECT pilotos.*, users.nombre, users.apellido, users.rol_id 
            FROM pilotos 
            INNER JOIN users ON pilotos.usuario_id = users.id 
            WHERE users.rol_id = 2
        """.trimIndent(), null
            )
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val equipo_id = cursor.getInt(cursor.getColumnIndexOrThrow("equipo_id"))
                val usuario_id = cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id"))
                val apodo = cursor.getString(cursor.getColumnIndexOrThrow("apodo"))
                val esta_activo = cursor.getInt(cursor.getColumnIndexOrThrow("esta_activo")) == 1
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
                val usuario = UserSimpleData(usuario_id, nombre, apellido)
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

    fun getPilotosByEquipo(equipo_id: Int): ArrayList<Piloto> {
        val db = dbHelper.readableDatabase
        val pilotos = ArrayList<Piloto>()
        var cursor: android.database.Cursor? = null
        return try {
            cursor = db.rawQuery(
                """
            SELECT pilotos.*, users.nombre, users.apellido, users.rol_id 
            FROM pilotos 
            INNER JOIN users ON pilotos.usuario_id = users.id 
            WHERE pilotos.equipo_id = $equipo_id AND users.rol_id = 2
        """.trimIndent(), null
            )
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val usuario_id = cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id"))
                val apodo = cursor.getString(cursor.getColumnIndexOrThrow("apodo"))
                val esta_activo = cursor.getInt(cursor.getColumnIndexOrThrow("esta_activo")) == 1
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
                val usuario = UserSimpleData(usuario_id, nombre, apellido)
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

    fun createPiloto(piloto: NewPiloto): Boolean {
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

    fun deletePiloto(id: Int): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            db.execSQL("DELETE FROM pilotos WHERE id = $id")
            true
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }
}