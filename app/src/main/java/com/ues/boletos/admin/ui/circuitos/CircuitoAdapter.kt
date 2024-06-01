package com.ues.boletos.admin.ui.circuitos

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.ues.boletos.R
import com.ues.boletos.models.Circuito

class CircuitoAdapter(
    private val context: Activity,
    private val arrayList: ArrayList<Circuito>,
    private val onButtonClickListener: OnButtonClickListener
) : ArrayAdapter<Circuito>(context, R.layout.list_circuito_item, arrayList) {
    interface OnButtonClickListener {
        fun onModificarClick(circuito: Circuito)
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val view = inflater.inflate(R.layout.list_circuito_item, null, true)

        val bEditarCircuito: Button = view.findViewById(R.id.bEditarCircuito)
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvUbicacion: TextView = view.findViewById(R.id.tvUbicacion)
        val tvLongitud: TextView = view.findViewById(R.id.tvLongitud)

        val circuito = arrayList[position]
        tvNombre.text = circuito.nombre
        tvUbicacion.text = circuito.ubicacion
        tvLongitud.text = "${circuito.longitud} km"

        bEditarCircuito.setOnClickListener {
            onButtonClickListener.onModificarClick(circuito)
        }

        return view
    }
}