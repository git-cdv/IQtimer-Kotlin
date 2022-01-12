package com.chkan.iqtimer.ui.statistic.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chkan.iqtimer.R
import com.chkan.iqtimer.data.room.DatabaseModel

class MainViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

    fun bindData(item: DatabaseModel){
        val tv_count = itemView.findViewById<TextView>(R.id.item_count)
        val tv_date = itemView.findViewById<TextView>(R.id.item_date)
        tv_count.text = item.count.toString()
        tv_date.text = item.date_full
    }
}