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
import com.ues.boletos.services.EquipoService

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditarEquipoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditarEquipoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dbHelper: DBHelper
    private lateinit var equipoService: EquipoService
    private lateinit var equipo: Equipo
    private lateinit var etNombre: EditText
    private lateinit var etMarca: EditText
    private lateinit var etPropietario: EditText
    private lateinit var etPatrocinador: EditText
    private lateinit var bGuardar: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var idEquipo: Int? = null
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            idEquipo = it.getInt("idEquipo")
        }
        dbHelper = DBHelper(requireContext())
        equipoService = EquipoService(dbHelper)
        if (idEquipo != null) {
            val result = equipoService.getEquipoById(idEquipo!!)
            if (result != null) {
                equipo = result
            } else {
                Toast.makeText(
                    requireContext(),
                    "No se ha encontrado el equipo",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_editar_equipo, container, false)
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
        etNombre.setText(equipo.nombre)
        etMarca.setText(equipo.marca)
        etPropietario.setText(equipo.propietario)
        etPatrocinador.setText(equipo.patrocinador)
    }

    private fun initListeners() {
        bGuardar.setOnClickListener {
            saveEquipo()
        }
    }

    private fun saveEquipo() {
        // todo: validar campos
        try {
            val nombre = etNombre.text.toString()
            val marca = etMarca.text.toString()
            val propietario = etPropietario.text.toString()
            val patrocinador = etPatrocinador.text.toString()
            val newEquipo = Equipo(
                equipo.id,
                nombre,
                marca,
                propietario,
                patrocinador
            )
            val result = equipoService.updateEquipo(newEquipo)
            if (result) {
                Toast.makeText(
                    requireContext(),
                    "Equipo actualizado correctamente",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Error al actualizar el equipo",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Error al actualizar el equipo",
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
         * @return A new instance of fragment EditarEquipoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditarEquipoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}