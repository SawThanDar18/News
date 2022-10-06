package com.sawthandar.newsapp.di

import android.app.Application
import androidx.room.Room
import com.sawthandar.newsapp.framework.network.persistence.NewsListDatabase
import com.sawthandar.newsapp.framework.network.persistence.daos.NewsListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): NewsListDatabase{
        return Room.databaseBuilder(application, NewsListDatabase::class.java, "News.db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideArticleDao(db: NewsListDatabase): NewsListDao{
        return db.newListDao()
    }
}