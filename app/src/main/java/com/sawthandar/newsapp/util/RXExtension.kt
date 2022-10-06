package com.sawthandar.newsapp.util

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
fun Completable.subscribeDBWithCompletable() {
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            Log.d("CRUD", "Success")
        }, {
            Log.d("CRUD", "Fail")
        })
}