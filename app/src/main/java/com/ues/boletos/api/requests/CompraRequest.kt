package com.ues.boletos.api.requests

data class CompraRequest(
    val carrera_id: Int,
    val entradas: List<CompraEntradaRequest>,
    val metodo_pago: MetodoPagoRequest
)
data class CompraEntradaRequest(
    val id: Int,
    val cantidad: Int
)

data class MetodoPagoRequest(
    val numero_tarjeta: String,
    val codigo_seguridad: String,
    val fecha_vencimiento: String,
    val nombre_tarjeta: String
)