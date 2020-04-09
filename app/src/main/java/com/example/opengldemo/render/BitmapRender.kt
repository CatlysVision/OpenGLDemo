package com.example.opengldemo.render

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import com.example.opengldemo.bitmap.BitmapDrawer
import com.example.opengldemo.createTextureIds
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class BitmapRender(private val drawer: BitmapDrawer) : GLSurfaceView.Renderer {


    override fun onDrawFrame(gl: GL10?) {
        drawer.draw()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
        drawer.onSurfaceChanged(width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0f, 0f, 0f, 0f)
        glClear(GL_COLOR_BUFFER_BIT)
        drawer.setTextureID(createTextureIds(1)[0])
    }


}