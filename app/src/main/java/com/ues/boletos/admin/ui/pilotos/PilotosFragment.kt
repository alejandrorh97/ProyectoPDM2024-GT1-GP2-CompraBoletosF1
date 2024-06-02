package com.ues.boletos.admin.ui.pilotos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ues.boletos.DBHelper
import com.ues.boletos.R
import com.ues.boletos.databinding.FragmentEquiposBinding
import com.ues.boletos.databinding.FragmentPilotosBinding
import com.ues.boletos.models.Equipo
import com.ues.boletos.models.NewPiloto
import com.ues.boletos.models.Piloto
import com.ues.boletos.models.UserSimpleData
import com.ues.boletos.services.EquipoService
import com.ues.boletos.services.PilotoService
import com.ues.boletos.services.UserService

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PilotosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PilotosFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentPilotosBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var pilotoServices: PilotoService
    private lateinit var lvPilotos: ListView
    private lateinit var fabCrearPiloto: FloatingActionButton
    private lateinit var etApodo: EditText
    private lateinit var spUsuarios: Spinner
    private lateinit var cbActivo: CheckBox
    private lateinit var equipo: Equipo
    private lateinit var equipoService: EquipoService
    private var pilotoSelected: Piloto? = null
    private lateinit var userService: UserService
    private lateinit var usuarios: ArrayList<UserSimpleData>
    private lateinit var bGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPilotosBinding.inflate(layoutInflater)
        var idEquipo: Int? = null
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            idEquipo = it.getInt("idEquipo")
        }
        dbHelper = DBHelper(requireContext())
        pilotoServices = PilotoService(dbHelper)
        equipoService = EquipoService(dbHelper)
        userService = UserService(dbHelper)
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
        usuarios = userService.getUsersSimpleData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pilotos, container, false)
        initComponents(view)
        initUI()
        initListeners()
        return view
    }

    private fun initComponents(view: View) {
        lvPilotos = view.findViewById(R.id.lvPilotos)
        fabCrearPiloto = view.findViewById(R.id.fabCrearPiloto)
        etApodo = view.findViewById(R.id.etApodo)
        spUsuarios = view.findViewById(R.id.spUsuarios)
        cbActivo = view.findViewById(R.id.cbActivo)
        bGuardar = view.findViewById(R.id.bGuardar)
    }

    private fun initUI() {
        // todo hacer que el header tenga el nombre del equipo
        updateList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, usuarios)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spUsuarios.adapter = adapter
    }

    private fun updateList() {
        val pilotos = pilotoServices.getPilotosByEquipo(equipo.id)
        Log.d("Pilotos", pilotos.toString())
        lvPilotos.adapter =
            PilotoAdapter(requireActivity(), pilotos, object : PilotoAdapter.OnButtonClickListener {
                override fun onModificarClick(piloto: Piloto) {
                    bGuardar.text = "Modificar"
                    Toast.makeText(
                        requireContext(),
                        "Seleccionar piloto ${piloto.apodo}",
                        Toast.LENGTH_SHORT
                    ).show()
                    pilotoSelected = piloto
                    etApodo.setText(piloto.apodo)
                    cbActivo.isChecked = piloto.esta_activo

                    for (i in 0 until usuarios.size) {
                        if (usuarios[i].id == piloto.usuario_id) {
                            spUsuarios.setSelection(i)
                            break
                        }
                    }
                }
            })
    }

    private fun initListeners() {
        fabCrearPiloto.setOnClickListener {
            clearForm()
        }
        bGuardar.setOnClickListener {
            savePilot()
        }
    }

    private fun clearForm() {
        etApodo.text.clear()
        cbActivo.isChecked = true
        spUsuarios.setSelection(0)
        bGuardar.text = "Crear"
        pilotoSelected = null
        Toast.makeText(requireContext(), "Limpiando formulario", Toast.LENGTH_SHORT).show()
    }

    private fun savePilot() {
        try {
            val apodo = etApodo.text.toString()
            val usuario = spUsuarios.selectedItem as UserSimpleData
            val activo = cbActivo.isChecked
            if (pilotoSelected == null) {
                val newPiloto = NewPiloto(usuario.id, equipo.id, apodo, activo)
                val result = pilotoServices.createPiloto(newPiloto)
                if (result) {
                    Toast.makeText(requireContext(), "Piloto creado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error al crear piloto", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                val updatedPiloto = Piloto(
                    pilotoSelected!!.id,
                    usuario.id,
                    equipo.id,
                    apodo,
                    activo,
                    usuario
                )
                val result = pilotoServices.updatePiloto(updatedPiloto)
                if (result) {
                    Toast.makeText(requireContext(), "Piloto modificado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error al modificar piloto",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
            clearForm()
            updateList()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error al guardar piloto", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PilotosFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PilotosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}