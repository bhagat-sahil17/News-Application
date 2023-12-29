package com.example.newslive.ui.activity

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newslive.R
import com.example.newslive.adapters.MyAdapter
import com.example.newslive.database.ArticleDataBase
import com.example.newslive.models.Article
import com.example.newslive.repository.NewsRepository
import com.example.newslive.ui.NewsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity2 : AppCompatActivity() {

    private lateinit var viewModel: NewsViewModel


    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val articleDataBase = ArticleDataBase.invoke(this)
        val newsRepository = NewsRepository(articleDataBase)
        viewModel = NewsViewModel(newsRepository)



        val receivedArticle = intent?.getParcelableExtra<Article>("article")
        val webView = findViewById<WebView>(R.id.wb)

        webView.webViewClient = WebViewClient()
        webView.apply {
            settings.javaScriptEnabled = true
            settings.safeBrowsingEnabled = true
            if (receivedArticle != null )
            {
                loadUrl(receivedArticle.url!!)
            }

        }

        val btnsave = findViewById<FloatingActionButton>(R.id.FLOATINGbtnSAVE)


        btnsave.setOnClickListener {
                viewModel.saveArticle(receivedArticle!!)
                Snackbar.make(btnsave,"News Saved Successfully",Snackbar.LENGTH_SHORT).show()
        }
      }
}
