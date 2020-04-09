package com.example.opengldemo.shape

import android.content.Context
import android.opengl.GLES20
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

abstract class BaseShape {

    var mProgram: Int = 0

    val mMVPMatrix = FloatArray(16)
    val mModelMatrix = FloatArray(16)
    val mViewMatrix = FloatArray(16)
    val mProjectionMatrix = FloatArray(16)

    open fun onDrawFrame(gl: GL10?) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)
    }

    abstract fun onSurfaceChanged(gl: GL10?, width: Int, height: Int)

    abstract fun onSurfaceCreated(gl: GL10?, config: EGLConfig?)
}