
package com.example.ewing20.map.profile

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ewing20.R
import com.example.ewing20.authentication.LoginActivity
import com.example.ewing20.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser

    private lateinit var email: TextView
    private lateinit var password: TextView
    private lateinit var username: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)


        profile()

        binding.logoutBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.changePasswordBtn.setOnClickListener {
            val newPassword = binding.newPasswordEditText.text.toString()
            changePassword(newPassword)
        }


        // Remove the Add Picture Button and related functionality
        /*
        binding.addPictureBtn.setOnClickListener {
            // Call a function to add a profile picture
            // AddProfilePictureFunction()
        }
        */
    }

    private fun profile() {

        email.text = firebaseUser.email
        password.text = "********" // You may choose to display or hide the password here
    }

    private fun changePassword(newPassword: String) {
        firebaseUser.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to change password", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
