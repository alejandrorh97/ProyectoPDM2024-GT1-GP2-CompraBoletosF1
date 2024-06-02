package com.ues.boletos.api.responses

data class CarreraShowResponse(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val fecha: String,
    val lugar: String,
    val entradas: List<CarreraEntradaShowResponse>,
    val circuito: CarreraCircuitoShowResponse
)
