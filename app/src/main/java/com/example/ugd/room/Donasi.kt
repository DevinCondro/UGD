package com.example.ugd.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Donasi (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val judulDonasi: String,
    val Deskripsi: String,
    val target: String,
    val namaPengalang: String,
    val caraPembayaran: String,
    val daerah: String
)