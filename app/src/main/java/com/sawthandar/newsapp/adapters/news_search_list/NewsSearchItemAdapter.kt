package com.sawthandar.newsapp.adapters.news_search_list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sawthandar.newsapp.adapters.BaseRecyclerAdapter
import com.sawthandar.newsapp.databinding.NewsListItemViewBinding
import com.sawthandar.newsapp.framework.vos.ArticlesListVO

class NewsSearchItemAdapter(var delegate: Delegate):
BaseRecyclerAdapter<NewsSearchItemViewHolder, ArticlesListVO>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsSearchItemViewHolder {
        return NewsSearchItemViewHolder(
            NewsListItemViewBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ),
            delegate
        )
    }

    interface Delegate{
        fun onTapNewsSearchItem(article: ArticlesListVO)
    }

}