package com.ues.boletos.admin.ui.equipos

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.ues.boletos.R
import com.ues.boletos.models.Equipo

class EquipoAdapter(
    private val context: Activity,
    private val arrayList: ArrayList<Equipo>,
    private val onButtonClickListener: OnButtonClickListener
) : ArrayAdapter<Equipo>(context, R.layout.list_equipo_item, arrayList) {
    interface OnButtonClickListener {
        fun onModificarClick(equipo: Equipo)
        fun onVerPilotosClick(equipo: Equipo)
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val view = inflater.inflate(R.layout.list_equipo_item, null, true)

        val bEditarEquipo: Button = view.findViewById(R.id.bEditarEquipo)
        val bVerPilotos: Button = view.findViewById(R.id.bVerPilotos)
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvMarca: TextView = view.findViewById(R.id.tvMarca)

        val equipo = arrayList[position]
        tvNombre.text = equipo.nombre
        tvMarca.text = equipo.marca

        bEditarEquipo.setOnClickListener {
            onButtonClickListener.onModificarClick(arrayList[position])
        }
        bVerPilotos.setOnClickListener {
            onButtonClickListener.onVerPilotosClick(arrayList[position])
        }

        return view
    }
}