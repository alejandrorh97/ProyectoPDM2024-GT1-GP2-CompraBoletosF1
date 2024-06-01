package com.ues.boletos.models

data class Usuario(
    val id: Int,
    val rol_id: Int,
    val email: String,
    val nombre: String,
    val apellido: String,
    val telefono: String,
    val fecha_nacimiento: String,
    val sexo: String,
    val token: String?,
    val password: String?
) {
    override fun toString(): String {
        return "$nombre $apellido"
    }
}

