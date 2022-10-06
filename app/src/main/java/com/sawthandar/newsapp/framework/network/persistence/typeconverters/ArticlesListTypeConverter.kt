package com.sawthandar.newsapp.framework.network.persistence.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sawthandar.newsapp.framework.vos.ArticlesListVO

class ArticlesListTypeConverter {

    @TypeConverter
    fun fromListToGson(list: List<ArticlesListVO>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromGsonToList(json: String): List<ArticlesListVO>? {
        val typeToken = object : TypeToken<List<ArticlesListVO>>() {}.type
        return Gson().fromJson(json, typeToken)
    }
}