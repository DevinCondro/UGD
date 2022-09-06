package com.example.ugd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("User Login")

        inputUsername = findViewById(R.id.username)
        inputPassword = findViewById(R.id.password)
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
            var username: String = inputUsername.getEditText()?.getText().toString()
            var password: String = inputPassword.getEditText()?.getText().toString()

            if(username.isEmpty()){
                inputUsername.setError("Username must be filled with a text")
                checkLogin = false
            }

            if(password.isEmpty()){
                inputPassword.setError("Password must be filled with a text")
                checkLogin = false
            }

            if(username == "admin" && password == "0729") checkLogin = true
            if(username == "Devin" && password == "0729") checkLogin = true
            if(username == "Alfa" && password == "0388") checkLogin = true
            if(!checkLogin) return@OnClickListener
            val moveHome = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(moveHome)
        })

    }
}