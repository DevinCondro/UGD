package com.example.ugd.Activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ugd.R
import com.example.ugd.api.PenggunaAPI
import com.example.ugd.databinding.ActivityLoginBinding
import com.example.ugd.databinding.ActivityPdfBinding
import com.example.ugd.models.Pengguna
import com.example.ugd.room.UserDB
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class LoginActivity : AppCompatActivity() {
    private var binding: ActivityLoginBinding? = null
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
    private var layoutLoading: LinearLayout? = null

    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        queue = Volley.newRequestQueue(this)
        val view : View = binding!!.root
        setContentView(view)
        supportActionBar?.hide()

        val db by lazy { UserDB(this) }
        val userDao = db.userDao()
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        inputUsername = findViewById(R.id.username)
        inputPassword = findViewById(R.id.password)
        mainLayout = findViewById(R.id.mainLayout)
        editUsername = findViewById(R.id.etUser)
        editPassword = findViewById(R.id.etPass)
        layoutLoading = findViewById(R.id.layout_loading)

        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnFoto: ImageView = findViewById(R.id.logoLogin)
        val btnRegister: Button = findViewById(R.id.btnRegister)

        getBundle()

        btnFoto.setOnClickListener(View.OnClickListener {
            val url = "https://picsum.photos/300"
            Glide.with(this)
                .load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding!!.logoLogin)
        })

        btnLogin.setOnClickListener(View.OnClickListener {
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()
            var checkLogin = true

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
                return@OnClickListener
            }else{
                LoginUser()
            }

//            val moveHome = Intent (this@LoginActivity, HomeActivity::class.java)
//            startActivity(moveHome)
        })

        btnRegister.setOnClickListener(View.OnClickListener {
            val moveRegister = Intent (this@LoginActivity, RegisterActivity::class.java)
            startActivity(moveRegister)
        })

    }

    private fun LoginUser() {
        setLoading(true)
        if (inputUsername!!.toString().isEmpty()){
            Toast.makeText(this@LoginActivity, "Username Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
        }else if (inputPassword!!.toString().isEmpty()){
            Toast.makeText(this@LoginActivity, "Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
        }else{
            val pengguna = Pengguna(
                0,
                inputUsername.getEditText()?.getText().toString(),
                "",
                "",
                "",
                inputPassword.getEditText()?.getText().toString()

            )
            val stringRequest: StringRequest =
                object : StringRequest(Method.POST, PenggunaAPI.CHECK, Response.Listener { response ->
                    val gson = Gson()
                    var user = gson.fromJson(response, Pengguna::class.java)

                    if(user!=null) {
                        var resJO = JSONObject(response.toString())
                        val  userobj = resJO.getJSONObject("data")

//                    Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
                        MotionToast.darkColorToast(this,"Notification Login!",
                            "Login Berhasil!!",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        sharedPreferences.edit()
                            .putInt("id",userobj.getInt("id"))
                            .putString("nama",userobj.getString("username"))
                            .putString("pass",userobj.getString("password"))
                            .apply()
                        startActivity(intent)
                    }else {
                        MotionToast.darkColorToast(this,"Notification Login!",
                            "Login Gagal!!",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                        return@Listener
                    }

                }, Response.ErrorListener { error ->
                    // setLoading(false)
                    try {
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
//                    Toast.makeText(
//                        this@LoginActivity,
//                        errors.getString("message"),
//                        Toast.LENGTH_SHORT
//                    ).show()
                        MotionToast.darkColorToast(this,"Notification Login!",
                            errors.getString("message"),
                            MotionToastStyle.INFO,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    }catch (e: Exception) {
//                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                        MotionToast.darkColorToast(this,"Notification Login!",
                            e.message.toString(),
                            MotionToastStyle.INFO,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    }

                }) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Accept"] = "application/json"
                        return headers
                    }

                    @Throws(AuthFailureError::class)
                    override fun getBody(): ByteArray {
                        val gson = Gson()
                        val requestBody = gson.toJson(pengguna)
                        return requestBody.toByteArray(StandardCharsets.UTF_8)
                    }

                    override fun getBodyContentType(): String {
                        return "application/json"
                    }
                }
            queue!!.add(stringRequest)
        }
        setLoading(false)
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

    private fun setLoading(isLoading: Boolean){
        if(isLoading){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }

}