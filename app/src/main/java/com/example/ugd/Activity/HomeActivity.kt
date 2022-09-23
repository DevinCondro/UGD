package com.example.ugd.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ugd.Fragment.FragmentDonatur
import com.example.ugd.Fragment.FragmentHome
import com.example.ugd.Fragment.FragmentProfile
import com.example.ugd.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var navigation: BottomNavigationView
    private lateinit var tvText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
                    tvText.text = item.title
                    changeFragment(FragmentHome())
                    return@setOnItemSelectedListener true
                }
                R.id.donatur1 -> {
                    tvText.text = null
                    changeFragment(FragmentDonatur())
                    return@setOnItemSelectedListener true
                }
                R.id.Profil -> {
                    tvText.text = null
                    changeFragment(FragmentProfile())
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}
