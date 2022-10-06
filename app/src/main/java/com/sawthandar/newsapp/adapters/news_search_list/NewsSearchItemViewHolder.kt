package com.sawthandar.newsapp.adapters.news_search_list

import com.bumptech.glide.Glide
import com.sawthandar.newsapp.adapters.BaseRecyclerViewHolder
import com.sawthandar.newsapp.databinding.NewsListItemViewBinding
import com.sawthandar.newsapp.framework.vos.ArticlesListVO

class NewsSearchItemViewHolder(
    private val binding: NewsListItemViewBinding,
    private val delegate: NewsSearchItemAdapter.Delegate
) : BaseRecyclerViewHolder<ArticlesListVO>(binding.root) {

    init {
        itemView.setOnClickListener {
            data?.let { articleListVO ->
                delegate.onTapNewsSearchItem(articleListVO)
            }
        }
    }

    override fun bindData(datas: ArticlesListVO) {
        data = datas

        Glide.with(itemView.context)
            .load(datas.urlToImage)
            .circleCrop()
            .into(binding.newsImage)

        binding.newsTitle.text = datas.title ?: ""
        binding.newsSource.text = datas.source?.name ?: ""
    }
}