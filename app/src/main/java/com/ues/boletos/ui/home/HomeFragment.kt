package com.ues.boletos.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ues.boletos.R
import com.ues.boletos.api.ApiClient
import com.ues.boletos.api.CarreraApi
import com.ues.boletos.api.TokenManager
import com.ues.boletos.api.responses.BaseApiResponse
import com.ues.boletos.api.responses.CarreraResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var adapter: CarrerasRVA
    private lateinit var rvCarrera: RecyclerView
    private var page = 1
    private var loading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("HomeFragment", "onCreateView")
        val view = inflater.inflate(R.layout.fragment_home_carreras, container, false)
        this.rvCarrera = view.findViewById(R.id.rvCarrerasUsuario)
        this.rvCarrera.layoutManager = LinearLayoutManager(requireContext())
        this.adapter = CarrerasRVA()
        this.rvCarrera.adapter = this.adapter
        this.page = 1
        this.loading = false

        cargarDatos()
        addScrollListener()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun cargarDatos() {
        this.loading = true
        val tokenManager = TokenManager(requireContext())
        val apiClient = ApiClient(tokenManager)
        val call = apiClient.createService<CarreraApi>().getCarreras(this.page)
        call.enqueue(object : Callback<BaseApiResponse<CarreraResponse>> {
            override fun onResponse(
                call: Call<BaseApiResponse<CarreraResponse>>,
                response: Response<BaseApiResponse<CarreraResponse>>
            ) {
                if (response.isSuccessful) {
                    val carreras = response.body()?.data
                    if (!carreras.isNullOrEmpty()) {
                        this@HomeFragment.adapter.addAll(carreras)
                        this@HomeFragment.page++
                    }
                } else {
                    Toast.makeText(context, "Error al obtener las carreras", Toast.LENGTH_SHORT).show()
                }
                this@HomeFragment.loading = false
            }

            override fun onFailure(call: Call<BaseApiResponse<CarreraResponse>>, t: Throwable) {
                if (t is java.net.SocketTimeoutException) {
                    Toast.makeText(context, "No se pudo conectar al servidor", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("HomeFragment", "Error al obtener las carreras", t)
                    Toast.makeText(context, "Error al obtener las carreras", Toast.LENGTH_SHORT).show()
                }
                this@HomeFragment.loading = false
            }
        })
    }

    private fun addScrollListener() {
        this.rvCarrera.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.d("HomeFragment", loading.toString())
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!loading) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        cargarDatos()
                    }
                }
            }
        })
    }
}
