package com.ues.boletos.admin.ui.pilotos

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.ues.boletos.R
import com.ues.boletos.models.Piloto

class PilotoAdapter(
    private val context: Activity,
    private val arrayList: ArrayList<Piloto>,
    private val onButtonClickListener: OnButtonClickListener
) : ArrayAdapter<Piloto>(context, R.layout.list_piloto_item, arrayList) {
    interface OnButtonClickListener {
        fun onModificarClick(piloto: Piloto)
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val view = inflater.inflate(R.layout.list_piloto_item, null, true)

        val bEditarPiloto: Button = view.findViewById(R.id.bEditarPiloto)
        val tvApodo: TextView = view.findViewById(R.id.tvApodo)
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)

        val piloto = arrayList[position]
        tvNombre.text = """${piloto.usuario?.nombre} ${piloto.usuario?.apellido}"""
        tvApodo.text = piloto.apodo

        bEditarPiloto.setOnClickListener {
            onButtonClickListener.onModificarClick(arrayList[position])
        }

        return view
    }
}