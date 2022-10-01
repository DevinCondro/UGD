package com.example.ugd.entity

class Donatur (var namaDonatur: String, var jumlahDonasi: Float, var gender: String){

    companion object{
        @JvmField
        var listOfDonatur = arrayOf(
            Donatur("Wendy", 100000F, "Pria"),
            Donatur("Alfa", 350000F, "Pria"),
            Donatur("Matius", 1000000F, "Pria"),
            Donatur("Yosi", 5000000F, "Wanita"),
            Donatur("Devin", 200000F, "Pria"),
            Donatur("Alex", 50000F, "Pria"),
            Donatur("Cindy", 700000F, "Wanita"),
            Donatur("Matt", 550000F, "Pria"),
            Donatur("Jo", 590000F, "Pria"),
            Donatur("Erigo", 45600000F, "Pria"),
        )
    }

}