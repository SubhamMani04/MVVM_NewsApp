package com.example.bulletin.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.bulletin.R
import com.example.bulletin.data.Article
import com.example.bulletin.databinding.ItemNewsBinding


class SearchNewsPagingAdapter : PagingDataAdapter<Article, NewsPagingAdapter.NewsViewHolder>(
        NEWS_COMPARATOR) {



    class NewsViewHolder(private val binding: ItemNewsBinding):
            RecyclerView.ViewHolder(binding.root){

        fun bind(news: Article){
            binding.apply {
                Glide.with(itemView).load(news.urlToImage)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(ivNews)

                tvNewsSource.text = news.source.name
                tvNewsTitle.text = news.title
            }
        }
    }

    companion object{
        private val NEWS_COMPARATOR = object : DiffUtil.ItemCallback<Article>(){
            override fun areItemsTheSame(oldItem: Article, newItem: Article) =
                    oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: Article, newItem: Article) =
                    oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: NewsPagingAdapter.NewsViewHolder, position: Int) {
        val currentItem = getItem(position)

        if(currentItem != null)
            holder.bind(currentItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsPagingAdapter.NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsPagingAdapter.NewsViewHolder(binding)
    }
}