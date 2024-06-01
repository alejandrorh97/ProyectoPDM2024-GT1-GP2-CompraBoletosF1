package com.ues.boletos.models

data class Circuito(
    val id: Int,
    val nombre: String,
    val longitud: Float,
    val curvas: Int,
    val ubicacion: String,
    val urlGoogleMaps: String?
)
