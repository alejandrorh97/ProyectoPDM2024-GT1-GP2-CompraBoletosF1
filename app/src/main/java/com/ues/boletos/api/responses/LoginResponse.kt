package com.ues.boletos.api.responses

data class LoginResponse(
    val access_token: String,
    val is_admin: Boolean
)
