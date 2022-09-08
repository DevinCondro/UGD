package com.example.ugd.entity

class Donatur (var namaDonatur: String, var jumlahDonasi: Double){

    companion object{
        @JvmField
        var listOfDonatur = arrayOf(
            Donatur("Wendy", 100.00),
            Donatur("Alfa", 35.00),
            Donatur("Matius", 1000.00),
            Donatur("Yosi", 500.00),
            Donatur("Devin", 2000.00),
            Donatur("Alex", 50.00),
            Donatur("Cindy", 70.00),
            Donatur("Matt", 550.00),
            Donatur("Jo", 59.00),
            Donatur("Erigo", 456.00),
        )
    }

}