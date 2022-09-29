package com.example.ugd.Activity

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Icon
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.ugd.PushNotification.NotificationReceiver
import com.example.ugd.R
import com.example.ugd.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class RegisterActivity : AppCompatActivity() {

    private var binding: ActivityRegisterBinding? = null
    private val CHANNEL_ID = "channel_notification"
    private val notificationId = 101
    private val KEY_TEXT_REPLY = "key_text_reply"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        createNotificationChannel()

        binding!!.btnSignUp.setOnClickListener {
            sendNotification()
        }

        setTitle("Register User")

        var btnSignUp = binding!!.btnSignUp
        var btnCancel = binding!!.btnCancel
        var registerUsername = binding!!.etUsername
        var registerPassword = binding!!.etPassword
        var registerEmail = binding!!.etEmail
        var registerTanggal = binding!!.etTanggal
        var registerTelp = binding!!.etPhone

        binding!!.btnSignUp.setOnClickListener (View.OnClickListener{
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
            sendNotification()
            startActivity(moveHome)
        })

        binding!!.btnCancel.setOnClickListener{
            registerUsername.setText("")
            registerPassword.setText("")
            registerEmail.setText("")
            registerTanggal.setText("")
            registerTelp.setText("")

            Snackbar.make(mainLayout, "Text Cleared Success", Snackbar.LENGTH_LONG).show()
        }

    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)


        }
    }

    private fun sendNotification(){
        val intent : Intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val broadcastIntent : Intent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", binding?.etPhone?.setText("Berhasil Sign Up").toString())
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val bigPictureBitmap = ContextCompat.getDrawable(this, R.drawable.profile)?.toBitmap()
        val replyLabel = "Enter your reply here"
        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
            .setLabel(replyLabel)
            .build()

        val resultIntent = Intent(this, RegisterActivity::class.java)

        val resultPendingIntent = PendingIntent.getActivity(
            this,
            0,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val icon = Icon.createWithResource(this@RegisterActivity,
            android.R.drawable.ic_dialog_info)

        val replyAction = Notification.Action.Builder(
            icon,
            "Reply", resultPendingIntent)
            .addRemoteInput(remoteInput)
            .build()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setSound(defaultSoundUri)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bigPictureBitmap)
            )
            .setContentTitle(binding?.etUsername?.text.toString())
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