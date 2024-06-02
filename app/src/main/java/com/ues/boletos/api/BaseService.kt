package com.ues.boletos.api

import AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BaseService {
    private const val BASE_URL = "http://3.144.23.33/api"
    private const val TOKEN = "1|u7Nk2KqknqhJfSp4BZTHL0KIRsyJdE8kfd25fLwg6509addb"

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