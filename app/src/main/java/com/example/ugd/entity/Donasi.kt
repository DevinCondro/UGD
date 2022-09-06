package com.example.ugd.entity

class Donasi (var namaDonasi: String, var targetDonasi: Double) {

    companion object{
        @JvmField
        var listOfDonasi = arrayOf(
            Donasi("Bencana tanah longsor", 15000000.00),
            Donasi("Gempa Bumi", 10000000.00),
            Donasi("Bencana tanah longsor", 1000000.00)
        )
    }
}