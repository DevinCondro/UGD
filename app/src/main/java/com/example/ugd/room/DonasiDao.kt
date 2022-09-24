package com.example.ugd.room

import androidx.room.*

@Dao
interface DonasiDao {
    @Insert
    suspend fun addDonasi(donasi: Donasi)
    @Update
    suspend fun updateDonasi(donasi: Donasi)
    @Delete
    suspend fun deleteDonasi(donasi: Donasi)
    @Query("SELECT * FROM donasi")
    suspend fun getDonasi() : List<Donasi>
    @Query("SELECT * FROM donasi WHERE id =:donasi_id")
    suspend fun getDonasi(donasi_id: Int) : List<Donasi>
}