package com.ues.boletos.api.responses

import com.ues.boletos.comprar.DetalleCompraEntrada

data class CarreraEntradaShowResponse(
    val id: Int,
    val tipo_entrada: String,
    val precio: Double,
    val cantidad: Int,
    val sector: String,
    var cantidadAComprar: Int
)