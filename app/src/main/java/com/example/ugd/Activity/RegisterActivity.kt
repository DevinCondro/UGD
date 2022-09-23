package com.example.ugd.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ugd.R
import com.example.ugd.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setTitle("Register User")

        var btnSignUp = binding.btnSignUp
        var btnCancel = binding.btnCancel
        var registerUsername = binding.etUsername
        var registerPassword = binding.etPassword
        var registerEmail = binding.etEmail
        var registerTanggal = binding.etTanggal
        var registerTelp = binding.etPhone

        binding.btnSignUp.setOnClickListener (View.OnClickListener{
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
            else if(password.isEmpty()){
                registerPassword.setError("Password must be filled with text")
                checkRegister = false
            }
            else if(email.isEmpty()){
                registerEmail.setError("Email must be filled with text")
                checkRegister = false
            }
            else if(tanggal.isEmpty()){
                registerTanggal.setError("Tanggal must be filled with text")
                checkRegister = false
            }
            else if(telfon.isEmpty()){
                registerTelp.setError("No Telp must be filled with text")
                checkRegister = false
            }
            else{
                checkRegister = true
            }

            intent.putExtra("register", mBundle)
            if(!checkRegister)return@OnClickListener
            val moveHome = Intent( this@RegisterActivity, MainActivity::class.java)
            startActivity(moveHome)
        })

        btnCancel.setOnClickListener{
            registerUsername.setText("")
            registerPassword.setText("")
            registerEmail.setText("")
            registerTanggal.setText("")
            registerTelp.setText("")

            Snackbar.make(mainLayout, "Text Cleared Success", Snackbar.LENGTH_LONG).show()
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