package com.example.bulletin.ui.main

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.example.bulletin.R
import com.example.bulletin.databinding.FragmentSearchNewsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.btn_retry
import kotlinx.android.synthetic.main.fragment_main.tv_empty
import kotlinx.android.synthetic.main.fragment_main.tv_error
import kotlinx.android.synthetic.main.fragment_search_news.*

@AndroidEntryPoint
class SearchNewsFragment: Fragment(R.layout.fragment_search_news) {

    private val newsViewModel by viewModels<NewsViewModel>()
    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchNewsPagingAdapter: SearchNewsPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchNewsPagingAdapter = SearchNewsPagingAdapter()
        _binding = FragmentSearchNewsBinding.bind(view)

        binding.apply {
            recyclerViewSearch.setHasFixedSize(true)
            recyclerViewSearch.itemAnimator = null
            recyclerViewSearch.adapter = searchNewsPagingAdapter.withLoadStateHeaderAndFooter(
                    header = NewsLoadStateAdapter { searchNewsPagingAdapter.retry() },
                    footer = NewsLoadStateAdapter { searchNewsPagingAdapter.retry() }
            )

            btn_retry.setOnClickListener {
                searchNewsPagingAdapter.retry()
            }
        }

        et_search_news.setOnEditorActionListener(editorListener)

        searchNewsPagingAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerViewSearch.isVisible = loadState.source.refresh is LoadState.NotLoading
                btn_retry.isVisible = loadState.source.refresh is LoadState.Error
                tv_error.isVisible = loadState.source.refresh is LoadState.Error

                //Empty view
                if(loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        searchNewsPagingAdapter.itemCount < 1){
                    recyclerViewSearch.isVisible = false
                    tv_empty.isVisible = true
                }
                else{
                    tv_empty.isVisible = false
                }
            }
        }
    }

    private val editorListener = TextView.OnEditorActionListener { v, actionId, _ ->
        when (actionId) {
            EditorInfo.IME_ACTION_SEARCH -> {
                val query = v.editableText.toString().trim()
                if (query.isNotEmpty()) {
                    newsViewModel.searchedResults.observe(viewLifecycleOwner) {
                        searchNewsPagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                    }
                    binding.recyclerViewSearch.scrollToPosition(0)
                    newsViewModel.searchNews(query)
                }
            }
        }
        false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}