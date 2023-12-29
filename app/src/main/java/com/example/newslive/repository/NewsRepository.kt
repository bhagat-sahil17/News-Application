package com.example.newslive.repository

import com.example.newslive.api.RetrofitInstance
import com.example.newslive.database.ArticleDao
import com.example.newslive.database.ArticleDataBase
import com.example.newslive.models.Article

class NewsRepository( val database : ArticleDataBase ) {


    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =

        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)


    suspend fun searchBreakingNews( query: String, pageNumber: Int )=
        RetrofitInstance.api.searchBreakingNews(query,pageNumber)

    suspend fun updateOrinsert( article : Article ) = database.getArticleDao().upsert(article)

    fun getSavedNews() = database.getArticleDao().getAllArticles()

    suspend fun deleteArticle( article : Article ) = database.getArticleDao().deleteArticle(article)


}