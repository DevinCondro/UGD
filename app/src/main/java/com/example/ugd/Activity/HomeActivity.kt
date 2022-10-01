package com.example.ugd.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ugd.Fragment.FragmentDonasi
import com.example.ugd.Fragment.FragmentDonatur
import com.example.ugd.Fragment.FragmentHome
import com.example.ugd.Fragment.FragmentProfile
import com.example.ugd.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var navigation: BottomNavigationView
    private lateinit var tvText : TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
        setTitle("Page Donasi")

        changeFragment(FragmentHome())
        init()
        navigationListener()
    }

    private fun init() {
        tvText = findViewById(R.id.tv_text)
        navigation = findViewById(R.id.navigasiBawah)
    }


    fun changeFragment(fragment: Fragment?){
        if (fragment != null){
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_fragment, fragment)
                .commit()
        }
    }

    private fun navigationListener() {
        navigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Menu -> {
                    changeFragment(FragmentHome())
                    return@setOnItemSelectedListener true
                }
                R.id.donatur1 -> {
                    changeFragment(FragmentDonatur())
                    return@setOnItemSelectedListener true
                }
                R.id.Profil -> {
                    changeFragment(FragmentProfile())
                    return@setOnItemSelectedListener true
                }
                R.id.Tambah -> {
                    setContentView(R.layout.activity_donasi)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    fun getSharedPreferences() : SharedPreferences{
        return sharedPreferences
    }
}
