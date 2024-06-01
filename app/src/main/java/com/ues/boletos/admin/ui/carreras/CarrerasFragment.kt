package com.ues.boletos.admin.ui.carreras

import CarreraAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.ues.boletos.DBHelper
import com.ues.boletos.R
import com.ues.boletos.databinding.FragmentCarrerasBinding
import com.ues.boletos.models.Carrera
import com.ues.boletos.services.CarreraService
import com.ues.boletos.services.CircuitoService

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CarrerasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CarrerasFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var lvCarreras: ListView
    private lateinit var binding: FragmentCarrerasBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var carreraService: CarreraService
    private lateinit var fabCrear: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCarrerasBinding.inflate(layoutInflater)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        dbHelper = DBHelper(requireContext())
        carreraService = CarreraService(dbHelper)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_carreras, container, false)
        initComponents(view)
        initUI()
        initListeners()
        return view
    }

    fun initComponents(view: View) {
        lvCarreras = view.findViewById(R.id.lvCarreras)
        fabCrear = view.findViewById(R.id.fabCrearCarrera)
    }

    fun initUI() {
        // Crear una lista de carreras de ejemplo
        val carreras = carreraService.getCarrerasWithCircuito()
        lvCarreras.adapter = CarreraAdapter(
            requireActivity(),
            carreras,
            object : CarreraAdapter.OnButtonClickListener {
                override fun onModificarClick(carrera: Carrera) {
                    findNavController().navigate(R.id.nav_editar_carrera, Bundle().apply {
                        putInt("idCarrera", carrera.id)
                    })
                }
            })
    }

    fun initListeners() {
        fabCrear.setOnClickListener {
            findNavController().navigate(R.id.nav_crear_carrera)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CarrerasFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CarrerasFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
