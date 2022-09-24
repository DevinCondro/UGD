package com.example.ugd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd.entity.Donatur

class RVProfileAdapter(private val data: Array<Donatur>) : RecyclerView.Adapter<RVProfileAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVProfileAdapter.viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_profile,parent,false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = data[position]
        holder.tv_namaDonatur.text = currentItem.namaDonatur
        holder.tv_jumlahDonasi.text = "Rp. ${currentItem.jumlahDonasi}"
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tv_namaDonatur : TextView = itemView.findViewById(R.id.tv_namaDonatur)
        val tv_jumlahDonasi: TextView = itemView.findViewById(R.id.tv_jumlahDonasi)
    }
}