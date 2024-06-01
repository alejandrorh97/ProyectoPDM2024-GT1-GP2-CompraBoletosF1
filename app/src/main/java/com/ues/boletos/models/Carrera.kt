package com.ues.boletos.models

data class Carrera(
    var id: Int,
    var circuitoId: Int,
    var fecha: String,
    var vueltas: Int,
    var circuito: Circuito? = null
)

data class NewCarrera(
    var circuitoId: Int,
    var fecha: String,
    var vueltas: Int
)