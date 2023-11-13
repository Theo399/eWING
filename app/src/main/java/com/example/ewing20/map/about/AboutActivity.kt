package com.example.ewing20.map.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ewing20.databinding.ActivityAboutBinding
<<<<<<< HEAD
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
=======
>>>>>>> fd93b9e4d00e83e419284cf2e8d09eb02b516c55

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding
<<<<<<< HEAD
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set the title for the activity
        title = "About eWing"

        // Initialize WebView
        webView = binding.webView
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()

        // Load the video link
        val videoLink = "https://youtu.be/zTJJ3I3DNpc"
        webView.loadUrl(videoLink)
    }
}
=======
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
>>>>>>> fd93b9e4d00e83e419284cf2e8d09eb02b516c55
