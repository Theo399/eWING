package com.example.ewing20.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ewing20.authentication.authenticationDBHelper.DBHelper
import com.example.ewing20.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbHelper: DBHelper

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

         dbHelper = DBHelper(this)

        binding.registerButton.setOnClickListener {
            val registerUsername = binding.username.text.toString()
            val registerEmail = binding.email.text.toString()
            val registerPassword = binding.password.text.toString()
            register(registerUsername, registerEmail, registerPassword)
        }

        binding.haveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun register(username: String, email: String, password: String) {
        // trim() to remove leading/trailing white spaces
        val getUsername = binding.username.text.toString().trim()
        val getEmail = binding.email.text.toString().trim()
        val getPassword = binding.password.text.toString().trim()
        val getConfirmPassword = binding.confirmPassword.text.toString().trim()

        if (getUsername.isEmpty() || getEmail.isEmpty() || getPassword.isEmpty() || getConfirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (getPassword != getConfirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val insertRowId = dbHelper.insertUser(username, email, password)
        if (insertRowId != -1L) {
            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT)
                .show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Registered Unsuccessfully", Toast.LENGTH_SHORT)
                .show()
        }
    }
}