package com.ues.boletos.admin.ui.equipos

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
import com.ues.boletos.models.Equipo
import com.ues.boletos.models.NewEquipo
import com.ues.boletos.services.EquipoService

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CrearEquipoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CrearEquipoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dbHelper: DBHelper
    private lateinit var equipoService: EquipoService
    private lateinit var etNombre: EditText
    private lateinit var etMarca: EditText
    private lateinit var etPropietario: EditText
    private lateinit var etPatrocinador: EditText
    private lateinit var bGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        dbHelper = DBHelper(requireContext())
        equipoService = EquipoService(dbHelper)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_crear_equipo, container, false)
        initComponents(view)
        initUI()
        initListeners()
        return view
    }
    private fun initComponents(view: View) {
        etNombre = view.findViewById(R.id.etNombre)
        etMarca = view.findViewById(R.id.etMarca)
        etPropietario = view.findViewById(R.id.etPropietario)
        etPatrocinador = view.findViewById(R.id.etPatrocinador)
        bGuardar = view.findViewById(R.id.bGuardar)
    }

    private fun initUI() {
    }

    private fun initListeners() {
        bGuardar.setOnClickListener {
            saveEquipo()
        }
    }

    private fun saveEquipo(){
        try {
            val nombre = etNombre.text.toString()
            val marca = etMarca.text.toString()
            val propietario = etPropietario.text.toString()
            val patrocinador = etPatrocinador.text.toString()
            if (nombre.isEmpty() || marca.isEmpty() || propietario.isEmpty() || patrocinador.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Todos los campos son obligatorios",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            val newEquipo = NewEquipo(
                nombre,
                marca,
                propietario,
                patrocinador
            )
            val result = equipoService.createEquipo(newEquipo)
            if (result) {
                Toast.makeText(
                    requireContext(),
                    "Equipo creado correctamente",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Error al crear el equipo",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Error al crear el equipo",
                Toast.LENGTH_SHORT
            ).show()
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
         * @return A new instance of fragment CrearEquipoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CrearEquipoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}