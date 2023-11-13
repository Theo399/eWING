package com.example.ewing20.map.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ewing20.databinding.ActivityAboutBinding
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding
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
