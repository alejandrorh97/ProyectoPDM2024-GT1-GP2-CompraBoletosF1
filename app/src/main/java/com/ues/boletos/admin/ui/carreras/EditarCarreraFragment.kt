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
import com.ues.boletos.services.CarreraService
import com.ues.boletos.services.CircuitoService
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditarCarreraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditarCarreraFragment : Fragment() {
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
    private lateinit var carrera: Carrera

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var idCarrera: Int? = null
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            idCarrera = it.getInt("idCarrera")
        }
        dbHelper = DBHelper(requireContext())
        carreraService = CarreraService(dbHelper)
        circuitoService = CircuitoService(dbHelper)
        if (idCarrera != null) {
            val result = carreraService.getCarreraWithCircuitoById(idCarrera!!)
            if (result == null) {
                Toast.makeText(requireContext(), "Carrera no encontrada", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                carrera = result
                hora = carrera.fecha.split(" ")[1]
                fecha = carrera.fecha.split(" ")[0]
            }
        } else {
            Toast.makeText(requireContext(), "Carrera no encontrada", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_editar_carrera, container, false)
        initComponents(view)
        initUI()
        initListeners()
        return view
    }

    private fun initComponents(view: View) {
        spCircuito = view.findViewById(R.id.spCircuito)
        bFecha = view.findViewById(R.id.bFecha)
        etVueltas = view.findViewById(R.id.etVueltas)
        bGuardar = view.findViewById(R.id.bGuardar)
        bHora = view.findViewById(R.id.bHora)
    }

    private fun initUI() {
        bFecha.text = fecha
        bHora.text = hora
        etVueltas.setText(carrera.vueltas.toString())
        val circuitos = circuitoService.getCircuitos()
        Log.d("Circuitos", circuitos.toString())
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, circuitos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCircuito.adapter = adapter

        for (i in 0 until circuitos.size) {
            if (circuitos[i].id == carrera.circuitoId) {
                spCircuito.setSelection(i)
                break
            }
        }
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
        // todo: validar campos
        try {
            val circuito = spCircuito.selectedItem as Circuito
            val vueltas = etVueltas.text.toString().toInt()
            carrera.circuitoId = circuito.id
            carrera.fecha = "$fecha $hora"
            carrera.vueltas = vueltas
            val result = carreraService.updateCarrera(carrera)
            if (result) {
                Toast.makeText(requireContext(), "Carrera actualizada", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Error al actualizar carrera", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error al actualizar carrera", Toast.LENGTH_SHORT).show()
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditarCarreraFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditarCarreraFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}