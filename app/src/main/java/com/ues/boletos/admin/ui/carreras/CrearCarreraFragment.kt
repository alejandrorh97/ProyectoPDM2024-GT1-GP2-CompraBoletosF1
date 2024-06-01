package com.ues.boletos.admin.ui.carreras

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.ues.boletos.DBHelper
import com.ues.boletos.R
import com.ues.boletos.models.Carrera
import com.ues.boletos.models.Circuito
import com.ues.boletos.models.NewCarrera
import com.ues.boletos.services.CarreraService
import com.ues.boletos.services.CircuitoService
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CrearCarreraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CrearCarreraFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dbHelper: DBHelper
    private lateinit var carreraService: CarreraService
    private lateinit var circuitoService: CircuitoService
    private lateinit var spCircuito: Spinner
    private lateinit var bFecha: Button
    private lateinit var bHora: Button
    private var hora: String? = null
    private var fecha: String? = null
    private lateinit var etVueltas: EditText
    private lateinit var bGuardar: Button
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        dbHelper = DBHelper(requireContext())
        carreraService = CarreraService(dbHelper)
        circuitoService = CircuitoService(dbHelper)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_crear_carrera, container, false)
        initComponents(view)
        initUI()
        initListeners()
        return view
    }
    private fun initComponents(view: View) {
        spCircuito = view.findViewById(R.id.spCircuito)
        bFecha = view.findViewById(R.id.bFecha)
        bHora = view.findViewById(R.id.bHora)
        etVueltas = view.findViewById(R.id.etVueltas)
        bGuardar = view.findViewById(R.id.bGuardar)
    }
    private fun initUI() {
        val circuitos = circuitoService.getCircuitos()
        Log.d("Circuitos", circuitos.toString())
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, circuitos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCircuito.adapter = adapter
    }

    private fun initListeners() {
        bFecha.setOnClickListener { showDatePickerDialog() }
        bGuardar.setOnClickListener { saveCarrera() }
        bHora.setOnClickListener { showTimePickerDialog() }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialog(
            requireContext(),
            { view, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                fecha = "$year-${month + 1}-$dayOfMonth"
                bFecha.text = fecha
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerDialog(
            requireContext(),
            { view, hourOfDay, minute ->
                hora = String.format("%02d:%02d:00", hourOfDay, minute)
                bHora.text = hora
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePicker.show()
    }

    private fun saveCarrera() {
        val circuito = spCircuito.selectedItem as Circuito
        val vueltas = etVueltas.text.toString().toInt()
        val carrera = NewCarrera(
            circuitoId = circuito.id,
            fecha = "$fecha $hora",
            vueltas = vueltas
        )
        val result = carreraService.insertCarrera(carrera)
        if (result) {
            Toast.makeText(requireContext(), "Carrera creada", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        } else {
            Toast.makeText(requireContext(), "Error al crear carrera", Toast.LENGTH_SHORT)
                .show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CrearCarreraFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CrearCarreraFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}