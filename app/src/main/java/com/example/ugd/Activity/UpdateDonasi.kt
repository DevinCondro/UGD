package com.example.ugd.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
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
import com.itextpdf.barcodes.BarcodeQRCode
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import kotlinx.android.synthetic.main.activity_update_donasi.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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
        binding = ActivityUpdateDonasiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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
            btnSave.setOnClickListener {
                val judul = etJudul!!.text.toString()
                val deskripsi = etDeskripsi!!.text.toString()
                val nominal = etNominal!!.text.toString()
                val penggalang = etPenggalang!!.text.toString()
                val pembayaran = cara!!.text.toString()
                val daerah = daerah!!.text.toString()
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                            createPdf(judul, deskripsi, nominal, penggalang, pembayaran, daerah)
                            createDonasi()
                    }
                } catch (e: FileNotFoundException){
                    e.printStackTrace()
                }
            }
        } else {
            tvTitle.setText("Edit Donasi")
            getDonasiById(id)

            btnSave.setOnClickListener { updateDonasi(id) }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Throws(
        FileNotFoundException::class
    )
    private fun createPdf(judul: String, deskripsi: String, nominal: String, penggalang: String, pembayaran: String, daerah: String) {
        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file = File(pdfPath, "donasi.pdf")
        FileOutputStream(file)

        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)
        pdfDocument.defaultPageSize = PageSize.A4
        document.setMargins(5f, 5f, 5f, 5f)
        @SuppressLint("UseCompatLoadingForDrawables") val d = getDrawable(R.drawable.logo)


        val bitmap = (d as BitmapDrawable?)!!.bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bitmapData = stream.toByteArray()
        val imageData = ImageDataFactory.create(bitmapData)
        val image = Image(imageData)
        val namapengguna = Paragraph("Identitas User").setBold().setFontSize(24f)
            .setTextAlignment(TextAlignment.CENTER)
        val group = Paragraph(
            """
                          Berikut adalah
                          Profile User Aplikasi AyoPeduli 
                          """.trimIndent()).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)
        val width = floatArrayOf(100f, 100f)
        val table = Table(width)
        table.setHorizontalAlignment(HorizontalAlignment.CENTER)
        table.addCell(Cell().add(Paragraph("Judul Donasi")))
        table.addCell(Cell().add(Paragraph(judul)))
        table.addCell(Cell().add(Paragraph("Deskripsi")))
        table.addCell(Cell().add(Paragraph(deskripsi)))
        table.addCell(Cell().add(Paragraph("Target Nominal")))
        table.addCell(Cell().add(Paragraph(nominal)))
        table.addCell(Cell().add(Paragraph("Nama Penggalang")))
        table.addCell(Cell().add(Paragraph(penggalang)))
        table.addCell(Cell().add(Paragraph("Cara Pembayaran")))
        table.addCell(Cell().add(Paragraph(pembayaran)))
        table.addCell(Cell().add(Paragraph("Daerah Donasi")))
        table.addCell(Cell().add(Paragraph(daerah)))
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        table.addCell(Cell().add(Paragraph("Tanggal Buat PDF")))
        table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a")
        table.addCell(Cell().add(Paragraph("Pukul Pembuatan")))
        table.addCell(Cell().add(Paragraph(LocalTime.now().format(timeFormatter))))

        val barcodeQRCode = BarcodeQRCode(
            """
                                      $judul
                                      $deskripsi
                                      $nominal
                                      $penggalang
                                      $pembayaran
                                      $daerah
                                      ${LocalDate.now().format(dateTimeFormatter)}
                                      ${LocalTime.now().format(timeFormatter)}
                                      """.trimIndent())
        val qrCodeObject = barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument)
        val qrCodeImage = Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(
            HorizontalAlignment.CENTER)

        document.add(image)
        document.add(namapengguna)
        document.add(group)
        document.add(table)
        document.add(qrCodeImage)

        document.close()
        MotionToast.createToast(this,
            "Hurray success 😍",
            "Upload Completed successfully!",
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this,R.font.bebas_neue_bold_700))
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

        if (etJudul!!.text.toString().isEmpty()){
            Toast.makeText(this@UpdateDonasi, "Judul Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
        }
        else if (etDeskripsi!!.text.toString().isEmpty()){
            Toast.makeText(this@UpdateDonasi, "Deskripsi Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
        }
        else if (etNominal!!.text.toString().isEmpty()){
            Toast.makeText(this@UpdateDonasi, "Target Nominal Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
        }
        else if (etPenggalang!!.text.toString().isEmpty()){
            Toast.makeText(this@UpdateDonasi, "Nama Penggalang Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
        }
        else if (cara!!.text.toString().isEmpty()){
            Toast.makeText(this@UpdateDonasi, "Cara Pembayaran Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
        }
        else if (daerah!!.text.toString().isEmpty()){
            Toast.makeText(this@UpdateDonasi, "Lokasi Daerah Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
        }
        else{
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
        setLoading(false)
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