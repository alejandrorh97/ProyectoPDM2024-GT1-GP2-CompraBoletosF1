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
        db.execSQL("""
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                rol_id INTEGER,
                email TEXT UNIQUE NOT NULL,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                telefono TEXT NOT NULL,
                fecha_nacimiento DATE NOT NULL,
                sexo TEXT NOT NULL,
                token TEXT,
                password TEXT NOT NULL,
                created_at DATETIME NOT NULL,
                updated_at DATETIME,
                FOREIGN KEY (rol_id) REFERENCES roles(id)
            );
        """)

        db.execSQL("""
            CREATE TABLE roles (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                descripcion TEXT NOT NULL
            );
        """)

        db.execSQL("""
            CREATE TABLE metodos_pagos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                numero_tarjeta TEXT NOT NULL,
                fecha_vencimiento DATE NOT NULL,
                cvv TEXT NOT NULL
            );
        """)

        db.execSQL("""
            CREATE TABLE circuitos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                longitud REAL NOT NULL,
                curvas INTEGER NOT NULL,
                ubicacion TEXT NOT NULL,
                url_google_maps TEXT
            );
        """)

        db.execSQL("""
            CREATE TABLE carreras (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                circuito_id INTEGER NOT NULL,
                fecha DATETIME NOT NULL,
                vueltas INTEGER NOT NULL,
                FOREIGN KEY (circuito_id) REFERENCES circuitos(id)
            );
        """)

        db.execSQL("""
            CREATE TABLE equipos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                marca TEXT NOT NULL,
                propietario TEXT NOT NULL,
                patrocinador TEXT NOT NULL
            );
        """)

        db.execSQL("""
            CREATE TABLE pilotos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario_id INTEGER NOT NULL,
                equipo_id INTEGER NOT NULL,
                apodo TEXT NOT NULL,
                esta_activo BOOLEAN NOT NULL,
                FOREIGN KEY (equipo_id) REFERENCES equipos(id),
                FOREIGN KEY (usuario_id) REFERENCES users(id)
            );
        """)

        db.execSQL("""
            CREATE TABLE asiento_tipos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                descripcion TEXT NOT NULL,
                precio REAL NOT NULL
            );
        """)

        db.execSQL("""
            CREATE TABLE asientos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                tipo_id INTEGER NOT NULL,
                circuito_id INTEGER NOT NULL,
                fila INTEGER NOT NULL,
                columna INTEGER NOT NULL,
                esta_activo BOOLEAN NOT NULL,
                FOREIGN KEY (tipo_id) REFERENCES asiento_tipos(id),
                FOREIGN KEY (circuito_id) REFERENCES circuitos(id),
                UNIQUE (circuito_id, fila, columna)
            );
        """)

        db.execSQL("""
            CREATE TABLE tickets (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                carrera_id INTEGER NOT NULL,
                usuario_id INTEGER NOT NULL,
                fecha_compra DATE NOT NULL,
                concepto TEXT NOT NULL,
                FOREIGN KEY (usuario_id) REFERENCES users(id),
                FOREIGN KEY (carrera_id) REFERENCES carreras(id)
            );
        """)

        db.execSQL("""
            CREATE TABLE ticket_asientos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                ticket_id INTEGER NOT NULL,
                asiento_id INTEGER NOT NULL,
                precio REAL NOT NULL,
                FOREIGN KEY (ticket_id) REFERENCES tickets(id),
                FOREIGN KEY (asiento_id) REFERENCES asientos(id)
            );
        """)

        db.execSQL("""
            CREATE TABLE carrera_pilotos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                carrera_id INTEGER NOT NULL,
                piloto_id INTEGER NOT NULL,
                puesto INTEGER,
                FOREIGN KEY (carrera_id) REFERENCES carreras(id),
                FOREIGN KEY (piloto_id) REFERENCES pilotos(id)
            );
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

}