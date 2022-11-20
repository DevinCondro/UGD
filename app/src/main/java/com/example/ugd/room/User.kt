package com.example.ugd.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val username: String,
        val Email: String,
        val nomorTelepon: String,
        val tanggal: String,
        val password: String
)
