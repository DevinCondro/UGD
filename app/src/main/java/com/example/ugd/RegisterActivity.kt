package com.example.ugd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText


class RegisterActivity : AppCompatActivity() {

    private lateinit var registerUsername: TextInputEditText
    private lateinit var registerPassword: TextInputEditText
    private lateinit var registerEmail: TextInputEditText
    private lateinit var registerTanggal : TextInputEditText
    private lateinit var registerTelp : TextInputEditText
    private lateinit var registerLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setTitle("Register User")

        registerUsername = findViewById(R.id.etUsername)
        registerPassword = findViewById(R.id.etPassword)
        registerEmail = findViewById(R.id.etEmail)
        registerTanggal = findViewById(R.id.etTanggal)
        registerTelp = findViewById(R.id.etPhone)
        registerLayout = findViewById(R.id.registerLayout)
        val btnCancel: Button = findViewById(R.id.btnCancel)
        val btnSignUp: Button = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener (View.OnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            val mBundle = Bundle()
            val username: String = registerUsername.getText().toString()
            val password: String = registerPassword.getText().toString()
            val email: String = registerEmail.getText().toString()
            val tanggal: String = registerTanggal.getText().toString()
            val telfon: String = registerTelp.getText().toString()
            var checkRegister = false

            mBundle.putString("username", registerUsername.text.toString())
            mBundle.putString("email", registerEmail.text.toString())
            mBundle.putString("password", registerPassword.text.toString())
            mBundle.putString("Tanggallahir", registerTanggal.text.toString())
            mBundle.putString("NoHandphone", registerTelp.text.toString())

            if(username.isEmpty()){
                registerUsername.setError("Username must be filled with text")
                checkRegister = false
            }
            if(password.isEmpty()){
                registerPassword.setError("Password must be filled with text")
                checkRegister = false
            }
            if(email.isEmpty()){
                registerEmail.setError("Email must be filled with text")
                checkRegister = false
            }
            if(tanggal.isEmpty()){
                registerTanggal.setError("Tanggal must be filled with text")
                checkRegister = false
            }
            if(telfon.isEmpty()){
                registerTelp.setError("No Telp must be filled with text")
                checkRegister = false
            }

            intent.putExtra("register", mBundle)
            if(!checkRegister)return@OnClickListener
            startActivity(intent)
        })

        btnCancel.setOnClickListener{
            registerUsername.setText("")
            registerPassword.setText("")
            registerEmail.setText("")
            registerTanggal.setText("")
            registerTelp.setText("")

            Snackbar.make(registerLayout, "Text Cleared Success", Snackbar.LENGTH_LONG).show()
        }

    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("Please confirm.")
            setMessage("Are you want to exit the app?")

            setPositiveButton("Yes") { _, _ ->
                super.onBackPressed()
            }

            setNegativeButton("No"){_, _ ->
                Toast.makeText(this@RegisterActivity, "Thank you",
                    Toast.LENGTH_LONG).show()
            }

            setCancelable(true)
        }.create().show()
    }
}