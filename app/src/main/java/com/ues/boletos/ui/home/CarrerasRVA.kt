package com.ues.boletos.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.ues.boletos.R
import com.ues.boletos.api.responses.CarreraResponse
import com.ues.boletos.models.Carrera

public class CarrerasRVA : RecyclerView.Adapter<CarrerasRVA.ViewHolder>(){
    private val datos = mutableListOf<CarreraResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_carrera_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datos[position])
    }

    override fun getItemCount(): Int {
        return datos.size
    }

    fun addAll(nuevosDatos: List<CarreraResponse>) {
        datos.addAll(nuevosDatos)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagen: ImageView = itemView.findViewById(R.id.ivCarreraUserImagen)
        val nombre: TextView = itemView.findViewById(R.id.tvCarreraUserNombreCircuito)
        val fecha: TextView = itemView.findViewById(R.id.tvCarreraUserFecha)
        val descripcion: TextView = itemView.findViewById(R.id.tvCarreraUserDescripcion)

        fun bind(carrera: CarreraResponse) {
            nombre.text = carrera.nombre
            fecha.text = carrera.fecha
            descripcion.text = carrera.descripcion
        }
    }
}