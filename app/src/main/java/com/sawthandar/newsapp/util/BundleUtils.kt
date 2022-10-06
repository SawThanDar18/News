package com.sawthandar.newsapp.util

import android.os.Bundle
import com.sawthandar.newsapp.framework.vos.ArticlesListVO

fun getBundleNewsDetail(articlesList: ArticlesListVO): Bundle{
    return Bundle().apply {
        putParcelable("news_article", articlesList)
    }
}