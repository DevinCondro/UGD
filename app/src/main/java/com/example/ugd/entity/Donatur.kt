package com.example.ugd.entity

class Donatur (var namaDonatur: String, var jumlahDonasi: Float){

    companion object{
        @JvmField
        var listOfDonatur = arrayOf(
            Donatur("Wendy", 100000F),
            Donatur("Alfa", 350000F),
            Donatur("Matius", 1000000F),
            Donatur("Yosi", 5000000F),
            Donatur("Devin", 200000F),
            Donatur("Alex", 50000F),
            Donatur("Cindy", 700000F),
            Donatur("Matt", 550000F),
            Donatur("Jo", 590000F),
            Donatur("Erigo", 45600000F),
        )
    }

}