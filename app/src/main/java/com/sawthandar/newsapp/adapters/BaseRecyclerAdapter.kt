package com.sawthandar.newsapp.adapters

import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T: BaseRecyclerViewHolder<W>, W>: RecyclerView.Adapter<T>() {

    private var data: MutableList<W> = ArrayList()

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: T, position: Int) = holder.bindData(data[position])

    fun setNewData(newData: MutableList<W>) {
        if (newData.isEmpty()) data.clear() else data = newData
        notifyDataSetChanged()
    }

}