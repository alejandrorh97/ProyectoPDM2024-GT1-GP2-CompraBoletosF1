package com.ues.boletos.comprar

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.ues.boletos.R
import com.ues.boletos.api.BaseService
import com.ues.boletos.api.ComprarApi
import com.ues.boletos.api.GenericResponse
import com.ues.boletos.api.requests.CompraEntradaRequest
import com.ues.boletos.api.requests.CompraRequest
import com.ues.boletos.api.requests.MetodoPagoRequest
import retrofit2.Callback

class PagoActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)
        val detalleCompra = intent.getParcelableExtra<DetalleCompra>("DETALLE_COMPRA")

        val tvTitulo: TextView = findViewById(R.id.tvPagoTitulo)
        val tvDetalle: TextView = findViewById(R.id.tvPagoDetalle1)
        val tvDetalle2: TextView = findViewById(R.id.tvPagoDetalle2)
        val btnPagar: Button = findViewById(R.id.btnPagar)

        tvTitulo.text = "Pago de ${detalleCompra?.titulo}"
        tvDetalle.text = getDetalleCompraString(detalleCompra!!)
        tvDetalle2.text = getTotal(detalleCompra)

        btnPagar.setOnClickListener {
            val compraRequest = getDetalleCompraRequest(detalleCompra)
            val compraService: ComprarApi = BaseService.instance.create(ComprarApi::class.java)
            val call = compraService.comprar(compraRequest)

            call.enqueue(object : Callback<GenericResponse> {
                override fun onResponse(
                    call: retrofit2.Call<GenericResponse>,
                    response: retrofit2.Response<GenericResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@PagoActivity, "Compra realizada con éxito", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@PagoActivity, "Error al realizar la compra", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<GenericResponse>, t: Throwable) {
                    Toast.makeText(this@PagoActivity, "Error al realizar la compra", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun getDetalleCompraString(detalleCompra: DetalleCompra): String {
        return "Carrera: ${detalleCompra.titulo}\n" +
                "Fecha: ${detalleCompra.fecha}\n" +
                "Lugar: ${detalleCompra.lugar}\n"
    }

    private fun getTotal(detalleCompra: DetalleCompra): String {
        var total = 0.0
        detalleCompra.entradas.forEach {
            total += (it.cantidadAComprar * it.precio)
        }
        return "Total: \$ $total"
    }

    private fun getDetalleCompraRequest(detalleCompra: DetalleCompra) : CompraRequest {
        val etNombre: TextInputEditText = findViewById(R.id.txt_nombre_tarjeta)
        val etNumero: TextInputEditText = findViewById(R.id.txt_num_tarjeta)
        val etFecha: TextInputEditText = findViewById(R.id.txt_fecha_vencimiento)
        val etCvv: TextInputEditText = findViewById(R.id.txt_cvv)

        val metodoPago: MetodoPagoRequest = MetodoPagoRequest(
            etNumero.text.toString(),
            etCvv.text.toString(),
            etFecha.text.toString(),
            etNombre.text.toString()
        )

        val entradas = detalleCompra.entradas.map {
            CompraEntradaRequest(it.id, it.cantidadAComprar)
        }

        return CompraRequest(detalleCompra.carrera, entradas, metodoPago)
    }
}