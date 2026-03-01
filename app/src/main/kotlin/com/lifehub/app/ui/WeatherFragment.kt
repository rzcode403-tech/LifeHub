package com.lifehub.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.lifehub.app.R
import com.lifehub.app.api.RetrofitClient
import com.lifehub.app.utils.NetworkUtils
import kotlinx.coroutines.launch

class WeatherFragment : Fragment() {

    private lateinit var etCity: EditText
    private lateinit var btnSearch: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var cardWeather: CardView
    private lateinit var tvCityName: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvWindSpeed: TextView
    private lateinit var tvPressure: TextView
    private lateinit var tvError: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupListeners()
    }

    private fun initViews(view: View) {
        etCity = view.findViewById(R.id.etCity)
        btnSearch = view.findViewById(R.id.btnSearch)
        progressBar = view.findViewById(R.id.progressBar)
        cardWeather = view.findViewById(R.id.cardWeather)
        tvCityName = view.findViewById(R.id.tvCityName)
        tvTemperature = view.findViewById(R.id.tvTemperature)
        tvDescription = view.findViewById(R.id.tvDescription)
        tvHumidity = view.findViewById(R.id.tvHumidity)
        tvWindSpeed = view.findViewById(R.id.tvWindSpeed)
        tvPressure = view.findViewById(R.id.tvPressure)
        tvError = view.findViewById(R.id.tvError)
    }

    private fun setupListeners() {
        btnSearch.setOnClickListener {
            val city = etCity.text.toString().trim()
            if (city.isNotEmpty()) {
                fetchWeather(city)
            } else {
                etCity.error = "أدخل اسم المدينة"
            }
        }
    }

    private fun fetchWeather(city: String) {
        if (!NetworkUtils.isInternetAvailable(requireContext())) {
            showError("لا يوجد اتصال بالإنترنت")
            return
        }

        showLoading(true)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.weatherApi.getWeather(city)
                showLoading(false)

                if (response.isSuccessful && response.body() != null) {
                    val weather = response.body()!!
                    displayWeather(weather)
                } else {
                    showError("لم يتم العثور على المدينة")
                }
            } catch (e: Exception) {
                showLoading(false)
                showError("حدث خطأ: ${e.message}")
            }
        }
    }

    private fun displayWeather(weather: com.lifehub.app.api.WeatherResponse) {
        cardWeather.visibility = View.VISIBLE
        tvError.visibility = View.GONE

        tvCityName.text = weather.name
        tvTemperature.text = "${weather.main.temp.toInt()}°C"
        tvDescription.text = weather.weather.firstOrNull()?.description ?: ""
        tvHumidity.text = "${weather.main.humidity}%"
        tvWindSpeed.text = "${weather.wind.speed} m/s"
        tvPressure.text = "${weather.main.pressure} hPa"
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        btnSearch.isEnabled = !show
    }

    private fun showError(message: String) {
        cardWeather.visibility = View.GONE
        tvError.visibility = View.VISIBLE
        tvError.text = message
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}
