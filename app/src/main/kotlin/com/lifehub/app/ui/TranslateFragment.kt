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

class TranslateFragment : Fragment() {

    private lateinit var spinnerSource: Spinner
    private lateinit var spinnerTarget: Spinner
    private lateinit var etSourceText: EditText
    private lateinit var btnTranslate: Button
    private lateinit var tvTranslatedText: TextView
    private lateinit var progressBar: ProgressBar

    private val languages = listOf(
        "ar" to "العربية",
        "en" to "الإنجليزية",
        "es" to "الإسبانية",
        "fr" to "الفرنسية",
        "de" to "الألمانية",
        "it" to "الإيطالية",
        "pt" to "البرتغالية",
        "ru" to "الروسية",
        "zh" to "الصينية",
        "ja" to "اليابانية",
        "ko" to "الكورية",
        "tr" to "التركية"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_translate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupSpinners()
        setupListeners()
    }

    private fun initViews(view: View) {
        spinnerSource = view.findViewById(R.id.spinnerSource)
        spinnerTarget = view.findViewById(R.id.spinnerTarget)
        etSourceText = view.findViewById(R.id.etSourceText)
        btnTranslate = view.findViewById(R.id.btnTranslate)
        tvTranslatedText = view.findViewById(R.id.tvTranslatedText)
        progressBar = view.findViewById(R.id.progressBar)
    }

    private fun setupSpinners() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            languages.map { "${it.first} - ${it.second}" }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        
        spinnerSource.adapter = adapter
        spinnerTarget.adapter = adapter
        
        spinnerSource.setSelection(0) // Arabic
        spinnerTarget.setSelection(1) // English
    }

    private fun setupListeners() {
        btnTranslate.setOnClickListener {
            translateText()
        }
    }

    private fun translateText() {
        val text = etSourceText.text.toString().trim()
        if (text.isEmpty()) {
            etSourceText.error = "أدخل النص للترجمة"
            return
        }

        if (!NetworkUtils.isInternetAvailable(requireContext())) {
            Toast.makeText(requireContext(), "لا يوجد اتصال بالإنترنت", Toast.LENGTH_LONG).show()
            return
        }

        val sourceLang = languages[spinnerSource.selectedItemPosition].first
        val targetLang = languages[spinnerTarget.selectedItemPosition].first

        showLoading(true)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.translationApi.translate(
                    text = text,
                    source = sourceLang,
                    target = targetLang
                )
                showLoading(false)

                if (response.isSuccessful && response.body() != null) {
                    val translated = response.body()!!.translatedText
                    tvTranslatedText.visibility = View.VISIBLE
                    tvTranslatedText.text = translated
                } else {
                    Toast.makeText(requireContext(), "فشل الترجمة", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                showLoading(false)
                Toast.makeText(requireContext(), "حدث خطأ: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        btnTranslate.isEnabled = !show
    }
}
