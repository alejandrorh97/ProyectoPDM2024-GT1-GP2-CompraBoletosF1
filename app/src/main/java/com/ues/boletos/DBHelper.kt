package com.ues.boletos

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
        db.execSQL(
            """
            CREATE TABLE roles (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                descripcion TEXT NOT NULL
            );
        """
        )
        db.execSQL(
            """
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
                created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (rol_id) REFERENCES roles(id)
            );
        """
        )
        db.execSQL(
            """
            CREATE TABLE metodos_pagos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                numero_tarjeta TEXT NOT NULL,
                fecha_vencimiento DATE NOT NULL,
                cvv TEXT NOT NULL
            );
        """
        )
        db.execSQL(
            """
            CREATE TABLE circuitos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                longitud REAL NOT NULL,
                curvas INTEGER NOT NULL,
                ubicacion TEXT NOT NULL,
                url_google_maps TEXT
            );
        """
        )
        db.execSQL(
            """
            CREATE TABLE carreras (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                circuito_id INTEGER NOT NULL,
                fecha DATETIME NOT NULL,
                vueltas INTEGER NOT NULL,
                FOREIGN KEY (circuito_id) REFERENCES circuitos(id)
            );
        """
        )
        db.execSQL(
            """
            CREATE TABLE equipos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                marca TEXT NOT NULL,
                propietario TEXT NOT NULL,
                patrocinador TEXT NOT NULL
            );
        """
        )
        db.execSQL(
            """
            CREATE TABLE pilotos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario_id INTEGER NOT NULL,
                equipo_id INTEGER NOT NULL,
                apodo TEXT NOT NULL,
                esta_activo BOOLEAN NOT NULL,
                FOREIGN KEY (equipo_id) REFERENCES equipos(id),
                FOREIGN KEY (usuario_id) REFERENCES users(id)
            );
        """
        )
        db.execSQL(
            """
            CREATE TABLE asiento_tipos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                descripcion TEXT NOT NULL,
                precio REAL NOT NULL
            );
        """
        )
        db.execSQL(
            """
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
        """
        )
        db.execSQL(
            """
            CREATE TABLE tickets (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                carrera_id INTEGER NOT NULL,
                usuario_id INTEGER NOT NULL,
                fecha_compra DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                concepto TEXT NOT NULL,
                FOREIGN KEY (usuario_id) REFERENCES users(id),
                FOREIGN KEY (carrera_id) REFERENCES carreras(id)
            );
        """
        )
        db.execSQL(
            """
            CREATE TABLE ticket_asientos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                ticket_id INTEGER NOT NULL,
                asiento_id INTEGER NOT NULL,
                precio REAL NOT NULL,
                FOREIGN KEY (ticket_id) REFERENCES tickets(id),
                FOREIGN KEY (asiento_id) REFERENCES asientos(id)
            );
        """
        )
        db.execSQL(
            """
            CREATE TABLE carrera_pilotos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                carrera_id INTEGER NOT NULL,
                piloto_id INTEGER NOT NULL,
                puesto INTEGER,
                FOREIGN KEY (carrera_id) REFERENCES carreras(id),
                FOREIGN KEY (piloto_id) REFERENCES pilotos(id)
            );
        """
        )

        seedDatabase(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    private fun seedDatabase(db: SQLiteDatabase) {
        // Insert roles
        db.execSQL("INSERT INTO roles (nombre, descripcion) VALUES ('Administrador', 'Rol de administrador del sistema');")
        db.execSQL("INSERT INTO roles (nombre, descripcion) VALUES ('Usuario', 'Rol de usuario regular del sistema');")

        // Insert asiento_tipos
        db.execSQL("INSERT INTO asiento_tipos (nombre, descripcion, precio) VALUES ('Premium', 'Asiento premium con vista privilegiada', 150.0);")
        db.execSQL("INSERT INTO asiento_tipos (nombre, descripcion, precio) VALUES ('VIP', 'Asiento VIP con servicios exclusivos', 100.0);")
        db.execSQL("INSERT INTO asiento_tipos (nombre, descripcion, precio) VALUES ('Normal', 'Asiento regular', 50.0);")

        // Insertar usuarios
        db.execSQL(
            """
                    INSERT INTO users (rol_id, email, nombre, apellido, telefono, fecha_nacimiento, sexo, token, password) 
                    VALUES 
                    (1, 'admin', 'Admin', 'User', '1234567890', '1980-01-01', 'Hombre', null, 'admin'),
                    (2, 'user1@example.com', 'Regular', 'User', '0987654321', '1990-01-01', 'Mujer', null, 'asdf'),
                    (2, 'user2@example.com', 'John', 'Doe', '1111111111', '1985-02-02', 'Hombre', null, 'asdf'),
                    (2, 'user3@example.com', 'Jane', 'Smith', '2222222222', '1992-03-03', 'Mujer', null, 'asdf');
                """
        )
        // Insertar circuitos
        db.execSQL(
            """
                    INSERT INTO circuitos (nombre, longitud, curvas, ubicacion, url_google_maps) 
                    VALUES 
                    ('Circuito 1', 5.5, 10, 'Ubicacion 1', 'http://maps.google.com/?q=circuito1'),
                    ('Circuito 2', 3.2, 8, 'Ubicacion 2', 'http://maps.google.com/?q=circuito2'),
                    ('Circuito 3', 4.7, 12, 'Ubicacion 3', 'http://maps.google.com/?q=circuito3');
                """
        )
        // Insertar carreras
        db.execSQL(
            """
                    INSERT INTO carreras (nombre, circuito_id, fecha, vueltas) 
                    VALUES 
                    ('Carrera 1', 1, '2024-06-01 14:00:00', 50),
                    ('Carrera 2', 2, '2024-07-01 15:00:00', 60),
                    ('Carrera 3', 3, '2024-08-01 16:00:00', 70);
                """
        )
        // Insertar equipos
        db.execSQL(
            """
                    INSERT INTO equipos (nombre, marca, propietario, patrocinador) 
                    VALUES 
                    ('Equipo 1', 'Marca 1', 'Propietario 1', 'Patrocinador 1'),
                    ('Equipo 2', 'Marca 2', 'Propietario 2', 'Patrocinador 2'),
                    ('Equipo 3', 'Marca 3', 'Propietario 3', 'Patrocinador 3');
                """
        )
        // Insertar pilotos
        db.execSQL(
            """
                    INSERT INTO pilotos (usuario_id, equipo_id, apodo, esta_activo) 
                    VALUES 
                    (2, 1, 'Piloto 1', 1),
                    (3, 2, 'Piloto 2', 1),
                    (4, 3, 'Piloto 3', 0);
                """
        )
        // Insertar asientos
        db.execSQL(
            """
                    INSERT INTO asientos (tipo_id, circuito_id, fila, columna, esta_activo) 
                    VALUES 
                    (1, 1, 1, 1, 1), 
                    (2, 1, 1, 2, 1), 
                    (3, 1, 1, 3, 1),
                    (1, 2, 2, 1, 1), 
                    (2, 2, 2, 2, 1), 
                    (3, 2, 2, 3, 1),
                    (1, 3, 3, 1, 1), 
                    (2, 3, 3, 2, 1), 
                    (3, 3, 3, 3, 1);
                """
        )
        // Insertar tickets
        db.execSQL(
            """
                    INSERT INTO tickets (carrera_id, usuario_id, concepto) 
                    VALUES 
                    (1, 2, 'Compra de ticket para Carrera 1'),
                    (2, 3, 'Compra de ticket para Carrera 2'),
                    (3, 4, 'Compra de ticket para Carrera 3');
                """
        )
        // Insertar ticket_asientos
        db.execSQL(
            """
                    INSERT INTO ticket_asientos (ticket_id, asiento_id, precio) 
                    VALUES 
                    (1, 1, 150.0), 
                    (1, 2, 100.0),
                    (2, 3, 50.0),
                    (2, 4, 150.0),
                    (3, 5, 100.0),
                    (3, 6, 50.0);
                """
        )
        // Insertar carrera_pilotos
        db.execSQL(
            """
                    INSERT INTO carrera_pilotos (carrera_id, piloto_id, puesto) 
                    VALUES 
                    (1, 1, 1),
                    (2, 2, 2),
                    (3, 3, 3);
                """
        )
    }

}