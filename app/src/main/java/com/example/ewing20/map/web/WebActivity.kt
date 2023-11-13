package com.example.ewing20.map.web

import android.os.Bundle
<<<<<<< HEAD
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
=======
>>>>>>> fd93b9e4d00e83e419284cf2e8d09eb02b516c55
import androidx.appcompat.app.AppCompatActivity
import com.example.ewing20.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebBinding
<<<<<<< HEAD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Blog"

        val webView = binding.webView
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()

        // Load the first URL
        webView.loadUrl("https://blog.twitter.com/en_us/topics/product/2021/introducing-birdwatch-a-community-based-approach-to-misinformation")

        // You can also add a button or any other UI element to switch between URLs
        // For example, you can add a button click listener to load the second URL
        /*binding.button.setOnClickListener {
            webView.loadUrl("https://twitter.com/BirdCentralPark")
        }*/
    }
}
=======
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
>>>>>>> fd93b9e4d00e83e419284cf2e8d09eb02b516c55
