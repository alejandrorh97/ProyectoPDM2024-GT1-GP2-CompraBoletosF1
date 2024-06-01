package com.ues.boletos.admin.ui.carreras

import CarreraAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ues.boletos.R
import com.ues.boletos.databinding.FragmentCarrerasBinding
import com.ues.boletos.models.CarreraItem

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCarrerasBinding.inflate(layoutInflater)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_carreras, container, false)
        lvCarreras = rootView.findViewById(R.id.lvCarreras)
        initUI()
        return rootView
    }

    fun initUI() {
        // Crear una lista de carreras de ejemplo
        val carreras = ArrayList<CarreraItem>()
        carreras.add(
            CarreraItem(
                1,
                "Carrera 1",
                "Circuito 1",
                "Ubicacion 1",
                "Longitud 1",
                "Curvas 1",
                "Fecha 1",
            )
        )
        carreras.add(
            CarreraItem(
                2,
                "Carrera 2",
                "Circuito 2",
                "Ubicacion 2",
                "Longitud 2",
                "Curvas 2",
                "Fecha 2",
            )
        )
        carreras.add(
            CarreraItem(
                3,
                "Carrera 3",
                "Circuito 3",
                "Ubicacion 3",
                "Longitud 3",
                "Curvas 3",
                "Fecha 3",
            )
        )
        carreras.add(
            CarreraItem(
                4,
                "Carrera 4",
                "Circuito 4",
                "Ubicacion 4",
                "Longitud 4",
                "Curvas 4",
                "Fecha 4",
            )
        )

        lvCarreras.adapter = CarreraAdapter(
            requireActivity(),
            carreras,
            object : CarreraAdapter.OnButtonClickListener {
                override fun onModificarClick(carrera: CarreraItem) {
                    onSelectCarrera(carrera)
                }
            })
    }

    fun onSelectCarrera(carrera: CarreraItem) {
        Toast.makeText(
            requireActivity(),
            "Carrera seleccionada: ${carrera.nombreCircuito}",
            Toast.LENGTH_SHORT
        ).show()
        findNavController().navigate(R.id.nav_editar_carrera, Bundle().apply {
            putInt("idCarrera", carrera.idCarrera)
        })
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
