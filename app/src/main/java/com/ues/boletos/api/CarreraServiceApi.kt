package com.ues.boletos.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.ues.boletos.api.requests.CarreraStoreRequest
import com.ues.boletos.api.responses.CarreraShowResponse
import com.ues.boletos.models.NewCarrera
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarreraServiceApi(private val context: Context) {
    fun guardarCarrera(nombre: String, circuito: String, fecha: String, lugar: String) {
        val carreraStoreRequest = CarreraStoreRequest(
            nombre = nombre,
            circuito = circuito,
            fecha = fecha,
            lugar = lugar
        )

        val tokenManager = TokenManager(context)
        val apiClient = ApiClient(tokenManager)

        val call = apiClient.createService<CarreraApi>().storeCarrera(carreraStoreRequest)

        call.enqueue(object : Callback<GenericShowResponse<CarreraShowResponse>> {
            override fun onResponse(
                call: Call<GenericShowResponse<CarreraShowResponse>>,
                response: Response<GenericShowResponse<CarreraShowResponse>>
            ) {
                if (response.isSuccessful) {
                    Log.i("CarreraServiceApi", "Carrera guardada en api")
                }
            }

            override fun onFailure(call: Call<GenericShowResponse<CarreraShowResponse>>, t: Throwable) {
                Log.e("CarreraServiceApi", "Error al guardar carrera en api", t)
            }
        })
    }
}