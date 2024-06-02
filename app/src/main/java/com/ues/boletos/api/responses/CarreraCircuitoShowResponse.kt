package com.ues.boletos.api.responses

data class CarreraCircuitoShowResponse(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val longitud: Int,
    val tipo: String,
)
