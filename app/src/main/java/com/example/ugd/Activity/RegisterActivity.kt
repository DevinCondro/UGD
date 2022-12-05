package com.example.ugd.Activity

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd.PushNotification.NotificationReceiver
import com.example.ugd.R
import com.example.ugd.api.PenggunaAPI
import com.example.ugd.databinding.ActivityRegisterBinding
import com.example.ugd.models.Pengguna
import com.example.ugd.room.User
import com.example.ugd.room.UserDB
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val CHANNEL_ID = "channel_01"
    private val notificationId = 101
    private var queue: RequestQueue? = null
    private var layoutLoading: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        queue = Volley.newRequestQueue(this)
        val view = binding.root
        setContentView(view)

        val db by lazy { UserDB(this) }
        val userDao = db.userDao()

        supportActionBar?.hide()

        createChannel()

        var inputUsername = binding.etUsername
        var inputPassword = binding.etPassword
        var inputEmail = binding.etEmail
        var inputNoTelp = binding.etPhone
        var inputTanggal = binding.etTanggal
        layoutLoading = findViewById(R.id.layout_loading)

        binding.etTanggal.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    binding.etTanggal.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)

                }, year, month, day)

            dpd.show()
        }

        binding.btnSignUp.setOnClickListener(View.OnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            val mBundle = Bundle()

            val username: String = inputUsername.getText().toString()
            val password: String = inputPassword.getText().toString()
            val email: String = inputEmail.getText().toString()
            val tanggal: String = inputTanggal.getText().toString()
            val telfon: String = inputNoTelp.getText().toString()
            var checkRegister = false

            mBundle.putString("username", inputUsername.text.toString())
            mBundle.putString("email", inputEmail.text.toString())
            mBundle.putString("password", inputPassword.text.toString())
            mBundle.putString("Tanggallahir", inputTanggal.text.toString())
            mBundle.putString("NoHandphone",inputTanggal.text.toString())

            if(username.isEmpty()){
                binding.etUsername.setError("Username must be filled with text")
                checkRegister = false
            }
            else if(password.isEmpty()){
                binding.etPassword.setError("Password must be filled with text")
                checkRegister = false
            }
            else if(email.isEmpty()){
                binding.etEmail.setError("Email must be filled with text")
                checkRegister = false
            }
            else if(tanggal.isEmpty()){
                binding.etTanggal.setError("Tanggal must be filled with text")
                checkRegister = false
            }
            else if(telfon.length != 12){
                binding.etPhone.setError("No Telp must be minimum 12 digit")
                checkRegister = false
            }
            if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty() && !tanggal.isEmpty() && !telfon.isEmpty()){
                checkRegister = true
            }

            if(!checkRegister){
                return@OnClickListener
            }else{
                createUser(mBundle)
            }

//            val user = User(0, username, email, telfon, tanggal, password)
//            userDao.addUser(user)


//            intent.putExtra("Register", mBundle)
//            startActivity(intent)
        })
    }

    private fun createUser(mBundle: Bundle) {
        setLoading(true)
        if (etUsername!!.text.toString().isEmpty()){
            Toast.makeText(this@RegisterActivity, "Username Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
        }
        else if (etEmail!!.text.toString().isEmpty()){
            Toast.makeText(this@RegisterActivity, "Email Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
        }
        else if (etPhone!!.text.toString().isEmpty() && etPhone!!.text.toString().length < 12){
            Toast.makeText(this@RegisterActivity, "Nomor Telepon Tidak Boleh Kosong dan Harus 12-13 Digit", Toast.LENGTH_SHORT).show()
        }
        else if (etTanggal!!.text.toString().isEmpty()){
            Toast.makeText(this@RegisterActivity, "Tanggal Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
        }
        else if (etPassword!!.text.toString().isEmpty()){
            Toast.makeText(this@RegisterActivity, "Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
        }
        else{
            val userprofil = Pengguna(
                0,
                binding.etUsername.getText().toString(),
                binding.etEmail.getText().toString(),
                binding.etPhone.getText().toString(),
                binding.etTanggal.getText().toString(),
                binding.etPassword.getText().toString()
            )
            val stringRequest: StringRequest =
                object : StringRequest(Method.POST, PenggunaAPI.REGISTER, Response.Listener { response ->
                    val gson = Gson()
                    var user = gson.fromJson(response, PenggunaAPI::class.java)

                    if(user != null) {
//                    Toast.makeText(this@RegisterActivity, "User Berhasil Register", Toast.LENGTH_SHORT).show()
                        MotionToast.darkColorToast(this,"Notification Register!",
                            "Register Berhasil!!",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        sendNotification()
                        intent.putExtra("Register", mBundle)
                        startActivity(intent)
                    }
                }, Response.ErrorListener { error ->
                    // setLoading(false)
                    try {
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
//                    Toast.makeText(
//                        this@RegisterActivity,
//                        errors.getString("message"),
//                        Toast.LENGTH_SHORT
//                    ).show()
                        MotionToast.darkColorToast(this,"Notification Register!",
                            errors.getString("message"),
                            MotionToastStyle.INFO,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    }catch (e: Exception) {
//                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
                        MotionToast.darkColorToast(this,"Notification Register!",
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
                        val requestBody = gson.toJson(userprofil)
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

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE)  as NotificationManager

            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotification() {

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val broadcastIntent: Intent = Intent(this, NotificationReceiver:: class.java)
        broadcastIntent.putExtra("toastMessage","Selamat Datang " + binding.etUsername.text.toString())
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val bigPictureBitmap = ContextCompat.getDrawable(this, R.drawable.profile)?.toBitmap()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setSound(defaultSoundUri)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bigPictureBitmap)
            )
            .setContentTitle(binding.etUsername.text.toString())
            .setContentText("Berhasil Sign Up Account")
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.BLUE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_reply_24, "Reply", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build())
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