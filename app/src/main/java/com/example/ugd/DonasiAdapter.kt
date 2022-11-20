package com.example.ugd

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd.Activity.DonasiActivity
import com.example.ugd.Activity.UpdateDonasi
import com.example.ugd.models.DonasiUser
import com.example.ugd.room.Donasi
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.grpc.InternalChannelz.id
import kotlinx.android.synthetic.main.donasi_adapter.view.*
import java.util.*
import kotlin.collections.ArrayList

class DonasiAdapter (private var donasiList: List<DonasiUser>, context: Context) : RecyclerView.Adapter<DonasiAdapter.ViewHolder>(), Filterable{

    private var filteredDonasiList: MutableList<DonasiUser>
    private val context: Context

    init {
        filteredDonasiList = ArrayList(donasiList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.donasi_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredDonasiList.size
    }

    fun setDonasiList(donasiList: Array<DonasiUser>){
        this.donasiList = donasiList.toList()
        filteredDonasiList = donasiList.toMutableList()
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered : MutableList<DonasiUser> = java.util.ArrayList()
                if (charSequenceString.isEmpty()){
                    filtered.addAll(donasiList)
                }else{
                    for (donasi in donasiList){
                        if (donasi.judulDonasi.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(donasi)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredDonasiList.clear()
                filteredDonasiList.addAll((filterResults.values as List<DonasiUser>))
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val donasi = filteredDonasiList[position]
        holder.judul.text = donasi.judulDonasi
        holder.deskripsi.text = "Deskripsi : " + donasi.Deskripsi
        holder.nominal.text = "Target Donasi : Rp. " + donasi.target
        holder.penggalang.text = "Nama Penggalang : " + donasi.namaPenggalang
        holder.pembayaran.text = "Cara Pembayaran : " + donasi.caraPembayaran
        holder.daerah.text = "Dearah : " + donasi.daerah

        holder.btnDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data mahasiswa ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){ _, _ ->
                    if (context is DonasiActivity){
                        context.deleteDonasi(
                            donasi.id
                        )
                    }
                }
                .show()
        }
        holder.cvDonasi.setOnClickListener{
            val i = Intent(context, UpdateDonasi::class.java)
            i.putExtra("id", donasi.id)
            if(context is DonasiActivity)
                context.startActivityForResult(i, DonasiActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var judul: TextView
        var deskripsi: TextView
        var nominal: TextView
        var penggalang: TextView
        var pembayaran: TextView
        var daerah: TextView
        var btnDelete: ImageButton
        var cvDonasi: CardView

        init {
            judul = itemView.findViewById(R.id.judul)
            deskripsi = itemView.findViewById(R.id.Deskripsi)
            nominal = itemView.findViewById(R.id.nominal)
            penggalang = itemView.findViewById(R.id.penggalang)
            pembayaran = itemView.findViewById(R.id.caraPembayaran)
            daerah = itemView.findViewById(R.id.daerah)
            btnDelete = itemView.findViewById(R.id.icon_delete)
            cvDonasi = itemView.findViewById(R.id.card_view)
        }
    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
//            DonasiViewHolder {
//        return DonasiViewHolder(
//
//            LayoutInflater.from(parent.context).inflate(R.layout.donasi_adapter,parent, false)
//        )
//    }
//    override fun onBindViewHolder(holder: DonasiViewHolder, position: Int) {
//        val donasi = donasies[position]
//        holder.view.judul.text = donasi.judulDonasi
//        holder.view.Deskripsi.text = donasi.Deskripsi
//        holder.view.nominal.text = donasi.target
//        holder.view.penggalang.text = donasi.namaPengalang
//        holder.view.caraPembayaran.text = "Cara Pembayaran : " +donasi.caraPembayaran
//        holder.view.daerah.text = "Daerah : " + donasi.daerah
//        holder.view.relativeLayout.setOnClickListener{
//            listener.onClick(donasi)
//        }
//        holder.view.icon_edit.setOnClickListener {
//            listener.onUpdate(donasi)
//        }
//        holder.view.icon_delete.setOnClickListener {
//            listener.onDelete(donasi)
//        }
//    }
//    override fun getItemCount() = donasies.size
//
//    inner class DonasiViewHolder( val view: View):
//        RecyclerView.ViewHolder(view)
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun setData(list: List<Donasi>){
//        donasies.clear()
//        donasies.addAll(list)
//        notifyDataSetChanged()
//    }
//    interface OnAdapterListener {
//        fun onUpdate(donasi: Donasi)
//        fun onDelete(donasi: Donasi)
//        fun onClick(donasi: Donasi)
//    }
}