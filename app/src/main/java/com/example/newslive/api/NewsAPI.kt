package com.example.newslive.api

import com.example.newslive.constants.constants.Companion.API_KEY
import com.example.newslive.models.NewsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    //https://gnews.io/api/v4/top-headlines?
    // category=general&
    // lang=en&
    // country=us&
    // max=10&
    // apikey=5d2c31c3a46b3c6ace50ab8ee6f7d60f


    @GET("v2/top-headlines")
    suspend fun getBreakingNews(

//        @Query("category")
//        category : String = "general",
//        @Query("lang")
//        lang : String = "en",
        @Query("country")
        countryCode : String = "in",
        @Query("page")
        pagenumber : Int = 1,
        @Query("apiKey")
        apiKey : String = API_KEY

    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchBreakingNews(


        @Query("q")
        countryCode : String ,
        @Query("pagenumber")
        pagenumber : Int,
        @Query("apiKey")
        apiKey : String = API_KEY

    ): Response<NewsResponse>
}