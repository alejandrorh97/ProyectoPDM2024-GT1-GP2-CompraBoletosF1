package com.ues.boletos.api

import com.ues.boletos.api.responses.BaseApiResponse
import com.ues.boletos.api.responses.CarreraResponse
import com.ues.boletos.api.responses.CarreraShowResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CarreraApi {
    @GET("carreras/index")
    fun getCarreras(@Query("page") page: Int): Call<BaseApiResponse<CarreraResponse>>

    @GET("carreras/show/{id}")
    fun getCarrera(@Path("id") id: Int): Call<GenericShowResponse<CarreraShowResponse>>
}