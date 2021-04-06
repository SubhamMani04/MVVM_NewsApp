package com.example.bulletin.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.bulletin.Constants
import com.example.bulletin.retrofit.NewsInterface
import retrofit2.HttpException
import java.io.IOException

class SearchNewsPagingSource(private val newsInterface: NewsInterface, private val query : String) : PagingSource<Int, Article>() {

    companion object{
        private const val STARTING_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key?: STARTING_PAGE_INDEX

        return try {
            val data = newsInterface.searchNews(query,Constants.API_KEY,position,params.loadSize)

            LoadResult.Page(
                    data = data.articles,
                    prevKey = if(position == STARTING_PAGE_INDEX) null else position-1,
                    nextKey = if(data.articles.isEmpty()) null else position+1
            )
        }
        catch (e: IOException){
            LoadResult.Error(e)
        }
        catch (e: HttpException){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        TODO("Not yet implemented")
    }
}