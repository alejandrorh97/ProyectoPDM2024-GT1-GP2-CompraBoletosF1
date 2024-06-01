package com.ues.boletos.admin.ui.equipos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ues.boletos.DBHelper
import com.ues.boletos.R
import com.ues.boletos.databinding.FragmentEquiposBinding
import com.ues.boletos.models.Equipo
import com.ues.boletos.services.EquipoService

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EquiposFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EquiposFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentEquiposBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var equipoServices: EquipoService
    private lateinit var lvEquipos: ListView
    private lateinit var fabCrearEquipo: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentEquiposBinding.inflate(layoutInflater)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        dbHelper = DBHelper(requireContext())
        equipoServices = EquipoService(dbHelper)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_equipos, container, false)
        initComponents(view)
        initUI()
        initListeners()
        return view
    }

    private fun initComponents(view: View) {
        lvEquipos = view.findViewById(R.id.lvEquipos)
        fabCrearEquipo = view.findViewById(R.id.fabCrearEquipo)
    }
    private fun initUI() {
        val equipos = equipoServices.getEquipos()
        lvEquipos.adapter = EquipoAdapter(requireActivity(), equipos, object : EquipoAdapter.OnButtonClickListener {
            override fun onModificarClick(equipo: Equipo) {
                findNavController().navigate(R.id.nav_editar_equipo, Bundle().apply {
                    putInt("idEquipo", equipo.id)
                })
            }
            override fun onVerPilotosClick(equipo: Equipo) {
                findNavController().navigate(R.id.nav_pilotos, Bundle().apply {
                    putInt("idEquipo", equipo.id)
                })
            }
        })
    }
    private fun initListeners() {
        fabCrearEquipo.setOnClickListener {
            findNavController().navigate(R.id.nav_crear_equipo)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EquiposFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EquiposFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}