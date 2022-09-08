package com.example.ugd

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity() {

    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    var mBundle: Bundle?=null
    lateinit var Nama: String
    lateinit var Password: String
    lateinit var TanggalLahir: String
    lateinit var Email: String
    lateinit var Handphone: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("User Login")

        inputUsername = findViewById(R.id.username)
        inputPassword = findViewById(R.id.password)
        mainLayout = findViewById(R.id.mainLayout)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            val mBundle = Bundle()

            mBundle.putString("Username", inputUsername.toString())
            mBundle.putString("Password", inputPassword.toString())
            startActivity(intent)
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = false
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if(username.isEmpty()){
                inputUsername.setError("Username must be filled with text")
                checkLogin = false
            }

            if(password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
                checkLogin = false
            }

            getBundle()
            if(mBundle == null) {
                checkLogin = false
                Snackbar.make(mainLayout, "Silahkan Daftar Dahulu", Snackbar.LENGTH_LONG).show()
            }else if (username == Nama && password == Password ) {
                checkLogin = true
            }else{
                checkLogin = false
                Snackbar.make(mainLayout, "Username dan Password SALAH", Snackbar.LENGTH_LONG).show()
            }


            if(!checkLogin)return@OnClickListener
            val moveHome = Intent( this@MainActivity, HomeActivity::class.java)
            startActivity(moveHome)
        })


    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("Please confirm.")
            setMessage("Are you want to exit the app?")

            setPositiveButton("Yes") { _, _ ->
                super.onBackPressed()
            }

            setNegativeButton("No"){_, _ ->
                Toast.makeText(this@MainActivity, "Thank you",
                    Toast.LENGTH_LONG).show()
            }

            setCancelable(true)
        }.create().show()
    }

    fun getBundle(){

        if(intent.getBundleExtra("register") != null) {
            mBundle = intent.getBundleExtra("register")

            // Mengambil data dari bundle
            Nama = mBundle?.getString("username")!!
            Email = mBundle?.getString("email")!!
            TanggalLahir = mBundle?.getString("Tanggallahir")!!
            Password = mBundle?.getString("password")!!
            Handphone = mBundle?.getString("NoHandphone")!!
        }
    }
}
