package com.ues.boletos.api.requests

data class CarreraStoreRequest(
    val nombre: String,
    val circuito: String,
    val fecha: String,
    val lugar: String,
)
