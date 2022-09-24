package com.example.ugd.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd.R
import com.example.ugd.RVDonaturAdapter
import com.example.ugd.entity.Donatur

class FragmentDonasi : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_donasi,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)

        val rvDonasi : RecyclerView = view.findViewById(R.id.rv_donasi)
        rvDonasi.layoutManager = layoutManager
        rvDonasi.setHasFixedSize(true)
    }
}