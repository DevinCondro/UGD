package com.example.ugd.entity

class Donasi (var name: String, var perluDonasi: Double) {

    companion object{
        @JvmField
        var listOfDonasi = arrayOf(
            Donasi()
        )
    }
}