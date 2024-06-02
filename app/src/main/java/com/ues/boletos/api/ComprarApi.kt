package com.ues.boletos.api

import com.ues.boletos.api.requests.CompraRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ComprarApi {

    @POST("compras/comprar")
    fun comprar(@Body requestData: CompraRequest): Call<GenericResponse>
}