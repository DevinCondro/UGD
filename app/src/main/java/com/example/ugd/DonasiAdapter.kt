package com.example.ugd

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd.room.Donasi
import kotlinx.android.synthetic.main.donasi_adapter.view.*

class DonasiAdapter (private val donasies: ArrayList<Donasi>, private val
listener: OnAdapterListener
) :
    RecyclerView.Adapter<DonasiAdapter.DonasiViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            DonasiViewHolder {
        return DonasiViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.donasi_adapter,parent, false)
        )
    }
    override fun onBindViewHolder(holder: DonasiViewHolder, position: Int) {
        val donasi = donasies[position]
        holder.view.judul.text = donasi.judulDonasi
        holder.view.Deskripsi.text = donasi.Deskripsi
        holder.view.nominal.text = donasi.target
        holder.view.penggalang.text = donasi.namaPengalang
        holder.view.relativeLayout.setOnClickListener{
            listener.onClick(donasi)
        }
        holder.view.icon_edit.setOnClickListener {
            listener.onUpdate(donasi)
        }
        holder.view.icon_delete.setOnClickListener {
            listener.onDelete(donasi)
        }
    }
    override fun getItemCount() = donasies.size

    inner class DonasiViewHolder( val view: View):
        RecyclerView.ViewHolder(view)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Donasi>){
        donasies.clear()
        donasies.addAll(list)
        notifyDataSetChanged()
    }
    interface OnAdapterListener {
        fun onUpdate(donasi: Donasi)
        fun onDelete(donasi: Donasi)
        fun onClick(donasi: Donasi)
    }
}