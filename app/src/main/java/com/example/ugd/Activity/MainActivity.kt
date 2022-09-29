package com.example.ugd.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ugd.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var editUser: EditText
    lateinit var userEdit: TextInputEditText
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    var mBundle: Bundle?=null
    lateinit var Nama: String
    lateinit var Password: String
    lateinit var TanggalLahir: String
    lateinit var Email: String
    lateinit var Handphone: String
    private val myPreference = "myPref"
    private val name = "nameKey"
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("User Login")

        inputUsername = findViewById(R.id.username)
        inputPassword = findViewById(R.id.password)
        mainLayout = findViewById(R.id.mainLayout)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        editUser = findViewById(R.id.etUser)
        sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        if(sharedPreferences!!.contains(name)){
            editUser?.setText(sharedPreferences!!.getString(name, ""))
        }

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
                checkLogin = true
            }

            if(password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
                checkLogin = true
            }

            getBundle()
            if(mBundle == null) {
                checkLogin = true
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

    fun saveData(view: View){
        val strName: String = editUser?.text.toString().trim()
        val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
        editor.putString(name, strName)
        editor.apply()
        Toast.makeText(baseContext, "Saved", Toast.LENGTH_SHORT).show()
    }

    fun readData(view: View){
        userEdit = findViewById(R.id.username)
        var strName: String = editUser?.text.toString().trim()
        strName = sharedPreferences!!.getString(name, "")!!
        sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        if(sharedPreferences!!.contains(name)){
            userEdit.text
        }
        Toast.makeText(baseContext, "Data Retrieved", Toast.LENGTH_SHORT).show()
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
            Nama = mBundle?.getString("username")!!
            Email = mBundle?.getString("email")!!
            TanggalLahir = mBundle?.getString("Tanggallahir")!!
            Password = mBundle?.getString("password")!!
            Handphone = mBundle?.getString("NoHandphone")!!
        }
    }
}
