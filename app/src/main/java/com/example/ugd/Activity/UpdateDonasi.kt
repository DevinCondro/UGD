package com.example.ugd.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.ugd.R
import com.example.ugd.databinding.ActivityDonasiBinding
import com.example.ugd.databinding.ActivityUpdateDonasiBinding
import com.example.ugd.room.*
import kotlinx.android.synthetic.main.activity_update_donasi.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateDonasi : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateDonasiBinding

    val db by lazy { DonasiDB(this) }
    private var donasiId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_donasi)

        binding = ActivityUpdateDonasiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupView()
        setupListener()
        Toast.makeText(this,donasiId.toString(), Toast.LENGTH_SHORT).show()
    }

    fun setupView() {
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType) {
            Constant.TYPE_CREATE -> {
                binding.btnUpdate.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                binding.btnSave.visibility = View.GONE
                binding.btnUpdate.visibility = View.GONE
                getDonasi()

            }
            Constant.TYPE_UPDATE -> {
                binding.btnSave.visibility = View.GONE
                getDonasi()
            }
        }
    }
    private fun setupListener() {
        binding.btnSave.setOnClickListener {
            if(binding.editJudul.text.toString().isEmpty() || binding.editDeskripsi.text.toString().isEmpty() || binding.edittarget.text.toString().isEmpty() || binding.editPenggalang.text.toString().isEmpty()) {
                return@setOnClickListener
            }
            CoroutineScope(Dispatchers.IO).launch {
                run {
                    db.donasiDao().addDonasi(
                        Donasi(donasiId, editJudul.text.toString(),
                            editDeskripsi.text.toString(), edittarget.text.toString(),
                            editPenggalang.text.toString())
                    )
                    finish()
                }
            }
        }

        binding.btnUpdate.setOnClickListener {
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