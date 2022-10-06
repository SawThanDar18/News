package com.sawthandar.newsapp.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sawthandar.newsapp.adapters.news_search_list.NewsSearchItemAdapter
import com.sawthandar.newsapp.framework.vos.ArticlesListVO
import com.sawthandar.newsapp.repository.NewsRepository
import com.sawthandar.newsapp.util.BASE_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsSearchListViewModel @Inject constructor(
    private val newsRepository: NewsRepository
): ViewModel(), NewsSearchItemAdapter.Delegate {

    private val newsArticlesList = ArrayList<ArticlesListVO>()
    private val articlesListLiveData = MutableLiveData<List<ArticlesListVO>>()

    private val navigateArticlesData: MutableLiveData<ArticlesListVO> = MutableLiveData()

    private var totalResult: Long? = null
    private var pageSize: Int = 50
    private var totalPage: Long = 0
    private var page: Int = 1

    private var mErrorMessage: MutableLiveData<String> = MutableLiveData<String>()
    private var mErrorMessageMore: MutableLiveData<String> = MutableLiveData<String>()
    private var progressLiveData: MutableLiveData<Int> = MutableLiveData<Int>()

    fun loadSearchNews(searchWord: String){

        newsRepository.loadSearchNewsList(
            page,
            pageSize,
            searchWord,
            onSuccess = {
                it.totalResults?.let {
                        totalResults ->
                    totalResult = totalResults
                    totalResult?.let {
                            result ->
                        totalPage = result / pageSize.toLong()
                    }
                }

                it.articles?.let { articleList->
                    newsArticlesList.clear()
                    newsArticlesList.addAll(articleList)
                    articlesListLiveData.value = newsArticlesList
                }
            },
            onFailure = {
                mErrorMessage.postValue(it)
                Log.d("loadSearchNewsError>>", it)
            }
        )
    }

    fun loadMoreSearchNews(searchWord: String){
        if (page.toLong() < totalPage) {
            page++
            progressLiveData.postValue(1)
            newsRepository.loadMoreSearchNewsList(
                BASE_URL,
                page,
                pageSize,
                searchWord,
                onSuccess = { newList ->
                    progressLiveData.postValue(0)

                    newList.articles?.let { articleList->
                        newsArticlesList.addAll(articleList)
                        articlesListLiveData.value = newsArticlesList
                    }
                },
                onFailure = {
                    progressLiveData.postValue(0)
                    mErrorMessage.postValue(it)
                    Log.d("loadMoreSearchNewsError>>", it)
                })
        }
    }

    fun getSearchNewsList(): MutableLiveData<List<ArticlesListVO>>{
        return articlesListLiveData
    }

    fun getNavigateSearchListToDetailData(): LiveData<ArticlesListVO> {
        return navigateArticlesData
    }

    override fun onTapNewsSearchItem(article: ArticlesListVO) {
        navigateArticlesData.postValue(article)
    }

    fun getErrorMessage(): MutableLiveData<String>{
        return mErrorMessage
    }

    fun getErrorMessageMore(): MutableLiveData<String>{
        return mErrorMessageMore
    }

    fun getShowOrHideProgress(): MutableLiveData<Int> {
        return progressLiveData
    }
}