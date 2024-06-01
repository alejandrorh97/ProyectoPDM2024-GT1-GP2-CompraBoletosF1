package com.ues.boletos.models

data class Carrera(
    val id: Int,
    val circuitoId: Int,
    val fecha: String,
    val vueltas: Int,
    var circuito: Circuito? = null
)