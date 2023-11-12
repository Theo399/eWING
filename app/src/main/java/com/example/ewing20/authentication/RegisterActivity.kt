package com.example.ewing20.authentication
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ewing20.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener {
            val registerUsername = binding.username.text.toString()
            val registerEmail = binding.email.text.toString()
            val registerPassword = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()

            register(registerUsername, registerEmail, registerPassword, confirmPassword)
        }

        binding.haveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun register(username: String, email: String, password: String, confirmPassword: String) {
        val getEmail = email.trim()
        val getPassword = password.trim()
        val getConfirmPassword = confirmPassword.trim()

        if (username.isEmpty() || getEmail.isEmpty() || getPassword.isEmpty() || getConfirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return
        }

        if (getPassword != getConfirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(getEmail, getPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    // If registration fails, display a message to the user.
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthUserCollisionException) {
                        // Handle collision (user already exists) exception
                        Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        // Handle other exceptions
                        Toast.makeText(this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}
