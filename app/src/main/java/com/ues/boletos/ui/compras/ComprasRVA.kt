package com.ues.boletos.ui.compras

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ues.boletos.R
import com.ues.boletos.api.responses.CompraResponse
import com.ues.boletos.api.responses.DetallesCompraResponse
import com.ues.boletos.comprar.DetalleCarreraActivity
import com.ues.boletos.ui.home.CarrerasRVA

class ComprasRVA: RecyclerView.Adapter<ComprasRVA.ViewHolder>() {

    private val datos = mutableListOf<CompraResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComprasRVA.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_detalle_compra, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComprasRVA.ViewHolder, position: Int) {
        val modelo = datos[position]
        Log.i("ComprasRVA", "onBindViewHolder: $position")
        holder.bind(modelo)
    }

    override fun getItemCount(): Int {
        return datos.size
    }

    fun setDatos(datosNuevos: List<CompraResponse>) {
        Log.i("ComprasRVA", "setDatos: ${datosNuevos.size}")
        datos.clear()
        datos.addAll(datosNuevos)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombre)
        val txtMetodoPago: TextView = itemView.findViewById(R.id.txtMetodoPago)
        val txtTotal: TextView = itemView.findViewById(R.id.txtTotal)
        val txtFecha: TextView = itemView.findViewById(R.id.txtFecha)
        val txtDetalles: TextView = itemView.findViewById(R.id.txtDetalles)
        fun bind(compra: CompraResponse) {
            txtNombre.text = compra.carrera
            txtMetodoPago.text = compra.metodo_pago
            txtTotal.text = compra.total.toString()
            txtFecha.text = compra.fecha_compra
            txtDetalles.text = getDetalles(compra.detalles)
        }

        private fun getDetalles(detalles: List<DetallesCompraResponse>): String
        {
            var temp = ""
            for (detalle in detalles)
            {
                temp += "${detalle.entrada} X ${detalle.cantidad}U \nSubtotal: ${detalle.subtotal}"
            }
            return temp
        }
    }
}