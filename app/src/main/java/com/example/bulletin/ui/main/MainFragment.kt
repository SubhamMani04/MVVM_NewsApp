package com.example.bulletin.ui.main

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.paging.LoadState
import com.example.bulletin.R
import com.example.bulletin.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*

@AndroidEntryPoint
class MainFragment: Fragment(R.layout.fragment_main) {

    private val newsViewModel by viewModels<NewsViewModel>()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsPagingAdapter : NewsPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentMainBinding.bind(view)

        newsPagingAdapter = NewsPagingAdapter()

        iv_search_news.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_searchNewsFragment)
        }

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = newsPagingAdapter.withLoadStateHeaderAndFooter(
                    header = NewsLoadStateAdapter { newsPagingAdapter.retry() },
                    footer = NewsLoadStateAdapter { newsPagingAdapter.retry() }
            )

            btn_retry.setOnClickListener {
                newsPagingAdapter.retry()
            }
        }

        newsViewModel.topHeadlinesList.observe(viewLifecycleOwner) {
            newsPagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        newsPagingAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                btn_retry.isVisible = loadState.source.refresh is LoadState.Error
                tv_error.isVisible = loadState.source.refresh is LoadState.Error

                //Empty view
                if(loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        newsPagingAdapter.itemCount < 1){
                    recyclerView.isVisible = false
                    tv_empty.isVisible = true
                }
                else{
                    tv_empty.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}