package com.example.ugd.api

class PenggunaAPI {
    companion object{
        val BASE_URL = "http://192.168.100.21/ugd_kelompok2/public/api/"

        val GET_ALL_URL = BASE_URL + "pengguna/"
        val GET_BY_ID_URL = BASE_URL + "pengguna/"
        val ADD_URL = BASE_URL + "pengguna"
        val UPDATE_URL = BASE_URL + "pengguna/"
        val DELETE_URL = BASE_URL + "pengguna/"
    }
}