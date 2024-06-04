package com.ues.boletos.ui.compras

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ues.boletos.R
import com.ues.boletos.api.ApiClient
import com.ues.boletos.api.ComprasApi
import com.ues.boletos.api.GenericShowResponse
import com.ues.boletos.api.TokenManager
import com.ues.boletos.api.responses.BaseApiResponse
import com.ues.boletos.api.responses.CompraResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ComprasFragment : Fragment() {

    private lateinit var adapter: ComprasRVA
    private lateinit var rvCompras: RecyclerView
    private lateinit var tvNodata: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home_mis_carreras, container, false)
        this.tvNodata = view.findViewById(R.id.tvNodata)
        this.rvCompras = view.findViewById(R.id.rvCompras)
        this.rvCompras.layoutManager = LinearLayoutManager(requireContext())
        this.adapter = ComprasRVA()
        this.rvCompras.adapter = this.adapter
        cargarDatos()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun cargarDatos() {
        val tokenManager = TokenManager(requireContext())
        val apiClient = ApiClient(tokenManager)
        val call = apiClient.createService<ComprasApi>().getCompras()

        call.enqueue(object : Callback<GenericShowResponse<List<CompraResponse>>> {
            override fun onResponse(
                call: Call<GenericShowResponse<List<CompraResponse>>>,
                response: Response<GenericShowResponse<List<CompraResponse>>>
            ) {
                if (response.isSuccessful) {
                    val compras = response.body()?.data
                    if (compras != null) {
                        adapter.setDatos(compras)
                    }
                    if (compras.isNullOrEmpty()) {
                        tvNodata.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<GenericShowResponse<List<CompraResponse>>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error al cargar las compras", Toast.LENGTH_SHORT).show()
            }
        })
    }
}