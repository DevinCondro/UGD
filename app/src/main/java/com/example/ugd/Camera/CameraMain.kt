package com.example.ugd.Camera

import android.annotation.SuppressLint
import android.hardware.Camera
import android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import com.example.ugd.FragmentEditProfil
import com.example.ugd.R

class CameraMain : AppCompatActivity() {
    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_camera)

        try {
            mCamera = Camera.open(CAMERA_FACING_FRONT)
        }catch (e: Exception){
            Log.d("Error", "Failed to get Camera" + e.message)
        }

        if(mCamera!=null){
            mCameraView = CameraView(this, mCamera!!)
            val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
            camera_view.addView(mCameraView)
        }

        @SuppressLint("MissingInflatedId", "LocalSuppress") val imageClose =
            findViewById<View>(R.id.btnClose) as ImageButton
        imageClose.setOnClickListener{view: View? -> System.exit(0)}
    }
}