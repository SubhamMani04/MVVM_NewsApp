package com.example.bulletin.retrofit

import com.example.bulletin.data.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsInterface {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country:String,
        @Query("apiKey") apiKey:String,
        @Query("page") page:Int,
        @Query("pageSize") pageSize:Int
    ) : NewsResponse

    @GET("everything")
    suspend fun searchNews(
        @Query("q") query:String,
        @Query("apiKey") apiKey: String,
        @Query("page") page:Int,
        @Query("pageSize") pageSize:Int
    ) : NewsResponse
}