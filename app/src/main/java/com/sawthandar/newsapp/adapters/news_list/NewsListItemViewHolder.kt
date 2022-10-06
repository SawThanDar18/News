package com.sawthandar.newsapp.adapters.news_list

import com.bumptech.glide.Glide
import com.sawthandar.newsapp.R
import com.sawthandar.newsapp.adapters.BaseRecyclerViewHolder
import com.sawthandar.newsapp.databinding.NewsListItemViewBinding
import com.sawthandar.newsapp.framework.vos.ArticlesListVO

class NewsListItemViewHolder(
    private val binding: NewsListItemViewBinding,
    private val delegate: NewsListItemAdapter.Delegate
): BaseRecyclerViewHolder<ArticlesListVO>(binding.root) {
    init {
        itemView.setOnClickListener {
            data?.let {
                articlesListVO -> delegate.onTapNewsItem(articlesListVO)
            }
        }
    }

    override fun bindData(datas: ArticlesListVO) {
        data = datas

        Glide.with(itemView.context)
            .load(datas.urlToImage)
            .placeholder(R.drawable.news_logo)
            .into(binding.newsImage)

        binding.newsSource.text = datas.source?.name ?: ""
        binding.newsTitle.text = datas.title ?: ""

    }
}