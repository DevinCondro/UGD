package com.example.ugd

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ugd.Activity.*
import com.example.ugd.room.UserDB
import kotlinx.android.synthetic.main.fragment_home.*

class FragmentHome : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db by lazy { UserDB(activity as HomeActivity) }
        val userDao = db.userDao()
        val btnLogout : Button = view.findViewById(R.id.btnLogout)
        val btnTambahDonasi: Button = view.findViewById(R.id.btn_tambahDonasi)
        val textNama: TextView = view.findViewById(R.id.textHome)
        val bencana: ImageView = view.findViewById(R.id.bencana)

        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()
        val user = userDao.getUser(sharedPreferences.getInt("id", 0))

        textNama.setText("Hi, " + user.username)

        btnTambahDonasi.setOnClickListener(View.OnClickListener {
            val moveDonasi= Intent(this@FragmentHome.context, DonasiActivity::class.java)
            startActivity(moveDonasi)
        })


        bencana.setOnClickListener(View.OnClickListener {
            val url = "https://picsum.photos/300"
            Glide.with(this)
                .load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(bencana)
        })

        btnLogout.setOnClickListener(View.OnClickListener {
            getActivity()?.let { it1 ->
                AlertDialog.Builder(it1).apply {
                    setTitle("Tolong Konfirmasi")
                    setMessage("Apakah anda yakin ingin keluar?")

                    setPositiveButton("Iya") { _, _ ->
                        val intent = Intent(this@FragmentHome.context, LoginActivity::class.java)
                        intent.putExtra("finish", true)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }

                    setNegativeButton("Tidak"){_, _ ->

                    }
                    setCancelable(true)
                }.create().show()
            }
        })
    }
}