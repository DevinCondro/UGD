package com.example.ugd.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Donasi::class],
    version = 1
)
abstract class DonasiDB: RoomDatabase() {
    abstract fun donasiDao(): DonasiDao
    companion object {
        @Volatile private var instance : DonasiDB? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                DonasiDB::class.java,
                "donasi.db"
            ).build()
    }
}