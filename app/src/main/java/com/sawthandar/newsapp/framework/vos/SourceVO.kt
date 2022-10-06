package com.sawthandar.newsapp.framework.vos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SourceVO(

    @SerializedName("name")
    var name: String? = null

) : Parcelable