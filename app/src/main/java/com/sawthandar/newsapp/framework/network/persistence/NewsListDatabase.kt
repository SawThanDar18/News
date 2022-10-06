package com.sawthandar.newsapp.framework.network.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sawthandar.newsapp.framework.network.persistence.daos.NewsListDao
import com.sawthandar.newsapp.framework.network.persistence.typeconverters.ArticlesListTypeConverter
import com.sawthandar.newsapp.framework.vos.NewsListVO

@Database(entities = [NewsListVO::class], version = 1, exportSchema = false)
@TypeConverters(ArticlesListTypeConverter::class)
abstract class NewsListDatabase: RoomDatabase() {

    abstract fun newListDao(): NewsListDao
}