package com.lifehub.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.lifehub.app.R
import com.lifehub.app.api.RetrofitClient
import com.lifehub.app.utils.NetworkUtils
import kotlinx.coroutines.launch

class CurrencyFragment : Fragment() {

    private lateinit var etAmount: EditText
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var btnConvert: Button
    private lateinit var tvResult: TextView
    private lateinit var progressBar: ProgressBar

    private val currencies = listOf(
        "USD" to "دولار أمريكي",
        "EUR" to "يورو",
        "GBP" to "جنيه إسترليني",
        "JPY" to "ين ياباني",
        "SAR" to "ريال سعودي",
        "AED" to "درهم إماراتي",
        "EGP" to "جنيه مصري",
        "KWD" to "دينار كويتي",
        "QAR" to "ريال قطري",
        "BHD" to "دينار بحريني",
        "OMR" to "ريال عماني",
        "JOD" to "دينار أردني"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupSpinners()
        setupListeners()
    }

    private fun initViews(view: View) {
        etAmount = view.findViewById(R.id.etAmount)
        spinnerFrom = view.findViewById(R.id.spinnerFrom)
        spinnerTo = view.findViewById(R.id.spinnerTo)
        btnConvert = view.findViewById(R.id.btnConvert)
        tvResult = view.findViewById(R.id.tvResult)
        progressBar = view.findViewById(R.id.progressBar)
    }

    private fun setupSpinners() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            currencies.map { "${it.first} - ${it.second}" }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter
        
        spinnerFrom.setSelection(0) // USD
        spinnerTo.setSelection(5)   // AED
    }

    private fun setupListeners() {
        btnConvert.setOnClickListener {
            convertCurrency()
        }
    }

    private fun convertCurrency() {
        val amountStr = etAmount.text.toString()
        if (amountStr.isEmpty()) {
            etAmount.error = "أدخل المبلغ"
            return
        }

        val amount = amountStr.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            etAmount.error = "مبلغ غير صالح"
            return
        }

        if (!NetworkUtils.isInternetAvailable(requireContext())) {
            Toast.makeText(requireContext(), "لا يوجد اتصال بالإنترنت", Toast.LENGTH_LONG).show()
            return
        }

        val fromCurrency = currencies[spinnerFrom.selectedItemPosition].first
        val toCurrency = currencies[spinnerTo.selectedItemPosition].first

        showLoading(true)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.currencyApi.getRates(fromCurrency)
                showLoading(false)

                if (response.isSuccessful && response.body() != null) {
                    val rates = response.body()!!.rates
                    val rate = rates[toCurrency]
                    
                    if (rate != null) {
                        val result = amount * rate
                        tvResult.visibility = View.VISIBLE
                        tvResult.text = String.format(
                            "%.2f %s = %.2f %s",
                            amount, fromCurrency, result, toCurrency
                        )
                    } else {
                        Toast.makeText(requireContext(), "العملة غير مدعومة", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "فشل التحويل", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                showLoading(false)
                Toast.makeText(requireContext(), "حدث خطأ: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        btnConvert.isEnabled = !show
    }
}
