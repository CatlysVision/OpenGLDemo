package com.example.opengldemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val glSurfaceView = BitmapGLSurfaceView(this)
        setContentView(glSurfaceView)
    }
}
