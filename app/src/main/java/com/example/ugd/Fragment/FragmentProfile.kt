package com.example.ugd.Fragment

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd.R
import com.example.ugd.RVDonaturAdapter
import com.example.ugd.entity.Donatur
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FragmentProfile : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile ,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLogout: Button = view.findViewById(R.id.btnLogout)

        btnLogout.setOnClickListener {
            activity?.let { it ->
                MaterialAlertDialogBuilder(it)
                    .setTitle("Apakah anda ingin keluar?")
                    .setNegativeButton("No") {dialog, which ->

                    }
                    .setPositiveButton("yes") {dialog, which ->
                        activity?.finish()
                    }
                    .show()
            }
        }
    }
}