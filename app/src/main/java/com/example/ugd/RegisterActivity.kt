package com.example.ugd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText


class RegisterActivity : AppCompatActivity() {

    private lateinit var registerUsername: TextInputEditText
    private lateinit var registerPassword: TextInputEditText
    private lateinit var registerEmail: TextInputEditText
    private lateinit var mainLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setTitle("Register User")

        registerUsername = findViewById(R.id.etUsername)
        registerPassword = findViewById(R.id.etPassword)
        registerEmail = findViewById(R.id.etEmail)
        mainLayout = findViewById(R.id.mainLayout)
        val btnCancel: Button = findViewById(R.id.btnCancel)
        val btnSignUp: Button = findViewById(R.id.btnSignUp)

        btnCancel.setOnClickListener{
            registerUsername.setText("")
            registerPassword.setText("")
            registerEmail.setText("")

            Snackbar.make(mainLayout, "Text Cleared Success", Snackbar.LENGTH_LONG).show()
        }
    }
}