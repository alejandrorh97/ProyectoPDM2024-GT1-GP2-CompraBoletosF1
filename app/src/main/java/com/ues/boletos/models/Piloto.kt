package com.ues.boletos.models

data class Piloto(
    val id: Int,
    val usuario_id: Int,
    val equipo_id: Int,
    val apodo: String,
    val esta_activo: Boolean,
    val usuario: Usuario?
) {
    override fun toString(): String {
        return if (usuario != null) {
            "${usuario.nombre} ${usuario.apellido}"
        } else {
            apodo
        }
    }
}

data class NewPiloto(
    val usuario_id: Int,
    val equipo_id: Int,
    val apodo: String,
    val esta_activo: Boolean
)