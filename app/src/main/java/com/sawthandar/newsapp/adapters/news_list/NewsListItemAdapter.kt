package com.sawthandar.newsapp.adapters.news_list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sawthandar.newsapp.adapters.BaseRecyclerAdapter
import com.sawthandar.newsapp.databinding.NewsListItemViewBinding
import com.sawthandar.newsapp.framework.vos.ArticlesListVO

class NewsListItemAdapter(private val delegate: Delegate) :
BaseRecyclerAdapter<NewsListItemViewHolder, ArticlesListVO>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListItemViewHolder {
                return NewsListItemViewHolder(
                        NewsListItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false), delegate
                )
        }

        interface Delegate {
                fun onTapNewsItem(articlesList: ArticlesListVO)
        }

}