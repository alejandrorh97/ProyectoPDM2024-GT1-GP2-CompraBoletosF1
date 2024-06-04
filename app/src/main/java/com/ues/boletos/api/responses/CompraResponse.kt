package com.ues.boletos.api.responses

data class CompraResponse(
    val id: Int,
    val metodo_pago: String,
    val fecha_compra: String,
    val total: Double,
    val carrera: String,
    val detalles: List<DetallesCompraResponse>
)

data class DetallesCompraResponse(
    val id: Int,
    val cantidad: Int,
    val subtotal: Double,
    val entrada: String
)