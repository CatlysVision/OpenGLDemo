package com.example.opengldemo.render

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import com.example.opengldemo.shape.BaseShape
import com.example.opengldemo.shape.Triangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ShapeRender(private val context: Context) : GLSurfaceView.Renderer {

    private lateinit var shape: Triangle

    override fun onDrawFrame(gl: GL10?) {
        shape.onDrawFrame(gl)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        shape.onSurfaceChanged(gl, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        shape = Triangle(context)
        shape.onSurfaceCreated(gl, config)
    }


}