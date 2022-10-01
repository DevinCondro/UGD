package com.example.ugd.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ugd.R
import com.example.ugd.room.*
import kotlinx.android.synthetic.main.activity_update_donasi.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateDonasi : AppCompatActivity() {
    val db by lazy { DonasiDB(this) }
    private var donasiId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_donasi)
        setupView()
        setupListener()
        Toast.makeText(this,donasiId.toString(), Toast.LENGTH_SHORT).show()
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
            Constant.TYPE_READ -> {
                getDonasi()
            }
        }
    }
    private fun setupListener() {
        button_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.donasiDao().updateDonasi(
                    Donasi(donasiId, editJudul.text.toString(),
                        editDeskripsi.text.toString(), edittarget.text.toString(),
                       editPenggalang.text.toString())
                )
                finish()
            }
        }
    }
    fun getDonasi() {
        donasiId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val donasies = db.donasiDao().getDonasi(donasiId)[0]
            editJudul.setText(donasies.judulDonasi)
            editDeskripsi.setText(donasies.Deskripsi)
            edittarget.setText(donasies.target)
            editPenggalang.setText(donasies.namaPengalang)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}