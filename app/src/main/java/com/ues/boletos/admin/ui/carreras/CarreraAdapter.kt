import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.ues.boletos.models.CarreraItem
import com.ues.boletos.R

class CarreraAdapter(
    private val context: Activity,
    private val carreras: ArrayList<CarreraItem>,
    private val onButtonClickListener: OnButtonClickListener
) : ArrayAdapter<CarreraItem>(context, R.layout.list_carrera_item, carreras) {
    interface OnButtonClickListener {
        fun onModificarClick(alumno: CarreraItem)
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_carrera_item, null, true)

        val nombreCircuito: TextView = rowView.findViewById(R.id.nombreCircuito)
        val ubicacionCircuito: TextView = rowView.findViewById(R.id.ubicacionCircuito)
        val longitudCircuito: TextView = rowView.findViewById(R.id.longitudCircuito)
        val fechaCarrera: TextView = rowView.findViewById(R.id.fechaCarrera)
        val vueltasCarrera: TextView = rowView.findViewById(R.id.vueltasCarrera)
        val bEditarCarrera: Button = rowView.findViewById(R.id.bEditarCarrera)

        val carrera = carreras[position]
        nombreCircuito.text = carrera.nombreCircuito
        ubicacionCircuito.text = carrera.ubicacionCircuito
        longitudCircuito.text = carrera.longitudCircuito
        fechaCarrera.text = carrera.fechaCarrera
        vueltasCarrera.text = carrera.vueltasCarrera

        bEditarCarrera.setOnClickListener {
            onButtonClickListener.onModificarClick(carrera)
        }

        return rowView
    }
}