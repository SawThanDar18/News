package com.sawthandar.newsapp.repository

import android.annotation.SuppressLint
import com.sawthandar.newsapp.framework.network.NewsAPI
import com.sawthandar.newsapp.framework.network.persistence.daos.NewsListDao
import com.sawthandar.newsapp.framework.vos.NewsListVO
import com.sawthandar.newsapp.util.API_KEY_DATA
import com.sawthandar.newsapp.util.GET_NEWS_LIST
import com.sawthandar.newsapp.util.QUERY_DATA
import com.sawthandar.newsapp.util.subscribeDBWithCompletable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val newsAPI: NewsAPI,
    private val newsListDao: NewsListDao
){
    @SuppressLint("CheckResult")
    fun loadNewsList(
        page: Int,
        pageSize: Int,
        onSuccess: (newsListVO: NewsListVO) -> Unit,
        onFailure: (String) -> Unit
    ) {
        newsAPI.loadNewsList(QUERY_DATA, API_KEY_DATA, pageSize, page).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it?.let { newsListVO ->
                    onSuccess(newsListVO)
                    newsListDao.insertNewsData(newsListVO).subscribeDBWithCompletable()
                }
            }, {
                it.message?.let { errorMessage ->
                    onFailure(errorMessage)
                }
            })
    }

    @SuppressLint("CheckResult")
    fun loadMoreNewsList(
        url: String,
        page: Int,
        pageSie: Int,
        onSuccess: (newsListVO: NewsListVO) -> Unit,
        onFailure: (String) -> Unit
    ) {
        newsAPI.loadMoreNewList(url + GET_NEWS_LIST, QUERY_DATA, API_KEY_DATA, pageSie, page)
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it?.let { newsListVO ->
                    if (newsListVO.status == "ok") {
                        onSuccess(newsListVO)
                        newsListDao.insertNewsData(newsListVO).subscribeDBWithCompletable()
                    } else {
                        onFailure("error")
                    }
                }
            }, {
                it.message?.let { errorMessage ->
                    onFailure(errorMessage)
                }
            })
    }

    @SuppressLint("CheckResult")
    fun loadSearchNewsList(
        page: Int,
        pageSie: Int,
        query: String,
        onSuccess: (newsListVO: NewsListVO) -> Unit,
        onFailure: (String) -> Unit
    ) {
        newsAPI.loadNewsList(query, API_KEY_DATA, pageSie, page).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it?.let { newsListVO ->
                    onSuccess(newsListVO)
                }
            }, {
                it.message?.let { errorMessage ->
                    onFailure(errorMessage)
                }
            })
    }

    @SuppressLint("CheckResult")
    fun loadMoreSearchNewsList(
        url: String,
        page: Int,
        pageSie: Int,
        query: String,
        onSuccess: (newsListVO: NewsListVO) -> Unit,
        onFailure: (String) -> Unit
    ) {
        newsAPI.loadMoreNewList(url + GET_NEWS_LIST, query, API_KEY_DATA, pageSie, page)
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    it?.let { newListVo ->

                        if (newListVo.status == "ok") {
                            onSuccess(newListVo)

                        } else {
                            onFailure("error")
                        }

                    }
                },
                {
                    it.message?.let { it1 ->
                        onFailure(it1)
                    }
                }
            )
    }

    fun getNewsListFromDatabase() = newsListDao.getNewsData()
}