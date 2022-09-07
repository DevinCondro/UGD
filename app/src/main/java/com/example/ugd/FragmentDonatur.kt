package com.example.ugd

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd.entity.Donatur

class FragmentDonatur : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_donatur,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter : RVDonaturAdapter = RVDonaturAdapter(Donatur.listOfDonatur)

        val rvDonasi : RecyclerView = view.findViewById(R.id.rv_donatur)
        rvDonasi.layoutManager = layoutManager
        rvDonasi.setHasFixedSize(true)
        rvDonasi.adapter = adapter
    }
}