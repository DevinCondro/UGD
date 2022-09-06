package com.example.ugd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd.entity.Donasi

class RVDonasiAdapter(private val data: Array<Donasi>) : RecyclerView.Adapter<RVDonasiAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVDonasiAdapter.viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_donasi,parent,false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = data[position]
        holder.tv_namaDonasi.text = currentItem.namaDonasi
        holder.tv_targetDonasi.text = "${currentItem}"
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tv_namaDonasi : TextView = itemView.findViewById(R.id.tv_namaDonasi)
        val tv_targetDonasi : TextView = itemView.findViewById(R.id.tv_targetDonasi)
    }
}