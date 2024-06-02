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
        db.execSQL(
            """
                    INSERT INTO roles (nombre, descripcion)
                    VALUES
                        ('Administrador', 'Rol de administrador del sistema'),
                        ('Piloto', 'Piloto de carreras de autos'),
                        ('Usuario', 'Compradores de boletos de carreras de autos');
                """
        )

        // Insert asiento_tipos
        db.execSQL(
            """
                    INSERT INTO asiento_tipos (nombre, descripcion, precio)
                    VALUES
                        ('Premium', 'Asiento premium con vista privilegiada', 150.0),
                        ('VIP', 'Asiento VIP con servicios exclusivos', 100.0),
                        ('Normal', 'Asiento regular', 50.0);
                """
        )

        // Insertar usuario administrador
        db.execSQL(
            """
                    INSERT INTO users (rol_id, nombre, apellido, email, password, fecha_nacimiento, sexo, telefono, token)
                    VALUES
                        (1, 'admin', 'Admin', 'admin@example.com', 'admin', '1980-01-01', 'Hombre', "77777777", 'admin');
                """
        )
        // Insertar circuitos
        db.execSQL(
            """
                    INSERT INTO circuitos (nombre, longitud, curvas, ubicacion, url_google_maps)
                    VALUES
                        ('Circuito Relámpago', 4.2, 12, 'Ciudad Veloz', 'https://maps.google.com/circuito-relampago'),
                        ('Circuito Fuego', 3.8, 10, 'Ciudad Ardiente', 'https://maps.google.com/circuito-fuego'),
                        ('Circuito Tormenta', 5.1, 15, 'Ciudad Tempestad', 'https://maps.google.com/circuito-tormenta'),
                        ('Circuito Viento', 4.5, 14, 'Ciudad Ventosa', 'https://maps.google.com/circuito-viento'),
                        ('Circuito Hielo', 3.6, 9, 'Ciudad Helada', 'https://maps.google.com/circuito-hielo'),
                        ('Circuito Lava', 4.8, 13, 'Ciudad Volcánica', 'https://maps.google.com/circuito-lava'),
                        ('Circuito Agua', 4.3, 11, 'Ciudad Acuática', 'https://maps.google.com/circuito-agua'),
                        ('Circuito Tierra', 4.1, 10, 'Ciudad Terrestre', 'https://maps.google.com/circuito-tierra');
                """
        )

        // Insertar equipos
        db.execSQL(
            """
                    INSERT INTO equipos (nombre, marca, propietario, patrocinador)
                    VALUES
                        ('Equipo Rayo Veloz', 'Rayo Motors', 'John Doe', 'Speedy Corp'),
                        ('Equipo Fuego Rojo', 'Fire Racing', 'Jane Smith', 'Blaze Co'),
                        ('Equipo Tormenta Azul', 'Storm Sports', 'Mike Johnson', 'Thunder Inc'),
                        ('Equipo Relámpago Amarillo', 'Flash Racing', 'Sarah Davis', 'Bolt Enterprises'),
                        ('Equipo Trueno Negro', 'Thunder Motors', 'David Wilson', 'Strike Industries'),
                        ('Equipo Viento Verde', 'Wind Racing', 'Emily Thompson', 'Gust Co'),
                        ('Equipo Hielo Plateado', 'Ice Racing', 'Robert Brown', 'Frost Corp'),
                        ('Equipo Lava Naranja', 'Magma Motors', 'Jessica Lee', 'Inferno Inc'),
                        ('Equipo Agua Cristalina', 'Aqua Racing', 'Michael Clark', 'Wave Enterprises'),
                        ('Equipo Tierra Dorada', 'Earth Motors', 'Jennifer Martinez', 'Terra Co'),
                        ('Equipo Cielo Celeste', 'Sky Racing', 'Daniel Johnson', 'Cloud Inc'),
                        ('Equipo Sol Radiante', 'Sun Motors', 'Sophia Anderson', 'Shine Corp'),
                        ('Equipo Luna Plateada', 'Moon Racing', 'Matthew Davis', 'Glow Enterprises'),
                        ('Equipo Estrella Brillante', 'Star Motors', 'Olivia Wilson', 'Sparkle Inc'),
                        ('Equipo Cosmos Negro', 'Cosmic Racing', 'Ethan Thompson', 'Galaxy Co'),
                        ('Equipo Universo Azul', 'Universe Motors', 'Ava Brown', 'Infinity Inc'),
                        ('Equipo Galaxia Roja', 'Galaxy Racing', 'Noah Lee', 'Nova Enterprises'),
                        ('Equipo Nebulosa Verde', 'Nebula Motors', 'Emma Clark', 'Stellar Co'),
                        ('Equipo Cometa Amarillo', 'Comet Racing', 'Liam Martinez', 'Meteor Inc'),
                        ('Equipo Planeta Morado', 'Planet Motors', 'Isabella Johnson', 'Orbit Corp');
                """
        )
        //Insertar usuarios pilotos
        db.execSQL(
            """
                INSERT INTO users (rol_id, email, nombre, apellido, telefono, fecha_nacimiento, sexo, token, password)
                VALUES 
                    (2, 'lewis.hamilton@example.com', 'Lewis', 'Hamilton', '555-0101', '1985-01-07', 'Hombre', NULL, 'securepassword'),
                    (2, 'sebastian.vettel@example.com', 'Sebastian', 'Vettel', '555-0102', '1987-07-03', 'Hombre', NULL, 'securepassword'),
                    (2, 'max.verstappen@example.com', 'Max', 'Verstappen', '555-0103', '1997-09-30', 'Hombre', NULL, 'securepassword'),
                    (2, 'fernando.alonso@example.com', 'Fernando', 'Alonso', '555-0104', '1981-07-29', 'Hombre', NULL, 'securepassword'),
                    (2, 'kimi.raikkonen@example.com', 'Kimi', 'Raikkonen', '555-0105', '1979-10-17', 'Hombre', NULL, 'securepassword'),
                    (2, 'charles.leclerc@example.com', 'Charles', 'Leclerc', '555-0106', '1997-10-16', 'Hombre', NULL, 'securepassword'),
                    (2, 'valtteri.bottas@example.com', 'Valtteri', 'Bottas', '555-0107', '1989-08-28', 'Hombre', NULL, 'securepassword'),
                    (2, 'daniel.ricciardo@example.com', 'Daniel', 'Ricciardo', '555-0108', '1989-07-01', 'Hombre', NULL, 'securepassword'),
                    (2, 'sergio.perez@example.com', 'Sergio', 'Perez', '555-0109', '1990-01-26', 'Hombre', NULL, 'securepassword'),
                    (2, 'lance.stroll@example.com', 'Lance', 'Stroll', '555-0110', '1998-10-29', 'Hombre', NULL, 'securepassword'),
                    (2, 'george.russell@example.com', 'George', 'Russell', '555-0111', '1998-02-15', 'Hombre', NULL, 'securepassword'),
                    (2, 'carlos.sainz@example.com', 'Carlos', 'Sainz', '555-0112', '1994-09-01', 'Hombre', NULL, 'securepassword'),
                    (2, 'lando.norris@example.com', 'Lando', 'Norris', '555-0113', '1999-11-13', 'Hombre', NULL, 'securepassword'),
                    (2, 'esteban.ocon@example.com', 'Esteban', 'Ocon', '555-0114', '1996-09-17', 'Hombre', NULL, 'securepassword'),
                    (2, 'pierre.gasly@example.com', 'Pierre', 'Gasly', '555-0115', '1996-02-07', 'Hombre', NULL, 'securepassword'),
                    (2, 'nicholas.latifi@example.com', 'Nicholas', 'Latifi', '555-0116', '1995-06-29', 'Hombre', NULL, 'securepassword'),
                    (2, 'antonio.giovinazzi@example.com', 'Antonio', 'Giovinazzi', '555-0117', '1993-12-14', 'Hombre', NULL, 'securepassword'),
                    (2, 'mick.schumacher@example.com', 'Mick', 'Schumacher', '555-0118', '1999-03-22', 'Hombre', NULL, 'securepassword'),
                    (2, 'kevin.magnussen@example.com', 'Kevin', 'Magnussen', '555-0119', '1992-10-05', 'Hombre', NULL, 'securepassword'),
                    (2, 'alexander.albon@example.com', 'Alexander', 'Albon', '555-0120', '1996-03-23', 'Hombre', NULL, 'securepassword');
                """
        )
        // Insertar pilotos
        db.execSQL(
            """
                INSERT INTO pilotos (usuario_id, equipo_id, apodo, esta_activo)
                VALUES
                    (1, 1, 'Speed Demon', 1),
                    (2, 2, 'Red Fury', 1),
                    (3, 3, 'Blue Thunder', 1),
                    (4, 4, 'Yellow Lightning', 1),
                    (5, 5, 'Black Thunder', 1),
                    (6, 6, 'Green Breeze', 1),
                    (7, 7, 'Silver Frost', 1),
                    (8, 8, 'Orange Inferno', 1),
                    (9, 9, 'Crystal Wave', 1),
                    (10, 10, 'Golden Earth', 1),
                    (11, 11, 'Sky Flyer', 1),
                    (12, 12, 'Radiant Sun', 1),
                    (13, 13, 'Silver Moon', 1),
                    (14, 14, 'Bright Star', 1),
                    (15, 15, 'Cosmic Racer', 1),
                    (16, 16, 'Blue Universe', 1),
                    (17, 17, 'Red Galaxy', 1),
                    (18, 18, 'Green Nebula', 1),
                    (19, 19, 'Yellow Comet', 1),
                    (20, 20, 'Purple Planet', 1);
                """
        )
    }

}