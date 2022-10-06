package com.sawthandar.newsapp.ui.newsList

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sawthandar.newsapp.adapters.news_list.NewsListItemAdapter
import com.sawthandar.newsapp.framework.vos.ArticlesListVO
import com.sawthandar.newsapp.framework.vos.NewsListVO
import com.sawthandar.newsapp.repository.NewsRepository
import com.sawthandar.newsapp.util.BASE_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(private val newsRepository: NewsRepository): ViewModel(), NewsListItemAdapter.Delegate, LifecycleObserver {

    private val articlesListData: MutableLiveData<ArticlesListVO> = MutableLiveData()

    private var totalResult: Long? = null
    private var pageSize: Int = 50
    private var totalPage: Long = 0
    private var page: Int = 1

    private var mErrorMessage: MutableLiveData<String> = MutableLiveData<String>()
    private var mErrorMessageMore: MutableLiveData<String> = MutableLiveData<String>()
    private var progressLiveData: MutableLiveData<Int> = MutableLiveData<Int>()

    fun onUIReady() {
        newsRepository.loadNewsList(
            page,
            pageSize,
            onSuccess = {
                it.totalResults?.let {
                    totalResults ->
                    totalResult = totalResults
                    totalResult?.let {
                        result ->
                        totalPage = result / pageSize.toLong()
                    }
                }
            },
            onFailure = {
                Log.d("onUIReadyError>>", it)
                mErrorMessage.postValue(it)
            }
        )
    }

    fun loadMoreNewsList() {
        if (page.toLong() < totalPage) {
            page++
            progressLiveData.postValue(1)
            newsRepository.loadMoreNewsList(
                BASE_URL,
                page,
                pageSize,
                onSuccess = {
                    progressLiveData.postValue(0)
                    Log.d("loadMoreNewsListSuccess>>", it.toString())
                },
                onFailure = {
                    progressLiveData.postValue(0)
                    mErrorMessage.postValue(it)
                    Log.d("loadMoreNewsList>>", it)
                }
            )
        }
    }

    fun getNewsListFromDatabase(): LiveData<List<NewsListVO>> {
        return newsRepository.getNewsListFromDatabase()
    }

    fun navigateSearchDataToDetails(): LiveData<ArticlesListVO> {
        return articlesListData
    }

    override fun onTapNewsItem(articlesList: ArticlesListVO) {
        articlesListData.postValue(articlesList)
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