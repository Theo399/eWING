package com.example.ewing20.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.ewing20.map.MapsActivity
import com.example.ewing20.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        Thread.sleep(3000)
        installSplashScreen()
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

        binding.noAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}