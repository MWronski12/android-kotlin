package com.example.disago

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.beust.klaxon.Klaxon
import com.example.disago.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL


class MainActivity : AppCompatActivity() {

    private var client: OkHttpClient = OkHttpClient()
    private lateinit var binding: ActivityMainBinding
    private val apiKey = "AIzaSyCVJTR0-GEFLqSvuZCQP5X8lAe2b0J4GnQ"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.requestRideBtn.setOnClickListener {
            if (binding.originAddressInput.text.isEmpty() || binding.destinationAddressInput.text.isEmpty()) {
                Toast.makeText(this@MainActivity, "Enter origin and destination addresses!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val origin = binding.originAddressInput.text.toString()
            val destination = binding.destinationAddressInput.text.toString()
            val response: String? = fetch("https://maps.googleapis.com/maps/api/directions/json?origin=${origin}&destination=${destination}&key=${apiKey}")
            binding.priceValue.text = response
        }
    }

    private fun getRequest(sUrl: String): String? {
        var result: String? = null

        try {
            val url = URL(sUrl)
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            result = response.body?.string()
        } catch(error: Error) {
            Log.d("Request Error", error.localizedMessage)
        }

        return result
    }

    private fun fetch(sUrl: String): String? {
        var time: String? = null
        lifecycleScope.launch(Dispatchers.IO) {
            val result = getRequest(sUrl)
            if (result != null) {
                try {
                    time = Klaxon().parse(result)
                } catch (error: Error) {
                    Log.d("Request Error", error.localizedMessage)
                }
            } else {
                Log.d("Parsing error", "Errrororoororororor")
            }
        }
        Log.d("chuj", time.toString())
        return time
    }
}