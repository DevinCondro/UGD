package com.example.ugd


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity


class SplashScreen : AppCompatActivity() {

    private val myPreferences = "mypref"
    private val nama = "nameKey"
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(myPreferences, MODE_PRIVATE)

        if(sharedPreferences!!.contains(nama)){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            setContentView(R.layout.activity_splash_screen)
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 5000)

            val editor: SharedPreferences.Editor =
                sharedPreferences!!.edit()
            editor.putString(nama, "Done")
            editor.apply()
        }
    }

}