package com.example.restjsonkotilin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.restjsonkotilin.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchCurrencyData().start()
    }

    private fun fetchCurrencyData() : Thread{
        return Thread {
            val url = URL("https://open.er-api.com/v6/latest/aud")
            val connection = url.openConnection() as HttpsURLConnection

            if(connection.responseCode == 200){
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, charset("UTF-8"))
                val request = Gson().fromJson(inputStreamReader, Request::class.java)
                updateUI(request)
                inputStreamReader.close()
                inputSystem.close()
            }
            else {
                binding.BaseCurrency.text = "failed connection"
            }
        }
    }

    private fun updateUI(request: Request?) {

        runOnUiThread {
            kotlin.run {
                if (request != null) {
                    binding.LastUpdated.text = request.time_last_update_utc
                }
                if (request != null) {
                    binding.nzd.text = String.format("NZD: %.2f", request.rates.NZD)
                }
                if (request != null) {
                    binding.USD.text = String.format("USD: %.2f", request.rates.USD)
                }
                if (request != null) {
                    binding.GBP.text = String.format("GBP: %.2f", request.rates.GBP)
                }
                if (request != null) {
                    binding.EUR.text = String.format("EUR: %.2f", request.rates.EUR)
                }
            }
        }
    }
}