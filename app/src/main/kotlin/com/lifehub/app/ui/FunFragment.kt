package com.lifehub.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.lifehub.app.R
import com.lifehub.app.api.RetrofitClient
import com.lifehub.app.utils.NetworkUtils
import kotlinx.coroutines.launch

class FunFragment : Fragment() {

    private lateinit var tvJoke: TextView
    private lateinit var tvFact: TextView
    private lateinit var tvQuote: TextView
    private lateinit var tvQuoteAuthor: TextView
    private lateinit var btnNewJoke: Button
    private lateinit var btnNewFact: Button
    private lateinit var btnNewQuote: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fun, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupListeners()
        
        // Load initial data
        loadJoke()
        loadFact()
        loadQuote()
    }

    private fun initViews(view: View) {
        tvJoke = view.findViewById(R.id.tvJoke)
        tvFact = view.findViewById(R.id.tvFact)
        tvQuote = view.findViewById(R.id.tvQuote)
        tvQuoteAuthor = view.findViewById(R.id.tvQuoteAuthor)
        btnNewJoke = view.findViewById(R.id.btnNewJoke)
        btnNewFact = view.findViewById(R.id.btnNewFact)
        btnNewQuote = view.findViewById(R.id.btnNewQuote)
        progressBar = view.findViewById(R.id.progressBar)
    }

    private fun setupListeners() {
        btnNewJoke.setOnClickListener { loadJoke() }
        btnNewFact.setOnClickListener { loadFact() }
        btnNewQuote.setOnClickListener { loadQuote() }
    }

    private fun loadJoke() {
        if (!NetworkUtils.isInternetAvailable(requireContext())) {
            Toast.makeText(requireContext(), "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show()
            return
        }

        btnNewJoke.isEnabled = false

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.jokeApi.getRandomJoke()
                btnNewJoke.isEnabled = true

                if (response.isSuccessful && response.body() != null) {
                    val joke = response.body()!!
                    val jokeText = when (joke.type) {
                        "single" -> joke.joke ?: "لا توجد نكتة"
                        "twopart" -> "${joke.setup}\n\n${joke.delivery}"
                        else -> "لا توجد نكتة"
                    }
                    tvJoke.text = jokeText
                } else {
                    tvJoke.text = "فشل تحميل النكتة"
                }
            } catch (e: Exception) {
                btnNewJoke.isEnabled = true
                tvJoke.text = "حدث خطأ: ${e.message}"
            }
        }
    }

    private fun loadFact() {
        if (!NetworkUtils.isInternetAvailable(requireContext())) {
            Toast.makeText(requireContext(), "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show()
            return
        }

        btnNewFact.isEnabled = false

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.factApi.getRandomFact()
                btnNewFact.isEnabled = true

                if (response.isSuccessful && response.body() != null) {
                    tvFact.text = response.body()!!.text
                } else {
                    tvFact.text = "فشل تحميل الحقيقة"
                }
            } catch (e: Exception) {
                btnNewFact.isEnabled = true
                tvFact.text = "حدث خطأ: ${e.message}"
            }
        }
    }

    private fun loadQuote() {
        if (!NetworkUtils.isInternetAvailable(requireContext())) {
            Toast.makeText(requireContext(), "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show()
            return
        }

        btnNewQuote.isEnabled = false

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.quoteApi.getRandomQuote()
                btnNewQuote.isEnabled = true

                if (response.isSuccessful && response.body() != null) {
                    val quote = response.body()!!
                    tvQuote.text = "\"${quote.content}\""
                    tvQuoteAuthor.text = "- ${quote.author}"
                } else {
                    tvQuote.text = "فشل تحميل الاقتباس"
                    tvQuoteAuthor.text = ""
                }
            } catch (e: Exception) {
                btnNewQuote.isEnabled = true
                tvQuote.text = "حدث خطأ: ${e.message}"
                tvQuoteAuthor.text = ""
            }
        }
    }
}
