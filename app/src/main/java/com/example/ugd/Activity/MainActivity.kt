package com.example.ugd.Activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ugd.R
import com.example.ugd.room.User
import com.example.ugd.room.UserDB
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var etPass: TextInputEditText
    lateinit var etUser: TextInputEditText
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    lateinit var mBundle: Bundle

    lateinit var Username: String
    lateinit var Password: String
    lateinit var TanggalLahir: String
    lateinit var Email: String
    lateinit var Handphone: String
    private val myPreference = "myPref"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        setTitle("User Login")

        inputUsername = findViewById(R.id.username)
        inputPassword = findViewById(R.id.password)
        mainLayout = findViewById(R.id.mainLayout)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        etUser = findViewById(R.id.etUser)
        etPass = findViewById(R.id.etPass)
        sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE)

        getBundle()

        btnLogin.setOnClickListener(View.OnClickListener {
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()
            var checkLogin = false

            if (username.isEmpty()) {
                inputUsername.setError("Username must be filled with text")
                checkLogin = false
            }

            if (password.isEmpty()) {
                inputPassword.setError("Password must be filled with text")
                checkLogin = false
            }

            if(username == "admin" && password == "admin") {
                checkLogin = true
            }

            if(intent.getBundleExtra("Register") != null) {
                if(username == Username && password == Password) {
                    checkLogin = true
                }
            }

            if(checkLogin == true) {
                loginAlert()
                return@OnClickListener
            }

            val moveHome = Intent (this@MainActivity, HomeActivity::class.java)
            startActivity(moveHome)
        })

        btnRegister.setOnClickListener(View.OnClickListener {
            val moveRegister = Intent (this@MainActivity, RegisterActivity::class.java)
            startActivity(moveRegister)
        })

    }

    fun loginAlert() {
        val builder = AlertDialog.Builder(this)
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(applicationContext,
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }
        builder.setTitle("Error!")
        builder.setMessage("Maaf, Username dan Password Salah. Tolong Cek Kembali")
        builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
        builder.show()
    }
    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("Tolong Konfirmasi")
            setMessage("Apakah anda yakin ingin keluar?")

            setPositiveButton("Iya") { _, _ ->
                super.onBackPressed()
            }

            setNegativeButton("Tidak"){_, _ ->
                Toast.makeText(this@MainActivity, "Terima Kasih",
                    Toast.LENGTH_LONG).show()
            }

            setCancelable(true)
        }.create().show()
    }

        fun getBundle() {
            if (intent.getBundleExtra("register") != null) {
                mBundle = intent.getBundleExtra("register")!!
                Username = mBundle.getString("username")!!
                Email = mBundle.getString("email")!!
                TanggalLahir = mBundle.getString("Tanggallahir")!!
                Password = mBundle.getString("password")!!
                Handphone = mBundle.getString("NoHandphone")!!
                etUser.setText(Username)
                etPass.setText(Password)
            }
        }
}

