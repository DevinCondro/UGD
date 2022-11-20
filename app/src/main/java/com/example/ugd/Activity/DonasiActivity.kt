package com.example.ugd.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd.DonasiAdapter
import com.example.ugd.R
import com.example.ugd.api.DonasiAPI
import com.example.ugd.databinding.ActivityDonasiBinding
import com.example.ugd.models.DonasiUser
import com.example.ugd.room.Constant
import com.example.ugd.room.Donasi
import com.example.ugd.room.DonasiDB
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_donasi.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class DonasiActivity : AppCompatActivity() {

    private var srDonasi: SwipeRefreshLayout? = null
    private var adapter: DonasiAdapter? = null
    private var svDonasi: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donasi)

        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srDonasi = findViewById(R.id.sr_donasi)
        svDonasi = findViewById(R.id.sv_donasi)

        srDonasi?.setOnRefreshListener ( SwipeRefreshLayout.OnRefreshListener{ allDonasi()} )
        svDonasi?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })

        val btnAdd = findViewById<Button>(R.id.btn_create)
        btnAdd.setOnClickListener{
            val i = Intent(this@DonasiActivity, UpdateDonasi::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvDonasi = findViewById<RecyclerView>(R.id.list_donasi)
        adapter = DonasiAdapter(ArrayList(), this)
        rvDonasi.layoutManager = LinearLayoutManager(this)
        rvDonasi.adapter = adapter
        allDonasi()
    }

    private fun allDonasi(){
        srDonasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, DonasiAPI.GET_ALL_URL, Response.Listener { response ->
//                val gson = Gson()
//                var donasi : Array<DonasiUser> = gson.fromJson(response, Array<DonasiUser>::class.java)

                var jo = JSONObject(response.toString())
                var donasiArray = arrayListOf<DonasiUser>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    var donasi = DonasiUser(
                        jo.getJSONArray("data").getJSONObject(i).getInt("id"),
                        jo.getJSONArray("data").getJSONObject(i).getString("judulDonasi"),
                        jo.getJSONArray("data").getJSONObject(i).getString("Deskripsi"),
                        jo.getJSONArray("data").getJSONObject(i).getString("target"),
                        jo.getJSONArray("data").getJSONObject(i).getString("namaPenggalang"),
                        jo.getJSONArray("data").getJSONObject(i).getString("caraPembayaran"),
                        jo.getJSONArray("data").getJSONObject(i).getString("daerah")

                    )
                    donasiArray.add(donasi)
                }

                var donasi: Array<DonasiUser> = donasiArray.toTypedArray()

                adapter!!.setDonasiList(donasi)
                adapter!!.filter.filter(svDonasi!!.query)
                srDonasi!!.isRefreshing = false

                if (!donasi.isEmpty())
                    Toast.makeText(this@DonasiActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT)
                        .show()
                else
                    Toast.makeText(this@DonasiActivity, "Data Kosong!", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
                srDonasi!!.isRefreshing = false
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@DonasiActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                } catch (e: Exception){
                    Toast.makeText(this@DonasiActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    fun deleteDonasi(id: Int){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, DonasiAPI.DELETE_URL + id, Response.Listener { response ->
                setLoading(false)

                val gson = Gson()
                var donasi = gson.fromJson(response, DonasiUser::class.java)
                if (donasi != null)
                    Toast.makeText(this@DonasiActivity, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                allDonasi()
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@DonasiActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                } catch (e: java.lang.Exception){
                    Toast.makeText(this@DonasiActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allDonasi()
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



//    val db by lazy { DonasiDB(this) }
//    lateinit var donasiAdapter: DonasiAdapter
//    private lateinit var binding: ActivityDonasiBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_donasi)
//        binding = ActivityDonasiBinding.inflate(layoutInflater)
//        val view = binding.root
//
//        setContentView(view)
//
//        setupListener()
//        setupRecyclerView()
//    }
//    private fun setupRecyclerView() {
//        donasiAdapter = DonasiAdapter(arrayListOf(), object :
//            DonasiAdapter.OnAdapterListener {
//            override fun onClick(donasi: Donasi){
//                intentEdit(donasi.id, Constant.TYPE_READ)
//            }
//            override fun onUpdate(donasi: Donasi) {
//                intentEdit(donasi.id, Constant.TYPE_UPDATE)
//            }
//            override fun onDelete(donasi: Donasi) {
//                deleteDialog(donasi)
//            }
//        })
//        list_donasi.apply {
//            layoutManager = LinearLayoutManager(applicationContext)
//            adapter = donasiAdapter
//        }
//    }
//    private fun deleteDialog(donasi: Donasi){
//        val alertDialog = AlertDialog.Builder(this)
//        alertDialog.apply {
//            setTitle("Confirmation")
//            setMessage("Are You Sure to delete this data From ${donasi.judulDonasi}?")
//            setNegativeButton("Cancel", DialogInterface.OnClickListener
//            { dialogInterface, i ->
//                dialogInterface.dismiss()
//            })
//            setPositiveButton("Delete", DialogInterface.OnClickListener
//            { dialogInterface, i ->
//                dialogInterface.dismiss()
//                CoroutineScope(Dispatchers.IO).launch {
//                    db.donasiDao().deleteDonasi(donasi)
//                    loadData()
//                }
//            })
//        }
//        alertDialog.show()
//    }
//    override fun onStart() {
//        super.onStart()
//        loadData()
//    }
//    fun loadData() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val notes = db.donasiDao().getDonasi()
//            Log.d("MainActivity","dbResponse: $notes")
//            withContext(Dispatchers.Main){
//                donasiAdapter.setData( notes )
//            }
//        }
//    }
//    fun setupListener() {
//        binding.btnCreate.setOnClickListener{
//            intentEdit(0,Constant.TYPE_CREATE)
//        }
//    }
//    fun intentEdit(donasiId : Int, intentType: Int){
//        startActivity(
//            Intent(applicationContext, UpdateDonasi::class.java)
//                .putExtra("intent_id", donasiId)
//                .putExtra("intent_type", intentType)
//        )
//    }
}