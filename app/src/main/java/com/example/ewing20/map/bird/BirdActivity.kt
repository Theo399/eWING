package com.example.ewing20.map.bird

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ewing20.R
import com.example.ewing20.databinding.ActivityBirdBinding

class BirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBirdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBirdBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_bird)

        binding.addBirdBtn.setOnClickListener {
            startActivity(Intent(this, BirdDetailActivity::class.java))
        }
    }
}