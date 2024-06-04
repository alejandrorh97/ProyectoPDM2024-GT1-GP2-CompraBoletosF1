package com.ues.boletos.api

import com.ues.boletos.api.requests.ActualizarPerfilRequest
import com.ues.boletos.api.responses.PerfilResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PerfilApi {

    @GET("perfil/me")
    fun getPerfil(): Call<GenericShowResponse<PerfilResponse>>

    @POST("perfil/updatePerfil")
    fun updatePerfil(@Body perfilRequest: ActualizarPerfilRequest): Call<GenericShowResponse<PerfilResponse>>
}