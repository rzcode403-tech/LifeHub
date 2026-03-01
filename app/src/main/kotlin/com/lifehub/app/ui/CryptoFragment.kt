package com.lifehub.app.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lifehub.app.R
import com.lifehub.app.api.CryptoResponse
import com.lifehub.app.api.RetrofitClient
import com.lifehub.app.utils.NetworkUtils
import kotlinx.coroutines.launch

class CryptoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var cryptoAdapter: CryptoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crypto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupRecyclerView()
        loadCrypto()
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        tvError = view.findViewById(R.id.tvError)
    }

    private fun setupRecyclerView() {
        cryptoAdapter = CryptoAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = cryptoAdapter
    }

    private fun loadCrypto() {
        if (!NetworkUtils.isInternetAvailable(requireContext())) {
            showError("لا يوجد اتصال بالإنترنت")
            return
        }

        showLoading(true)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.cryptoApi.getCrypto()
                showLoading(false)

                if (response.isSuccessful && response.body() != null) {
                    val cryptoList = response.body()!!
                    if (cryptoList.isNotEmpty()) {
                        cryptoAdapter.updateData(cryptoList)
                        tvError.visibility = View.GONE
                    } else {
                        showError("لا توجد بيانات متاحة")
                    }
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
    inner class CryptoAdapter(
        private var cryptoList: List<CryptoResponse>
    ) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

        inner class CryptoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imgCrypto: android.widget.ImageView = itemView.findViewById(R.id.imgCrypto)
            val tvCryptoName: TextView = itemView.findViewById(R.id.tvCryptoName)
            val tvCryptoPrice: TextView = itemView.findViewById(R.id.tvCryptoPrice)
            val tvChange: TextView = itemView.findViewById(R.id.tvChange)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_crypto, parent, false)
            return CryptoViewHolder(view)
        }

        override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
            val crypto = cryptoList[position]
            
            holder.tvCryptoName.text = crypto.name
            holder.tvCryptoPrice.text = String.format("$%.2f", crypto.current_price)
            
            val change = crypto.price_change_percentage_24h
            holder.tvChange.text = String.format("%.2f%%", change)
            
            if (change >= 0) {
                holder.tvChange.setTextColor(Color.GREEN)
            } else {
                holder.tvChange.setTextColor(Color.RED)
            }

            Glide.with(holder.itemView.context)
                .load(crypto.image)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.imgCrypto)
        }

        override fun getItemCount() = cryptoList.size

        fun updateData(newList: List<CryptoResponse>) {
            cryptoList = newList
            notifyDataSetChanged()
        }
    }
}
