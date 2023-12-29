package com.example.newslive.ui.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewFragment
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.newslive.R

class ArticleFragment: Fragment(R.layout.article_fragment) {

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        arguments?.let { bundle ->
//            val articleUrl = bundle.getString("article")
//            // Use the received data (articleUrl) here as needed
//            val webView: WebView = requireView().findViewById(R.id.webView)
//            articleUrl?.let {
//                webView.apply {
//                    settings.javaScriptEnabled = true
//                    settings.safeBrowsingEnabled = true
//                    loadUrl(articleUrl)
//                }
//            } ?: run {
//                // Handle invalid or missing URL
//            }
//        }

     }

}