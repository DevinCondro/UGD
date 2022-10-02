package com.example.ugd.Activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ugd.DonasiAdapter
import com.example.ugd.R
import com.example.ugd.databinding.ActivityDonasiBinding
import com.example.ugd.room.Constant
import com.example.ugd.room.Donasi
import com.example.ugd.room.DonasiDB
import kotlinx.android.synthetic.main.activity_donasi.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DonasiActivity : AppCompatActivity() {
    val db by lazy { DonasiDB(this) }
    lateinit var donasiAdapter: DonasiAdapter
    private lateinit var binding: ActivityDonasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donasi)
        binding = ActivityDonasiBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        setupListener()
        setupRecyclerView()
    }
    private fun setupRecyclerView() {
        donasiAdapter = DonasiAdapter(arrayListOf(), object :
            DonasiAdapter.OnAdapterListener {
            override fun onClick(donasi: Donasi){
                intentEdit(donasi.id, Constant.TYPE_READ)
            }
            override fun onUpdate(donasi: Donasi) {
                intentEdit(donasi.id, Constant.TYPE_UPDATE)
            }
            override fun onDelete(donasi: Donasi) {
                deleteDialog(donasi)
            }
        })
        list_donasi.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = donasiAdapter
        }
    }
    private fun deleteDialog(donasi: Donasi){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Are You Sure to delete this data From ${donasi.judulDonasi}?")
            setNegativeButton("Cancel", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            setPositiveButton("Delete", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.donasiDao().deleteDonasi(donasi)
                    loadData()
                }
            })
        }
        alertDialog.show()
    }
    override fun onStart() {
        super.onStart()
        loadData()
    }
    fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.donasiDao().getDonasi()
            Log.d("MainActivity","dbResponse: $notes")
            withContext(Dispatchers.Main){
                donasiAdapter.setData( notes )
            }
        }
    }
    fun setupListener() {
        binding.btnCreate.setOnClickListener{
            intentEdit(0,Constant.TYPE_CREATE)
        }
    }
    fun intentEdit(donasiId : Int, intentType: Int){
        startActivity(
            Intent(applicationContext, UpdateDonasi::class.java)
                .putExtra("intent_id", donasiId)
                .putExtra("intent_type", intentType)
        )
    }
}