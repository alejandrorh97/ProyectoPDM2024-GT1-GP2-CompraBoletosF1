package com.ues.boletos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    private lateinit var bLogin: Button
    private lateinit var bRegister: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        initListener()
    }

    private fun initComponents() {
        bLogin = findViewById(R.id.bLogin)
        bRegister = findViewById(R.id.bRegister)
    }

    private fun initListener() {
        bLogin.setOnClickListener {
            val sharedPreferences = getSharedPreferences("compra-boletos-formula-1", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", true) // Cambia esto a lo que quieras
            editor.apply()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        bRegister.setOnClickListener { startActivity(Intent(this, SignupActivity::class.java)) }
    }
}