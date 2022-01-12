package com.chkan.iqtimer.ui.statistic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chkan.iqtimer.R
import com.chkan.iqtimer.data.room.DatabaseModel

class StatisticAdapter : RecyclerView.Adapter<MainViewHolder>() {

    private var list: List<DatabaseModel> = listOf()

    fun setList (list: List<DatabaseModel>){
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_statistic_item,parent,false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = list[position]
        holder.bindData(item)
    }

    override fun getItemCount(): Int = list.size
}