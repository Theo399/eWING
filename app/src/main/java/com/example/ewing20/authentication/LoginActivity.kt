package com.example.ewing20.authentication

import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ewing20.databinding.ActivityLoginBinding
import com.example.ewing20.map.MapsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        Thread.sleep(3000)
        installSplashScreen()
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

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
        val getEmail = email.trim()
        val getPassword = password.trim()

        if (getEmail.isEmpty() || getPassword.isEmpty()) {
            Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(getEmail, getPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MapsActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthInvalidUserException) {
                        // Handle invalid user (not registered) exception
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        // Handle other exceptions
                        Toast.makeText(this, "Login Unsuccessfully", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}
