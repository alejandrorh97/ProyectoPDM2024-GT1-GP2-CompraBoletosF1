package com.ues.boletos.services

import android.content.Context
import android.util.Log
import com.ues.boletos.DBHelper
import com.ues.boletos.models.Carrera
import com.ues.boletos.models.NewCarrera

class CarreraService(private val dbHelper: DBHelper) {

    fun getCarreras(): ArrayList<Carrera> {
        val db = dbHelper.readableDatabase
        val carreras = ArrayList<Carrera>()
        var cursor: android.database.Cursor? = null
        return try {
            cursor = db.rawQuery("SELECT * FROM carreras", null)
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val circuitoId = cursor.getInt(cursor.getColumnIndexOrThrow("circuito_id"))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
                val vueltas = cursor.getInt(cursor.getColumnIndexOrThrow("vueltas"))
                carreras.add(Carrera(id, circuitoId, fecha, vueltas))
            }
            carreras
        } catch (e: Exception) {
            ArrayList()
        } finally {
            cursor?.close()
            db.close()
        }
    }

    fun getCarrerasWithCircuito(): ArrayList<Carrera> {
        val db = dbHelper.readableDatabase
        val carreras = ArrayList<Carrera>()
        var cursor: android.database.Cursor? = null
        return try {
            val circuitoService = CircuitoService(dbHelper)
            cursor = db.rawQuery("SELECT * FROM carreras", null)
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val circuitoId = cursor.getInt(cursor.getColumnIndexOrThrow("circuito_id"))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
                val vueltas = cursor.getInt(cursor.getColumnIndexOrThrow("vueltas"))
                val circuito = circuitoService.getCircuitoById(circuitoId)
                val carrera = Carrera(id, circuitoId, fecha, vueltas)
                carrera.circuito = circuito
                carreras.add(carrera)
            }
            carreras
        } catch (e: Exception) {
            ArrayList()
        } finally {
            cursor?.close()
            db.close()
        }
    }

    fun getCarreraById(id: Int): Carrera? {
        val db = dbHelper.readableDatabase
        var cursor: android.database.Cursor? = null
        return try {
            cursor = db.rawQuery("SELECT * FROM carreras WHERE id = $id", null)
            if (cursor.moveToNext()) {
                val circuitoId = cursor.getInt(cursor.getColumnIndexOrThrow("circuito_id"))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
                val vueltas = cursor.getInt(cursor.getColumnIndexOrThrow("vueltas"))
                Carrera(id, circuitoId, fecha, vueltas)
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

    fun getCarreraWithCircuitoById(id: Int): Carrera? {
        val db = dbHelper.readableDatabase
        var cursor: android.database.Cursor? = null
        return try {
            cursor = db.rawQuery("SELECT * FROM carreras WHERE id = $id", null)
            if (cursor.moveToNext()) {
                val circuitoId = cursor.getInt(cursor.getColumnIndexOrThrow("circuito_id"))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
                val vueltas = cursor.getInt(cursor.getColumnIndexOrThrow("vueltas"))
                val circuito = CircuitoService(dbHelper).getCircuitoById(circuitoId)
                val carrera = Carrera(id, circuitoId, fecha, vueltas)
                carrera.circuito = circuito
                carrera
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

    fun insertCarrera(carrera: NewCarrera): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            db.execSQL("INSERT INTO carreras (circuito_id, fecha, vueltas) VALUES (${carrera.circuitoId}, '${carrera.fecha}', ${carrera.vueltas})")
            true
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    fun updateCarrera(carrera: Carrera): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            db.execSQL("UPDATE carreras SET circuito_id = ${carrera.circuitoId}, fecha = '${carrera.fecha}', vueltas = ${carrera.vueltas} WHERE id = ${carrera.id}")
            true
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    fun deleteCarrera(id: Int): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            db.execSQL("DELETE FROM carreras WHERE id = $id")
            true
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

}