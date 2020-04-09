package com.example.opengldemo.filter

import android.graphics.SurfaceTexture
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glUniform1f
import com.example.opengldemo.manager.SHADER_6
import com.example.opengldemo.manager.ShaderParam
import com.example.opengldemo.manager.getParamByType


class Filter6(id: Int) : BaseFilter(id) {

    private var uTimeHandle = 0
    private var startTime = 0L

    override fun onCreate(width: Int, height: Int) {
        super.onCreate(width, height)
        startTime = System.currentTimeMillis()
        uTimeHandle = glGetUniformLocation(mProgram, "uTime")
    }

    override fun getParam(): ShaderParam {
        return getParamByType(SHADER_6)
    }

    override fun onDrawFrame(surfaceTexture: SurfaceTexture) {
        super.onDrawFrame(surfaceTexture)
        val value = System.currentTimeMillis() - startTime
        glUniform1f(uTimeHandle, value.toFloat())
    }

}