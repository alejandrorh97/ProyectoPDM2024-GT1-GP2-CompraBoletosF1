package com.ues.boletos.comprar

import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ues.boletos.R
import com.ues.boletos.api.ApiClient
import com.ues.boletos.api.CarreraApi
import com.ues.boletos.api.GenericShowResponse
import com.ues.boletos.api.TokenManager
import com.ues.boletos.api.responses.CarreraEntradaShowResponse
import com.ues.boletos.api.responses.CarreraShowResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleCarreraActivity: AppCompatActivity() {

    private lateinit var carrera: CarreraShowResponse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_carrera_usuario)
        val id = intent.getIntExtra("ID", 0)

//        val apiService = BaseService.instance.create(CarreraApi::class.java)
//        val call = apiService.getCarrera(id)

        val tokenManager = TokenManager(this)
        val apiClient = ApiClient(tokenManager)
        val call = apiClient.createService<CarreraApi>().getCarrera(id)

        call.enqueue(object : Callback<GenericShowResponse<CarreraShowResponse>> {
            override fun onResponse(
                call: Call<GenericShowResponse<CarreraShowResponse>>,
                response: Response<GenericShowResponse<CarreraShowResponse>>
            ) {
                if (response.isSuccessful) {
                    val carrera = response.body()?.data
                    if (carrera != null) {
                        this@DetalleCarreraActivity.carrera = carrera
                        val tvNombre: TextView = findViewById(R.id.tvDetalleCarreraNombre)
                        val tvFecha: TextView = findViewById(R.id.tvDetalleCarreraFecha)
                        val tvDescripcion: TextView = findViewById(R.id.tvDetalleCarreraDescripcion)
                        val tvLugar: TextView = findViewById(R.id.tvDetalleCarreraLugar)
                        val lvEntradas: ListView = findViewById(R.id.lvCarrerasUsuario)
                        val adapter = EntradasRV(this@DetalleCarreraActivity, carrera.entradas)
                        val botonComprar = findViewById<Button>(R.id.btnComprarCarrera)

                        lvEntradas.adapter = adapter
                        tvNombre.text = this@DetalleCarreraActivity.carrera.nombre
                        tvFecha.text = this@DetalleCarreraActivity.carrera.fecha
                        tvDescripcion.text = this@DetalleCarreraActivity.carrera.descripcion
                        tvLugar.text = this@DetalleCarreraActivity.carrera.lugar

                        botonComprar.setOnClickListener {
                            val entradas = adapter.getEntradasAComprar()
                            if (entradas.isEmpty()) {
                                Toast.makeText(this@DetalleCarreraActivity, "No hay entradas seleccionadas", Toast.LENGTH_SHORT).show()
                            } else {
                                val detalleCompra = prepareDetalleCompra(this@DetalleCarreraActivity.carrera, adapter.getEntradasAComprar())
                                val intent = android.content.Intent(this@DetalleCarreraActivity, PagoActivity::class.java).apply {
                                    putExtra("DETALLE_COMPRA", detalleCompra)
                                }
                                startActivity(intent)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<GenericShowResponse<CarreraShowResponse>>, t: Throwable) {
                Toast.makeText(this@DetalleCarreraActivity, "Error al cargar la carrera", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    private fun prepareDetalleCompra(carrera: CarreraShowResponse, entradas: List<CarreraEntradaShowResponse>) : DetalleCompra{
        val entradasAComprar = mutableListOf<DetalleCompraEntrada>()
        for (entrada in entradas) {
            entradasAComprar.add(DetalleCompraEntrada(entrada.id, entrada.cantidadAComprar, entrada.precio))
        }
        return DetalleCompra(carrera.id, carrera.nombre, carrera.fecha, carrera.lugar, entradasAComprar)
    }
}