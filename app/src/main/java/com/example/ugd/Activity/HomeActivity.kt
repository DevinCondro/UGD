package com.example.ugd.Activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.ugd.*
import com.example.ugd.Location.FragmentLocation
import com.google.android.material.bottomnavigation.BottomNavigationView
import nl.joery.animatedbottombar.AnimatedBottomBar

class HomeActivity : AppCompatActivity() {
    private lateinit var navigationBottom : AnimatedBottomBar
    private lateinit var textView : TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        setTitle("Ayo Donasi")

        changeFragment(FragmentHome())

        init()
        navListener()

    }
    private fun init(){
        navigationBottom = findViewById(R.id.botNavigation)
    }
    fun changeFragment(fragment: Fragment?) {
        if (fragment != null) {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_fragment, fragment)
                .commit()
        }
    }

    private fun navListener() {
        navigationBottom.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                when(newIndex) {
                    0 -> changeFragment(FragmentHome())
                    1 -> changeFragment(FragmentDonatur())
                    2 -> changeFragment(FragmentQR())
                    3 -> changeFragment(FragmentProfile())
                    4 -> changeFragment(FragmentLocation())
                    else -> changeFragment(FragmentHome())
                }
                Log.d("bottom_bar", "Selected index: $newIndex, title: ${newTab.title}")
            }

            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
                Log.d("bottom_bar", "Reselected index: $index, title: ${tab.title}")
            }
        })
    }

//    private fun navListener() {
//        navigationBottom.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.Menu -> {
//                   textView.text = item.title
//                    changeFragment(FragmentHome())
//                    return@setOnItemSelectedListener true
//                }
//                R.id.donatur -> {
//                    textView.text = null
//                    changeFragment(FragmentDonatur())
//                    return@setOnItemSelectedListener true
//                }
//                R.id.QrCode -> {
//                    textView.text = null
//                    changeFragment(FragmentQR())
//                    return@setOnItemSelectedListener true
//                }
//                R.id.Profile -> {
//                    textView.text = null
//                    changeFragment(FragmentProfile())
//                    return@setOnItemSelectedListener true
//                }
//                R.id.Location -> {
//                    textView.text = null
//                    changeFragment(FragmentLocation())
//                    return@setOnItemSelectedListener true
//                }
//            }
//            false
//        }
//    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("Tolong Konfirmasi")
            setMessage("Apakah anda yakin ingin keluar?")

            setPositiveButton("Iya") { _, _ ->
                moveTaskToBack(true)
                android.os.Process.killProcess(android.os.Process.myPid())
                System.exit(1)

            }

            setNegativeButton("Tidak"){_, _ ->

            }

            setCancelable(true)
        }.create().show()
    }

    fun getSharedPreferences() : SharedPreferences {
        return sharedPreferences
    }

}