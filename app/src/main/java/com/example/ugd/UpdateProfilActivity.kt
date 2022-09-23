package com.example.ugd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.ugd.room.Constant
import com.example.ugd.room.User
import com.example.ugd.room.UserDB
import kotlinx.android.synthetic.main.activity_update_profil.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UpdateProfilActivity : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profil)
        setupView()
        setupListener()
        Toast.makeText(this,userId.toString(), Toast.LENGTH_SHORT).show()
    }
    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
            Constant.TYPE_READ -> {
                getNote()
            }
        }
    }
    private fun setupListener() {
        btnUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.userDao().updateUser(
                    User(userId, updateUsername.text.toString(),
                        updatePassword.text.toString(), updateEmail.text.toString(),
                        updateTanggal.text.toString(), updatePhone.text.toString())
                )
                finish()
            }
        }
    }
    fun getNote() {
        userId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val users = db.userDao().getUser(userId)[0]
            updateUsername.setText(users.username)
            updatePassword.setText(users.password)
            updateEmail.setText(users.email)
            updateTanggal.setText(users.tanggal)
            updatePhone.setText(users.telp)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}