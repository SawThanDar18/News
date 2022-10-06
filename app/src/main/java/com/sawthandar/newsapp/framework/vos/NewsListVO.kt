package com.sawthandar.newsapp.framework.vos

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "news_list")
data class NewsListVO(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @SerializedName("status")
    var status: String? = null,

    @SerializedName("totalResults")
    var totalResults: Long? = null,

    @SerializedName("articles")
    var articles: MutableList<ArticlesListVO>? = null

)