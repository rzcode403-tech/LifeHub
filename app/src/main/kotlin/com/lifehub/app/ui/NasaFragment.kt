package com.lifehub.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.lifehub.app.R
import com.lifehub.app.api.RetrofitClient
import com.lifehub.app.utils.NetworkUtils
import kotlinx.coroutines.launch

class NasaFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var cardNasa: CardView
    private lateinit var imgApod: ImageView
    private lateinit var tvApodTitle: TextView
    private lateinit var tvApodDate: TextView
    private lateinit var tvApodExplanation: TextView
    private lateinit var tvError: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nasa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        loadApod()
    }

    private fun initViews(view: View) {
        progressBar = view.findViewById(R.id.progressBar)
        cardNasa = view.findViewById(R.id.cardNasa)
        imgApod = view.findViewById(R.id.imgApod)
        tvApodTitle = view.findViewById(R.id.tvApodTitle)
        tvApodDate = view.findViewById(R.id.tvApodDate)
        tvApodExplanation = view.findViewById(R.id.tvApodExplanation)
        tvError = view.findViewById(R.id.tvError)
    }

    private fun loadApod() {
        if (!NetworkUtils.isInternetAvailable(requireContext())) {
            showError("لا يوجد اتصال بالإنترنت")
            return
        }

        showLoading(true)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.nasaApi.getApod()
                showLoading(false)

                if (response.isSuccessful && response.body() != null) {
                    val apod = response.body()!!
                    displayApod(apod)
                } else {
                    showError("فشل تحميل الصورة")
                }
            } catch (e: Exception) {
                showLoading(false)
                showError("حدث خطأ: ${e.message}")
            }
        }
    }

    private fun displayApod(apod: com.lifehub.app.api.NasaResponse) {
        cardNasa.visibility = View.VISIBLE
        tvError.visibility = View.GONE

        tvApodTitle.text = apod.title
        tvApodDate.text = apod.date
        tvApodExplanation.text = apod.explanation

        if (apod.media_type == "image" && apod.url.isNotEmpty()) {
            Glide.with(requireContext())
                .load(apod.url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imgApod)
        }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        cardNasa.visibility = View.GONE
        tvError.visibility = View.VISIBLE
        tvError.text = message
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}
