package com.ues.boletos.admin.ui.circuitos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ues.boletos.DBHelper
import com.ues.boletos.R
import com.ues.boletos.models.Circuito
import com.ues.boletos.services.CircuitoService

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditarCircuitoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditarCircuitoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dbHelper: DBHelper
    private lateinit var circuitoService: CircuitoService
    private lateinit var etNombre: EditText
    private lateinit var etLongitud: EditText
    private lateinit var etCurvas: EditText
    private lateinit var etUbicacion: EditText
    private lateinit var etUrlGoogleMaps: EditText
    private lateinit var bGuardar: Button
    private lateinit var circuito: Circuito

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var idCircuito: Int? = null
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            idCircuito = it.getInt("idCircuito")
        }
        dbHelper = DBHelper(requireContext())
        circuitoService = CircuitoService(dbHelper)
        if (idCircuito != null) {
            val result = circuitoService.getCircuitoById(idCircuito!!)
            if (result != null) {
                circuito = result
            } else {
                Toast.makeText(
                    requireContext(),
                    "No se ha encontrado el circuito",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().supportFragmentManager.popBackStack()
            }
        } else {
            Toast.makeText(
                requireContext(),
                "No se ha seleccionado un circuito",
                Toast.LENGTH_SHORT
            ).show()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_editar_circuito, container, false)
        initComponents(view)
        initUI()
        initListeners()
        return view
    }

    private fun initComponents(view: View) {
        etNombre = view.findViewById(R.id.etNombre)
        etLongitud = view.findViewById(R.id.etLongitud)
        etCurvas = view.findViewById(R.id.etCurvas)
        etUbicacion = view.findViewById(R.id.etUbicacion)
        etUrlGoogleMaps = view.findViewById(R.id.etUrlGoogleMaps)
        bGuardar = view.findViewById(R.id.bGuardar)
    }

    private fun initUI() {
        etNombre.setText(circuito.nombre)
        etLongitud.setText(circuito.longitud.toString())
        etCurvas.setText(circuito.curvas.toString())
        etUbicacion.setText(circuito.ubicacion)
        etUrlGoogleMaps.setText(circuito.urlGoogleMaps)
    }

    private fun initListeners() {
        bGuardar.setOnClickListener { saveCircuito() }
    }

    private fun saveCircuito() {
        val nombre = etNombre.text.toString()
        val longitud = etLongitud.text.toString().toFloat()
        val curvas = etCurvas.text.toString().toInt()
        val ubicacion = etUbicacion.text.toString()
        val urlGoogleMaps = etUrlGoogleMaps.text.toString()
        val circuito = Circuito(circuito.id, nombre, longitud, curvas, ubicacion, urlGoogleMaps)
        if (circuitoService.updateCircuit(circuito)) {
            Toast.makeText(requireContext(), "Circuito actualizado", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        } else {
            Toast.makeText(requireContext(), "Error al actualizar el circuito", Toast.LENGTH_SHORT)
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
         * @return A new instance of fragment EditarCircuitoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditarCircuitoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}