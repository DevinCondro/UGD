package com.example.ugd.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd.R
import com.example.ugd.api.DonasiAPI
import com.example.ugd.databinding.ActivityUpdateDonasiBinding
import com.example.ugd.models.DonasiUser
import com.example.ugd.room.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_update_donasi.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class UpdateDonasi : AppCompatActivity() {

    companion object{
        private val PEMBAYARAN_LIST = arrayOf("Cash", "Transfer Bank", "E-Wallet", "Barang", "Kartu Kredit", "PayPal", "QR Code", "Rekening Bersama")
        private val DAERAH_LIST = arrayOf(
            "DKI Jakarta",
            "Jawa Barat",
            "Banten",
            "Jawa Tengah",
            "Bali",
            "Sulawesi Utara",
            "Sulawesi Barat",
            "Sulawesi Tengah",
            "Kalimantan Barat",
            "Kalimantan Selatan",
            "Papua",
            "Nusa Tenggara Timur",
            "Nusa Tenggara Barat",
            "D.I Yogyakarta",
            "Jawa Timur")
    }
    private lateinit var binding: ActivityUpdateDonasiBinding
    private var etJudul: EditText? = null
    private var etDeskripsi: EditText? = null
    private var etNominal: EditText? = null
    private var etPenggalang: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var cara: AutoCompleteTextView? = null
    private var daerah: AutoCompleteTextView? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_donasi)

        queue = Volley.newRequestQueue(this)
        etJudul = findViewById(R.id.editJudul)
        etDeskripsi = findViewById(R.id.editDeskripsi)
        etNominal = findViewById(R.id.edittarget)
        etPenggalang = findViewById(R.id.editPenggalang)
        cara = findViewById(R.id.cara)
        daerah = findViewById(R.id.daerah)
        layoutLoading = findViewById(R.id.layout_loading)

        setExposedDropDownMenu()

        val btnCancel = findViewById<Button>(R.id.btnCancel)
        btnCancel.setOnClickListener{ finish() }
        val btnSave = findViewById<Button>(R.id.btnSave)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getIntExtra("id", -1)
        if (id == -1){
            tvTitle.setText("Tambah Donasi")
            btnSave.setOnClickListener { createDonasi() }
        } else {
            tvTitle.setText("Edit Donasi")
            getDonasiById(id)

            btnSave.setOnClickListener { updateDonasi(id) }
        }
    }

    fun setExposedDropDownMenu(){
        val adapterPembayaran: ArrayAdapter<String> = ArrayAdapter<String>(this,
            R.layout.item_list, PEMBAYARAN_LIST)
        cara!!.setAdapter(adapterPembayaran)

        val adapterDaerah: ArrayAdapter<String> = ArrayAdapter<String>(this,
            R.layout.item_list, DAERAH_LIST)
        daerah!!.setAdapter(adapterDaerah)
    }

    private fun getDonasiById(id: Int){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, DonasiAPI.GET_BY_ID_URL + id, Response.Listener { response ->
//                val gson = Gson()
//                val donasi = gson.fromJson(response, DonasiUser::class.java)

                var jo = JSONObject(response.toString())
                val donasi = jo.getJSONObject("data")


                etJudul!!.setText(donasi.getString("judulDonasi"))
                etDeskripsi!!.setText(donasi.getString("Deskripsi"))
                etNominal!!.setText(donasi.getString("target"))
                etPenggalang!!.setText(donasi.getString("namaPenggalang"))
                cara!!.setText(donasi.getString("caraPembayaran"))
                daerah!!.setText(donasi.getString("daerah"))
                setExposedDropDownMenu()

                Toast.makeText(this@UpdateDonasi, "Data berhasil diambil!", Toast.LENGTH_SHORT).show()
                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)

                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@UpdateDonasi,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@UpdateDonasi, e.message, Toast.LENGTH_SHORT).show()
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

    private fun createDonasi(){
        setLoading(true)

        val donasi = DonasiUser(
            0,
            etJudul!!.text.toString(),
            etDeskripsi!!.text.toString(),
            etNominal!!.text.toString(),
            etPenggalang!!.text.toString(),
            cara!!.text.toString(),
            daerah!!.text.toString()
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, DonasiAPI.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                var donasi = gson.fromJson(response, DonasiUser::class.java)

                if(donasi != null)
                    Toast.makeText(this@UpdateDonasi, "Data berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@UpdateDonasi,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@UpdateDonasi, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(donasi)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }

    private fun updateDonasi(id: Int){
        setLoading(true)

        val donasi = DonasiUser(
            id,
            etJudul!!.text.toString(),
            etDeskripsi!!.text.toString(),
            etNominal!!.text.toString(),
            etPenggalang!!.text.toString(),
            cara!!.text.toString(),
            daerah!!.text.toString()
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.PUT, DonasiAPI.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()
                var donasi = gson.fromJson(response, DonasiUser::class.java)

                if(donasi != null)
                    Toast.makeText(this@UpdateDonasi, "Data berhasil Diupdate", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@UpdateDonasi,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@UpdateDonasi, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(donasi)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
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
//    private var donasiId: Int = 0
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_update_donasi)
//
//        binding = ActivityUpdateDonasiBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
//
//        queue = Volley.newRequestQueue(this)
//        cara = findViewById(R.id.cara)
//        daerah = findViewById(R.id.daerah)
//        setupView()
////        setupListener()
//        setExposedDropDownMenu()
//        btnSave.setOnClickListener { createDonasi() }
//
//        Toast.makeText(this,donasiId.toString(), Toast.LENGTH_SHORT).show()
//    }
//
//    fun setExposedDropDownMenu(){
//        val adapterPembayaran: ArrayAdapter<String> = ArrayAdapter<String>(this,
//            R.layout.item_list, PEMBAYARAN_LIST)
//        cara!!.setAdapter(adapterPembayaran)
//
//        val adapterDaerah: ArrayAdapter<String> = ArrayAdapter<String>(this,
//            R.layout.item_list, DAERAH_LIST)
//        daerah!!.setAdapter(adapterDaerah)
//    }
//
//    fun setupView() {
//        val intentType = intent.getIntExtra("intent_type", 0)
//        when (intentType) {
//            Constant.TYPE_CREATE -> {
//                binding.btnUpdate.visibility = View.GONE
//            }
//            Constant.TYPE_READ -> {
//                binding.btnSave.visibility = View.GONE
//                binding.btnUpdate.visibility = View.GONE
//                getDonasi()
//
//            }
//            Constant.TYPE_UPDATE -> {
//                binding.btnSave.visibility = View.GONE
//                getDonasi()
//            }
//        }
//    }
//    private fun setupListener() {
//        binding.btnSave.setOnClickListener {
//            if(binding.editJudul.text.toString().isEmpty() || binding.editDeskripsi.text.toString().isEmpty() || binding.edittarget.text.toString().isEmpty() || binding.editPenggalang.text.toString().isEmpty() || binding.cara.text.toString().isEmpty() || binding.daerah.toString().isEmpty()) {
//                return@setOnClickListener
//            }
//            CoroutineScope(Dispatchers.IO).launch {
//                run {
//                    db.donasiDao().addDonasi(
//                        Donasi(donasiId, editJudul.text.toString(),
//                            editDeskripsi.text.toString(), edittarget.text.toString(),
//                            editPenggalang.text.toString(), cara!!.text.toString(), daerah!!.text.toString())
//                    )
//                    finish()
//                }
//            }
//        }
//
//        binding.btnUpdate.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                db.donasiDao().updateDonasi(
//                    Donasi(donasiId, editJudul.text.toString(),
//                        editDeskripsi.text.toString(), edittarget.text.toString(),
//                       editPenggalang.text.toString(),cara!!.text.toString(), daerah!!.text.toString())
//                )
//                finish()
//            }
//        }
//    }
//    fun getDonasi() {
//        donasiId = intent.getIntExtra("intent_id", 0)
//        CoroutineScope(Dispatchers.IO).launch {
//            val donasies = db.donasiDao().getDonasi(donasiId)[0]
//            editJudul.setText(donasies.judulDonasi)
//            editDeskripsi.setText(donasies.Deskripsi)
//            edittarget.setText(donasies.target)
//            editPenggalang.setText(donasies.namaPengalang)
//            cara!!.setText(donasies.caraPembayaran)
//            daerah!!.setText(donasies.daerah)
//
////            setExposedDropDownMenu()
//        }
//    }
//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return super.onSupportNavigateUp()
//    }
//
//    private fun createDonasi(){
//        val donasi = com.example.ugd.models.DonasiUser(
//            "saya",
//            editDeskripsi.text.toString(), edittarget.text.toString(),
//            editPenggalang.text.toString(), cara!!.text.toString(), daerah!!.text.toString()
//        )
//
//        val stringRequest: StringRequest =
//            object : StringRequest(Method.POST, DonasiAPI.ADD_URL, Response.Listener { response ->
//                val gson = Gson()
//                var donasi = gson.fromJson(response, com.example.ugd.models.DonasiUser::class.java)
//
//                if(donasi != null)
//                    Toast.makeText(this@UpdateDonasi, "Data berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
//
//                val returnIntent = Intent()
//                setResult(RESULT_OK, returnIntent)
//                finish()
//
////                        setLoading(false)
//            }, Response.ErrorListener { error ->
////                        setLoading(false)
//                try {
//                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
//                    val errors = JSONObject(responseBody)
//                    Toast.makeText(
//                        this@UpdateDonasi,
//                        errors.getString("message"),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }catch (e: Exception){
//                    Toast.makeText(this@UpdateDonasi, e.message, Toast.LENGTH_SHORT).show()
//                }
//            }){
//                @Throws(AuthFailureError::class)
//                override fun getHeaders(): Map<String, String> {
//                    val headers = HashMap<String, String>()
//                    headers["Accept"] = "application/json"
//                    return headers
//                }
//
//                @Throws(AuthFailureError::class)
//                override fun getBody(): ByteArray {
//                    val gson = Gson()
//                    val requestBody = gson.toJson(donasi)
//                    return requestBody.toByteArray(StandardCharsets.UTF_8)
//                }
//
//                override fun getBodyContentType(): String {
//                    return "application/json"
//                }
//            }
//        queue!!.add(stringRequest)
//    }
//
//    private fun setLoading(isLoading: Boolean){
//        if(isLoading){
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//            )
//            layoutLoading!!.visibility = View.VISIBLE
//        } else {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//            layoutLoading!!.visibility = View.INVISIBLE
//        }
//    }
}