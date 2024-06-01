package com.ues.boletos.models

data class Equipo(
    val id: Int,
    val nombre: String,
    val marca: String,
    val propietario: String,
    val patrocinador: String,
) {
    override fun toString(): String {
        return "$nombre - $marca"
    }
}

data class NewEquipo(
    val nombre: String,
    val marca: String,
    val propietario: String,
    val patrocinador: String,
)