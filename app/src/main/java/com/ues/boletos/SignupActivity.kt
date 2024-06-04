package com.ues.boletos

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ues.boletos.api.ApiClient
import com.ues.boletos.api.LoginApi
import com.ues.boletos.api.TokenManager
import com.ues.boletos.api.requests.LoginRequest
import com.ues.boletos.api.requests.RegistrarRequest
import com.ues.boletos.models.NewUser
import com.ues.boletos.services.UserService
import java.util.Calendar

class SignupActivity : AppCompatActivity() {
    private lateinit var bRegistrarse: Button
    private lateinit var etEmail: EditText
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etTelefono: EditText
    private lateinit var bFechaNacimiento: Button
    private var fechaNacimiento: String? = null
    private lateinit var spSexo: Spinner
    private lateinit var etPassword: EditText
    private lateinit var etRePassword: EditText
    private lateinit var userService: UserService
    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        dbHelper = DBHelper(this)
        userService = UserService(dbHelper)
        initComponents()
        initUI()
        initListener()
    }

    private fun initComponents() {
        bRegistrarse = findViewById(R.id.bRegistrarse)
        etEmail = findViewById(R.id.etEmail)
        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etTelefono = findViewById(R.id.etTelefono)
        bFechaNacimiento = findViewById(R.id.bFechaNacimiento)
        spSexo = findViewById(R.id.spSexo)
        etPassword = findViewById(R.id.etPassword)
        etRePassword = findViewById(R.id.etRePassword)
    }

    private fun initUI() {
        val sexos = arrayOf("Hombre", "Mujer")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sexos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spSexo.adapter = adapter
    }

    private fun initListener() {
        bRegistrarse.setOnClickListener {
            registerUser()
        }
        bFechaNacimiento.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialog(
            this,
            { DatePicker, year: Int, month: Int, day: Int ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, day)

                val currentCalendar = Calendar.getInstance()

                // Comprueba si la fecha seleccionada es mayor que la fecha actual
                if (selectedCalendar.after(currentCalendar)) {
                    Toast.makeText(
                        this,
                        "La fecha de nacimiento no puede ser futura",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@DatePickerDialog
                }

                // Comprueba si el usuario es mayor de 18 años
                currentCalendar.add(Calendar.YEAR, -18)
                if (selectedCalendar.after(currentCalendar)) {
                    Toast.makeText(this, "Debes ser mayor de 18 años", Toast.LENGTH_SHORT).show()
                    return@DatePickerDialog
                }

                fechaNacimiento = "$year/${month + 1}/$day"
                bFechaNacimiento.text = fechaNacimiento
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun registerUser() {
        val email = etEmail.text.toString()
        val nombre = etNombre.text.toString()
        val apellido = etApellido.text.toString()
        val telefono = etTelefono.text.toString()
        val sexo = spSexo.selectedItem.toString()
        val password = etPassword.text.toString()
        val rePassword = etRePassword.text.toString()

        if (email.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || password.isEmpty() || rePassword.isEmpty() || fechaNacimiento == null) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != rePassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            return
        }

        val tokenManager = TokenManager(this)
        val apiClient = ApiClient(tokenManager)
        val registrarRequest: RegistrarRequest = RegistrarRequest(
            nombre,
            apellido,
            email,
            password,
            fechaNacimiento.toString(),
            telefono,
            this.getGenero(sexo)
        )
        val call = apiClient.createService<LoginApi>().registrar(registrarRequest)

        call.enqueue(object : retrofit2.Callback<com.ues.boletos.api.responses.LoginResponse> {
            override fun onResponse(
                call: retrofit2.Call<com.ues.boletos.api.responses.LoginResponse>,
                response: retrofit2.Response<com.ues.boletos.api.responses.LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        Toast.makeText(this@SignupActivity, "Cuenta creada!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@SignupActivity, "Error al registrar", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                call: retrofit2.Call<com.ues.boletos.api.responses.LoginResponse>,
                t: Throwable
            ) {
                Toast.makeText(this@SignupActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getGenero(genero: String): String {
        if (genero == "Hombre") {
            return "hombre"
        }
        if (genero == "Mujer") {
            return "mujer"
        }
        return "otro"
    }
}