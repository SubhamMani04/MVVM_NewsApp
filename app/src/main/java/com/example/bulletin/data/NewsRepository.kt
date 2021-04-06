package com.example.bulletin.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.bulletin.retrofit.NewsInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(private val newsInterface: NewsInterface) {

    fun getTopHeadlines() : LiveData<PagingData<Article>> = Pager(
        config = PagingConfig(
            pageSize = 5,
            maxSize = 80,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            TopHeadlinesPagingSource(newsInterface)
        }
    ).liveData

    fun searchNews(query:String): LiveData<PagingData<Article>> = Pager(
            config = PagingConfig(
                    pageSize = 5,
                    maxSize = 80,
                    enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchNewsPagingSource(newsInterface,query)
            }
    ).liveData
}