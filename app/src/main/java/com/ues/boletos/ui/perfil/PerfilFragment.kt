package com.ues.boletos.ui.perfil

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.ues.boletos.R
import com.ues.boletos.api.ApiClient
import com.ues.boletos.api.GenericShowResponse
import com.ues.boletos.api.PerfilApi
import com.ues.boletos.api.TokenManager
import com.ues.boletos.api.requests.ActualizarPerfilRequest
import com.ues.boletos.api.responses.PerfilResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class PerfilFragment : Fragment() {

    private lateinit var tvnombre: TextView
    private lateinit var tvcorreo: TextView
    private lateinit var etNombre: TextInputEditText
    private lateinit var etApellido: TextInputEditText
    private lateinit var etCorreo: TextInputEditText
    private lateinit var etTelefono: TextInputEditText
    private lateinit var etFechaNacimiento: TextInputEditText
    private lateinit var btnGuardar: Button
    private lateinit var etGenero: AutoCompleteTextView
    private lateinit var fecha: String
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("PerfilFragment", "onCreateView")
        val view = inflater.inflate(R.layout.fragment_home_perfil, container, false)
        initComponents(view)
        initListeners()
        cargarPerfil()
        return view
    }

    private fun initComponents(view: View) {
        Log.d("PerfilFragment", "initComponents")
        this.tvnombre = view.findViewById(R.id.tvNombreUsuario)
        this.tvcorreo = view.findViewById(R.id.tvCorreoUsuario)
        this.etNombre = view.findViewById(R.id.etNombre)
        this.etApellido = view.findViewById(R.id.etApellido)
        this.etCorreo = view.findViewById(R.id.etCorreo)
        this.etTelefono = view.findViewById(R.id.etTelefono)
        this.etFechaNacimiento = view.findViewById(R.id.etFechaNacimiento)
        this.etGenero = view.findViewById(R.id.etGenero)
        this.btnGuardar = view.findViewById(R.id.btnGuardar)

        val opcionesGenero = listOf("hombre", "mujer", "Otro")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, opcionesGenero)
        etGenero.setAdapter(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun initListeners(){
        Log.d("PerfilFragment", "initListeners")
        btnGuardar.setOnClickListener { guardarPerfil() }

        etFechaNacimiento.setOnClickListener { showDatePickerDialog() }
    }

    private fun setData(perfil: PerfilResponse) {
        Log.d("PerfilFragment", "setData")
        if (perfil != null) {
            this.tvnombre.text = "${perfil.nombre} ${perfil.apellido}"
            this.tvcorreo.text = perfil.correo
            this.etNombre.setText(perfil.nombre)
            this.etApellido.setText(perfil.apellido)
            this.etCorreo.setText(perfil.correo)
            this.etTelefono.setText(perfil.telefono)
            this.etFechaNacimiento.setText(perfil.fecha_nacimiento)
        }
    }

    private fun cargarPerfil() {
        val tokenManager = TokenManager(requireContext())
        val apiClient = ApiClient(tokenManager)
        val call = apiClient.createService<PerfilApi>().getPerfil()

        call.enqueue(object : Callback<GenericShowResponse<PerfilResponse>> {
            override fun onResponse(
                call: Call<GenericShowResponse<PerfilResponse>>,
                response: Response<GenericShowResponse<PerfilResponse>>
            ) {
                if (response.isSuccessful) {
                    Log.d("PerfilFragment", "Perfil cargado")
                    val perfil = response.body()?.data
                    if (perfil != null) {
                        setData(perfil)
                        this@PerfilFragment.fecha = perfil.fecha_nacimiento
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error al cargar perfil",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<GenericShowResponse<PerfilResponse>>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Error al cargar perfil",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialog(
            requireContext(),
            { view, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                fecha = "$dayOfMonth-${month + 1}-$year"
                etFechaNacimiento.setText(fecha)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun guardarPerfil() {
        val tokenManager = TokenManager(requireContext())
        val apiClient = ApiClient(tokenManager)
        val perfilRequest = ActualizarPerfilRequest(
            nombre = etNombre.text.toString(),
            apellido = etApellido.text.toString(),
            email = etCorreo.text.toString(),
            telefono = etTelefono.text.toString(),
            fecha_nacimiento = fecha,
            genero = etGenero.text.toString()
        )
        val call = apiClient.createService<PerfilApi>().updatePerfil(perfilRequest)

        call.enqueue(object : Callback<GenericShowResponse<PerfilResponse>> {
            override fun onResponse(
                call: Call<GenericShowResponse<PerfilResponse>>,
                response: Response<GenericShowResponse<PerfilResponse>>
            ) {
                if (response.isSuccessful) {
                    Log.d("PerfilFragment", "Perfil actualizado")
                    val perfil = response.body()?.data
                    if (perfil != null) {
                        Toast.makeText(
                            requireContext(),
                            "Perfil actualizado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error al actualizar perfil",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<GenericShowResponse<PerfilResponse>>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Error al actualizar perfil",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}