package com.example.ewing20.map.video

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ewing20.databinding.ActivityVideoBinding
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient


class VideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoBinding
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set the title for the activity
        title = "Posts"

        // Initialize WebView
        webView = binding.webView
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()

        // Load the BirdForum website
        val birdForumUrl = "https://www.birdforum.net/"
        webView.loadUrl(birdForumUrl)
    }
}
