package com.lifehub.app.ui

import android.content.Intent
import android.net.Uri
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.lifehub.app.R
import com.lifehub.app.api.Article
import com.lifehub.app.api.RetrofitClient
import com.lifehub.app.utils.NetworkUtils
import kotlinx.coroutines.launch

class NewsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupRecyclerView()
        loadNews()
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        progressBar = view.findViewById(R.id.progressBar)
        tvError = view.findViewById(R.id.tvError)

        swipeRefresh.setOnRefreshListener {
            loadNews()
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(emptyList()) { article ->
            openArticle(article.url)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = newsAdapter
    }

    private fun loadNews() {
        if (!NetworkUtils.isInternetAvailable(requireContext())) {
            showError("لا يوجد اتصال بالإنترنت")
            return
        }

        showLoading(true)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.newsApi.getNews()
                showLoading(false)
                swipeRefresh.isRefreshing = false

                if (response.isSuccessful && response.body() != null) {
                    val articles = response.body()!!.articles
                    if (articles.isNotEmpty()) {
                        newsAdapter.updateData(articles)
                        tvError.visibility = View.GONE
                    } else {
                        showError("لا توجد أخبار متاحة")
                    }
                } else {
                    showError("فشل تحميل الأخبار")
                }
            } catch (e: Exception) {
                showLoading(false)
                swipeRefresh.isRefreshing = false
                showError("حدث خطأ: ${e.message}")
            }
        }
    }

    private fun openArticle(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
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
    inner class NewsAdapter(
        private var articles: List<Article>,
        private val onClick: (Article) -> Unit
    ) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

        inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imgNews: android.widget.ImageView = itemView.findViewById(R.id.imgNews)
            val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
            val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
            val tvReadMore: TextView = itemView.findViewById(R.id.tvReadMore)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_news, parent, false)
            return NewsViewHolder(view)
        }

        override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
            val article = articles[position]
            
            holder.tvTitle.text = article.title
            holder.tvDescription.text = article.description ?: "لا يوجد وصف"
            
            if (!article.urlToImage.isNullOrEmpty()) {
                Glide.with(holder.itemView.context)
                    .load(article.urlToImage)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.imgNews)
            }

            holder.tvReadMore.setOnClickListener {
                onClick(article)
            }
        }

        override fun getItemCount() = articles.size

        fun updateData(newArticles: List<Article>) {
            articles = newArticles
            notifyDataSetChanged()
        }
    }
}
