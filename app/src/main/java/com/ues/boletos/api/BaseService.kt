package com.ues.boletos.api

import AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BaseService {
    private const val BASE_URL = "http://172.20.0.6/api/"
    private const val TOKEN = "1|0JImqZ5WmusMd908QgEzkXASYhpfZIaitwYh7YOsdb9000d3"

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(TOKEN))
        .build()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}