package com.ues.boletos.api

import com.ues.boletos.api.responses.CompraResponse
import retrofit2.http.GET
import retrofit2.Call

interface ComprasApi {
    @GET("perfil/compras/index")
    fun getCompras(): Call<GenericShowResponse<List<CompraResponse>>>
}