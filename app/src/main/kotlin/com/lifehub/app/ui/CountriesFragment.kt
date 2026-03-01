package com.lifehub.app.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lifehub.app.R
import com.lifehub.app.api.Country
import com.lifehub.app.api.RetrofitClient
import com.lifehub.app.utils.NetworkUtils
import kotlinx.coroutines.launch
import java.text.NumberFormat

class CountriesFragment : Fragment() {

    private lateinit var etSearch: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var countriesAdapter: CountriesAdapter

    private var allCountries: List<Country> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_countries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupRecyclerView()
        setupSearch()
        loadCountries()
    }

    private fun initViews(view: View) {
        etSearch = view.findViewById(R.id.etSearchCountry)
        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        tvError = view.findViewById(R.id.tvError)
    }

    private fun setupRecyclerView() {
        countriesAdapter = CountriesAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = countriesAdapter
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterCountries(s.toString())
            }
        })
    }

    private fun filterCountries(query: String) {
        val filtered = if (query.isEmpty()) {
            allCountries
        } else {
            allCountries.filter {
                it.name.common.contains(query, ignoreCase = true) ||
                it.name.official.contains(query, ignoreCase = true)
            }
        }
        countriesAdapter.updateData(filtered)
    }

    private fun loadCountries() {
        if (!NetworkUtils.isInternetAvailable(requireContext())) {
            showError("لا يوجد اتصال بالإنترنت")
            return
        }

        showLoading(true)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.countriesApi.getAllCountries()
                showLoading(false)

                if (response.isSuccessful && response.body() != null) {
                    allCountries = response.body()!!.sortedBy { it.name.common }
                    countriesAdapter.updateData(allCountries)
                    tvError.visibility = View.GONE
                } else {
                    showError("فشل تحميل البيانات")
                }
            } catch (e: Exception) {
                showLoading(false)
                showError("حدث خطأ: ${e.message}")
            }
        }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        tvError.visibility = View.VISIBLE
        tvError.text = message
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    // Adapter
    inner class CountriesAdapter(
        private var countries: List<Country>
    ) : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

        inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imgFlag: android.widget.ImageView = itemView.findViewById(R.id.imgFlag)
            val tvCountryName: TextView = itemView.findViewById(R.id.tvCountryName)
            val tvCapital: TextView = itemView.findViewById(R.id.tvCapital)
            val tvPopulation: TextView = itemView.findViewById(R.id.tvPopulation)
            val tvRegion: TextView = itemView.findViewById(R.id.tvRegion)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_country, parent, false)
            return CountryViewHolder(view)
        }

        override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
            val country = countries[position]
            
            holder.tvCountryName.text = country.name.common
            holder.tvCapital.text = "العاصمة: ${country.capital?.firstOrNull() ?: "غير معروفة"}"
            holder.tvRegion.text = country.region
            
            val formatter = NumberFormat.getInstance()
            holder.tvPopulation.text = "السكان: ${formatter.format(country.population)}"

            Glide.with(holder.itemView.context)
                .load(country.flags.png)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.imgFlag)
        }

        override fun getItemCount() = countries.size

        fun updateData(newList: List<Country>) {
            countries = newList
            notifyDataSetChanged()
        }
    }
}
