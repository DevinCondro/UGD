package com.example.ugd

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ugd.Activity.HomeActivity
import com.example.ugd.Camera.CameraMain
import com.example.ugd.R
import com.example.ugd.databinding.FragmentProfileBinding
import com.example.ugd.room.UserDB
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.fragment_profile.*

class FragmentProfile() : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    val db by lazy { UserDB(requireActivity()) }
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()

        binding.btnUpdate.setOnClickListener {
            transitionFragment(FragmentEditProfil())
        }

        logoProfile.setOnClickListener {
            requireActivity().run {
                val intent = Intent(this, CameraMain::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setData() {
        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()

        val db by lazy { UserDB(activity as HomeActivity) }
        val userDao = db.userDao()

        val user = userDao.getUser(sharedPreferences.getInt("id", 0))
        binding.viewUsername.setText(user.username)
        binding.viewTanggal.setText(user.tanggal)
        binding.viewEmail.setText(user.Email)
        binding.viewNomorTelepon.setText(user.nomorTelepon)
    }

    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.layout_fragment, fragment)
            .addToBackStack(null).commit()
        transition.hide(FragmentProfile())
    }

}