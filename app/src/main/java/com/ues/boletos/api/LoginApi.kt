package com.ues.boletos.api

import com.ues.boletos.api.requests.LoginRequest
import com.ues.boletos.api.requests.RegistrarRequest
import com.ues.boletos.api.responses.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("auth/registrar")
    fun registrar(@Body registrarRequest: RegistrarRequest): Call<LoginResponse>
}