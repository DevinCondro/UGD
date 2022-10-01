package com.example.ugd.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ugd.Activity.HomeActivity
import com.example.ugd.R
import com.example.ugd.room.User
import com.example.ugd.room.UserDB
import com.example.ugd.databinding.FragmentEditProfileBinding

class FragmentEditProfile : Fragment (){

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                updateData()
                transitionFragment(FragmentProfile())
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
            binding.updatePhone.text.toString()
        )
        userDao.updateUser(user)
    }

    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.layout_fragment, fragment)
            .addToBackStack(null).commit()
        transition.hide(FragmentEditProfile())
    }
}