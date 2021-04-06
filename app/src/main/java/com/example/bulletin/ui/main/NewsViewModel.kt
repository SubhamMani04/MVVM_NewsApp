package com.example.bulletin.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.bulletin.data.Article
import com.example.bulletin.data.NewsRepository

class NewsViewModel @ViewModelInject constructor(newsRepository: NewsRepository) :
    ViewModel() {

        companion object{
                private const val DEFAULT_QUERY = ""
        }

        val topHeadlinesList : LiveData<PagingData<Article>> = newsRepository.getTopHeadlines().cachedIn(viewModelScope)

        private val currentQuery = MutableLiveData(DEFAULT_QUERY)
        val searchedResults : LiveData<PagingData<Article>> = currentQuery.switchMap {queryString ->
                newsRepository.searchNews(queryString).cachedIn(viewModelScope)
        }

        fun searchNews(query:String){
                currentQuery.value = query
        }
}