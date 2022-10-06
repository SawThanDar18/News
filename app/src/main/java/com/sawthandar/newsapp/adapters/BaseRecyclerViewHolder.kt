package com.sawthandar.newsapp.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewHolder<W>(itemView: View): RecyclerView.ViewHolder(itemView) {
    var data: W? = null

    abstract fun bindData(datas: W)
}