package com.example.ugd

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd.Activity.HomeActivity
import com.example.ugd.databinding.FragmentEditProfilBinding
import com.example.ugd.room.User
import com.example.ugd.room.UserDB
import com.example.ugd.FragmentProfile
import com.example.ugd.api.PenggunaAPI
import com.example.ugd.models.Pengguna
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets


class FragmentEditProfil : Fragment() {

    private var _binding: FragmentEditProfilBinding? = null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null
    private var layoutLoading: LinearLayout? = null

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfilBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        queue = Volley.newRequestQueue(activity)
        layoutLoading = view.findViewById(R.id.layout_loading)
        sharedPreferences = (activity as HomeActivity).getSharedPreferences("login", Context.MODE_PRIVATE)
        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()
        val id = sharedPreferences.getInt("id",0)
        val pass = sharedPreferences.getString("pass",null)

        var check = true

        binding.btnUpdate.setOnClickListener {
            if(binding.updateUsername.text.toString().isEmpty()) {
                binding.updateUsername.setError("Kosong")
                check =false
            }
            if (binding.updatePassword.text.toString().isEmpty()){
                binding.updatePassword.setError("Kosong")
                check =false
            }
            if(binding.updateEmail.text.toString().isEmpty()) {
                binding.updateEmail.setError("Kosong")
                check =false
            }
            if(binding.updateTanggal.text.toString().isEmpty()) {
                binding.updateTanggal.setError("Kosong")
                check =false
            }
            if(binding.updatePhone.text.toString().isEmpty()) {
                binding.updatePhone.setError("Kosong")
                check =false
            }
            if(!check) {
                check = true
                return@setOnClickListener
            }else {
//                updateData()
                if(pass!=null){
                    updateUser(id,pass)
                }
                transitionFragment(FragmentProfile())
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateUser(id: Int, pass: String) {
        setLoading(true)

        val user= Pengguna(
            id,
            binding.updateUsername.text.toString(),
            binding.updateEmail.text.toString(),
            binding.updateTanggal.text.toString(),
            binding.updatePhone.text.toString(),
            pass
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.PUT, PenggunaAPI.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()
                var user = gson.fromJson(response, Pengguna::class.java)

                if(user != null) {
                    var resJO = JSONObject(response.toString())
                    val  userobj = resJO.getJSONObject("data")

                    sharedPreferences.edit()
                        .putInt("id",userobj.getInt("id"))
                        .putString("nama",userobj.getString("username"))
                        .putString("pass",userobj.getString("password"))
                        .apply()
//                    Toast.makeText(activity, "User Berhasil Diupdate", Toast.LENGTH_SHORT).show()

                    MotionToast.darkColorToast(
                        context as Activity,"Notification Profil!",
                        "Data user berhasil diperbarui!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                }
                setLoading(false)

            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(
//                        activity,
//                        errors.getString("message"),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    MotionToast.darkColorToast(
                        context as Activity,"Notification Profil!",
                        errors.getString("message"),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch (e: Exception) {
//                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    MotionToast.darkColorToast(
                        context as Activity,"Notification Profil!",
                        e.message.toString(),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
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
                    val requestBody = gson.toJson(user)
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
            activity?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        } else {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }

    private fun updateData() {
        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()

        val db by lazy { UserDB(activity as HomeActivity) }
        val userDao = db.userDao()

        val id = sharedPreferences.getInt("id", 0)

        val getUser = userDao.getUser(id)

        val user = User(id,
            binding.updateUsername.text.toString(),
            binding.updatePassword.text.toString(),
            binding.updateEmail.text.toString(),
            binding.updateTanggal.text.toString(),
            binding.updatePhone.text.toString(),
        )
        userDao.updateUser(user)
    }

    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.layout_fragment, fragment)
            .addToBackStack(null).commit()
        transition.hide(FragmentEditProfil())
    }

}