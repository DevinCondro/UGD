package com.example.ugd

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
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
                R.id.setting -> {
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    /*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.navigasi_bawah, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.donatur1){
            changeFragment(FragmentDonatur())
        } else {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@HomeActivity)
            builder.setMessage("Are you sure want to exit?")
                .setPositiveButton("YES", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {
                        finishAndRemoveTask()
                    }
                })
                .show()
        }
        return super.onOptionsItemSelected(item)
    }
     */



//    fun getBundle(){
//        mBundle = intent.getBundleExtra("Data")!!
//    }
}
