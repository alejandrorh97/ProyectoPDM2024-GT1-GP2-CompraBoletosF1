package com.ues.boletos.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ues.boletos.comprar.DetalleCarreraActivity
import com.ues.boletos.R
import com.ues.boletos.api.responses.CarreraResponse

public class CarrerasRVA : RecyclerView.Adapter<CarrerasRVA.ViewHolder>(){
    private val datos = mutableListOf<CarreraResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_carrera_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelo = datos[position]
        holder.bind(modelo)

        holder.botonComprar.setOnClickListener {
            val id = modelo.id
            val intent = Intent(holder.itemView.context, DetalleCarreraActivity::class.java)
            intent.putExtra("ID", id)
            holder.itemView.context.startActivity(intent)
        }
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
        val botonComprar: Button = itemView.findViewById(R.id.btnComprarCarrera)

        fun bind(carrera: CarreraResponse) {
            nombre.text = carrera.nombre
            fecha.text = carrera.fecha
            descripcion.text = carrera.descripcion
        }
    }
}