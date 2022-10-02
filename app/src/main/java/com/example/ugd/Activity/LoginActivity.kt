package com.example.ugd.Activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ugd.R
import com.example.ugd.room.UserDB
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    private lateinit var editUsername : TextInputEditText
    private lateinit var editPassword : TextInputEditText
    lateinit var mBundle: Bundle

    lateinit var Username: String
    lateinit var Password: String
    lateinit var Email: String
    lateinit var Tanggal : String
    lateinit var NoTelp: String

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        val db by lazy { UserDB(this) }
        val userDao = db.userDao()
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        inputUsername = findViewById(R.id.username)
        inputPassword = findViewById(R.id.password)
        mainLayout = findViewById(R.id.mainLayout)
        editUsername = findViewById(R.id.etUser)
        editPassword = findViewById(R.id.etPass)

        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnRegister: Button = findViewById(R.id.btnRegister)

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

            val user = userDao.checkUser(username,password)
            if(user !=null) {
                sharedPreferences.edit()
                    .putInt("id", user.id)
                    .apply()

                checkLogin = true
            }

            if(intent.getBundleExtra("Register") != null) {
                if(username == user?.username && password == user.password) {
                    checkLogin = true
                }
            }

            if(!checkLogin) {
                loginAlert()
                return@OnClickListener
            }

            val moveHome = Intent (this@LoginActivity, HomeActivity::class.java)
            startActivity(moveHome)
        })

        btnRegister.setOnClickListener(View.OnClickListener {
            val moveRegister = Intent (this@LoginActivity, RegisterActivity::class.java)
            startActivity(moveRegister)
        })

    }

    fun loginAlert() {
        val builder = AlertDialog.Builder(this)
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(applicationContext,
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }
        builder.setTitle("Warning!")
        builder.setMessage("Maaf, Username dan Password Salah. Silahkan Login Kembali")
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
                Toast.makeText(this@LoginActivity, "Terima Kasih",
                    Toast.LENGTH_LONG).show()
            }

            setCancelable(true)
        }.create().show()
    }

    fun getBundle() {
        if (intent.getBundleExtra("Register") != null) {
            mBundle = intent.getBundleExtra("Register")!!
            Username = mBundle.getString("username")!!
            Tanggal = mBundle.getString("Tanggallahir")!!
            Email = mBundle.getString("email")!!
            NoTelp = mBundle.getString("NoHandphone")!!
            Password = mBundle.getString("password")!!
            editUsername.setText(Username)
            editPassword.setText(Password)
        }
    }


}