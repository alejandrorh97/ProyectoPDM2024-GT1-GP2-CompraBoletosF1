package com.ues.boletos.api.responses

data class BaseApiResponse<T>(
    val data: List<T>,
    val links: Links,
    val meta: Meta
)
