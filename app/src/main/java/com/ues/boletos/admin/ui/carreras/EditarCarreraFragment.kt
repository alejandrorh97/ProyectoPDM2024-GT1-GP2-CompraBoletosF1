package com.ues.boletos.admin.ui.carreras

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.ues.boletos.DBHelper
import com.ues.boletos.R
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
    private lateinit var  bFecha: Button
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
        initComponents()
        initUI()
        initListeners()
        return inflater.inflate(R.layout.fragment_editar_carrera, container, false)
    }

    private fun initComponents() {
        spCircuito = requireView().findViewById(R.id.spCircuito)
        bFecha = requireView().findViewById(R.id.bFecha)
        etVueltas = requireView().findViewById(R.id.etVueltas)
        bGuardar = requireView().findViewById(R.id.bGuardar)
    }

    private fun initUI() {

    }

    private fun initListeners() {
        bFecha.setOnClickListener { showDatePickerDialog() }
        bGuardar.setOnClickListener { saveCarrera() }
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
    }

    private fun saveCarrera() {

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