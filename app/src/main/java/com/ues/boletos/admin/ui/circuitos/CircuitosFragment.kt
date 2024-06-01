package com.ues.boletos.admin.ui.circuitos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ues.boletos.DBHelper
import com.ues.boletos.R
import com.ues.boletos.databinding.FragmentCircuitosBinding
import com.ues.boletos.models.Circuito
import com.ues.boletos.services.CircuitoService

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CircuitosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CircuitosFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentCircuitosBinding
    private lateinit var lvCircuitos: ListView
    private lateinit var dbHelper: DBHelper
    private lateinit var circuitoServices: CircuitoService
    private lateinit var fabCrearCircuito: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCircuitosBinding.inflate(layoutInflater)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        dbHelper = DBHelper(requireContext())
        circuitoServices = CircuitoService(dbHelper)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_circuitos, container, false)
        initComponents(view)
        initUI()
        initListeners()
        return view
    }

    private fun initComponents(view: View) {
        lvCircuitos = view.findViewById(R.id.lvCircuitos)
        fabCrearCircuito = view.findViewById(R.id.fabCrearCircuito)
    }

    private fun initUI() {
        val circuitos = circuitoServices.getCircuitos()
        lvCircuitos.adapter = CircuitoAdapter(
            requireActivity(),
            circuitos,
            object : CircuitoAdapter.OnButtonClickListener {
                override fun onModificarClick(circuito: Circuito) {
                    Toast.makeText(
                        requireContext(),
                        "Modificar circuito ${circuito.nombre}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun initListeners() {
        fabCrearCircuito.setOnClickListener {
            Toast.makeText(requireContext(), "Crear circuito", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CircuitosFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CircuitosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}