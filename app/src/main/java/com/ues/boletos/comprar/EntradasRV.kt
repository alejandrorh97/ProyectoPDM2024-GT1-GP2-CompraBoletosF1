package com.ues.boletos.comprar

import android.content.Context
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.TextView
import com.ues.boletos.R
import com.ues.boletos.api.responses.CarreraEntradaShowResponse

public class EntradasRV(private val context: Context, private val components: List<CarreraEntradaShowResponse>)
    : BaseAdapter()
{
    override fun getCount(): Int {
        return components.size
    }

    override fun getItem(position: Int): Any {
        return components[position]
    }

    override fun getItemId(position: Int): Long {
        return components[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.card_entrada, null)
        val tvTipoEntrada: TextView = view.findViewById(R.id.tvCardEntradaNombre)
        val tvPrecio: TextView = view.findViewById(R.id.tvCardEntradaPrecio)
        val etCantidadAComprar: EditText = view.findViewById(R.id.etCantidadAComprar)

        val component = components[position]
        tvTipoEntrada.text = component.tipo_entrada + " - " + component.sector
        tvPrecio.text = "$" + component.precio.toString()

        etCantidadAComprar.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                component.cantidadAComprar = s.toString().toInt()
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        return view
    }

    fun getEntradasAComprar(): List<CarreraEntradaShowResponse> {
        return components.filter { it.cantidadAComprar > 0 }
    }
}