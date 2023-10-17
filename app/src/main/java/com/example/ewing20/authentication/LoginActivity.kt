package com.example.ewing20.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.ewing20.databinding.ActivityLoginBinding
import com.example.ewing20.authentication.authenticationDBHelper.DBHelper
import com.example.ewing20.map.MapsActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        Thread.sleep(3000)
        installSplashScreen()
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        binding.loginButton.setOnClickListener {
            val loginEmail = binding.email.text.toString()
            val loginPassword = binding.password.text.toString()
            login(loginEmail, loginPassword)
        }

        binding.noAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun login(email: String, password: String) {
        // trim() to remove leading/trailing white spaces
        val getEmail = binding.email.text.toString().trim()
        val getPassword = binding.password.text.toString().trim()

        if (getEmail.isEmpty() || getPassword.isEmpty()) {
            Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val userExists = dbHelper.readUser(email, password)
        if (userExists) {
            Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT)
                .show()
            startActivity(Intent(this, MapsActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Login Unsuccessfully", Toast.LENGTH_SHORT)
                .show()
        }
    }
}