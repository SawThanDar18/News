package com.sawthandar.newsapp.framework.network.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sawthandar.newsapp.framework.vos.NewsListVO
import io.reactivex.Completable

@Dao
interface NewsListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewsData(newsListVO: NewsListVO): Completable

    @Query("SELECT * from news_list")
    fun getNewsData(): LiveData<List<NewsListVO>>

}