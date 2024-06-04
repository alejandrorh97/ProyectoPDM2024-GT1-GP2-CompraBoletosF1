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
import com.ues.boletos.api.ApiClient
import com.ues.boletos.api.LoginApi
import com.ues.boletos.api.TokenManager
import com.ues.boletos.api.requests.LoginRequest
import com.ues.boletos.services.UserService

class LoginActivity : AppCompatActivity() {
    private lateinit var bLogin: Button
    private lateinit var bRegister: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var userService: UserService
    private lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        dbHelper = DBHelper(this)
        userService = UserService(dbHelper)
        initComponents()
        initListener()
    }

    private fun initComponents() {
        bLogin = findViewById(R.id.bLogin)
        bRegister = findViewById(R.id.bRegister)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
    }

    private fun initListener() {
        bLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email y contraseña son requeridos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tokenManager = TokenManager(this)
            val apiClient = ApiClient(tokenManager)
            val loginRequest: LoginRequest = LoginRequest(email, password)
            val call = apiClient.createService<LoginApi>().login(loginRequest)

            call.enqueue(object : retrofit2.Callback<com.ues.boletos.api.responses.LoginResponse> {
                override fun onResponse(
                    call: retrofit2.Call<com.ues.boletos.api.responses.LoginResponse>,
                    response: retrofit2.Response<com.ues.boletos.api.responses.LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null) {
                            tokenManager.saveToken(loginResponse.access_token)
                            val sharedPreferences = getSharedPreferences("compra-boletos-formula-1", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("isLoggedIn", true)
                            editor.putBoolean("isAdmin", loginResponse.is_admin)
                            editor.apply()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<com.ues.boletos.api.responses.LoginResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(this@LoginActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })

        }
        bRegister.setOnClickListener { startActivity(Intent(this, SignupActivity::class.java)) }
    }
}