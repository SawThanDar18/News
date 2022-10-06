package com.sawthandar.newsapp.framework.vos

import android.os.Parcelable
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticlesListVO (

    @Embedded(prefix = "source_")
    @SerializedName("source")
    var source: SourceVO? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("urlToImage")
    var urlToImage: String? = null,

    @SerializedName("content")
    var content: String? = null

) : Parcelable