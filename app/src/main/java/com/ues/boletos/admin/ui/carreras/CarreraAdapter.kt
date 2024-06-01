import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.ues.boletos.models.Carrera
import com.ues.boletos.R

class CarreraAdapter(
    private val context: Activity,
    private val carreras: ArrayList<Carrera>,
    private val onButtonClickListener: OnButtonClickListener
) : ArrayAdapter<Carrera>(context, R.layout.list_carrera_item, carreras) {
    interface OnButtonClickListener {
        fun onModificarClick(alumno: Carrera)
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
        val tvDistanciaCarrera: TextView = rowView.findViewById(R.id.tvDistanciaCarrera)

        val carrera = carreras[position]
        nombreCircuito.text = carrera.circuito?.nombre
        ubicacionCircuito.text = carrera.circuito?.ubicacion
        longitudCircuito.text = "${carrera.circuito?.longitud} km"
        fechaCarrera.text = carrera.fecha
        vueltasCarrera.text = carrera.vueltas.toString()
        tvDistanciaCarrera.text = "${carrera.vueltas * carrera.circuito?.longitud!!} km"

        bEditarCarrera.setOnClickListener {
            onButtonClickListener.onModificarClick(carrera)
        }

        return rowView
    }
}