package com.ues.boletos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ues.boletos.services.UserService

class SignupActivity : AppCompatActivity() {
    private lateinit var bRegistrarse: Button
    private  lateinit var etEmail: EditText
    private lateinit var etNombre: EditText
    private lateinit var etPassword: EditText
    private lateinit var etRePassword: EditText
    private lateinit var userService: UserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userService = UserService(this)
        initComponents()
        initListener()
    }

    private fun initComponents() {
        bRegistrarse = findViewById(R.id.bRegistrarse)
        etEmail = findViewById(R.id.etEmail)
        etNombre = findViewById(R.id.etNombre)
        etPassword = findViewById(R.id.etPassword)
        etRePassword = findViewById(R.id.etRePassword)
    }

    private fun initListener() {
        bRegistrarse.setOnClickListener {
            val email = etEmail.text.toString()
            val nombre = etNombre.text.toString()
            val password = etPassword.text.toString()
            val rePassword = etRePassword.text.toString()
            if(password != rePassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(password.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(userService.existeUsuario(email)) {
                Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            try {
//                userService.insertUsuario(new NewUser())
                Toast.makeText(this, "Bienvenido $nombre!", Toast.LENGTH_SHORT).show()
                val sharedPreferences = getSharedPreferences("compra-boletos-formula-1", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLoggedIn", true) // Cambia esto a lo que quieras
                editor.apply()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } catch (e: Exception) {
                Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }
}