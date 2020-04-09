package com.example.opengldemo.filter

import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLES20.*
import com.example.opengldemo.manager.ShaderParam
import com.example.opengldemo.toFloatBuffer
import java.lang.reflect.MalformedParameterizedTypeException

abstract class BaseFilter(val OESTextureId: Int) {

    protected val transformMatrix = FloatArray(16)

    private val vertexData = floatArrayOf(
        -1f, -1f,
        1f, -1f,
        -1f, 1f,
        1f, 1f
    )
    protected val vertexBuffer = vertexData.toFloatBuffer()

    private val textureData = floatArrayOf(
        0f, 0f,
        1f, 0f,
        0f, 1f,
        1f, 1f
    )

    protected val textureBuffer = textureData.toFloatBuffer()

    protected var mProgram: Int = 0

    protected var aPositionLocation = -1
    protected var aTextureCoordLocation = -1
    protected var uTextureMatrixLocation = -1
    protected var uTextureSamplerLocation = -1

    protected var mWidth = 0
    protected var mHeight = 0

    open fun onCreate(width: Int, height: Int) {
        val shaderParam = getParam()
        mProgram = shaderParam.program
        glUseProgram(mProgram)
        aPositionLocation = shaderParam.positionLocation
        aTextureCoordLocation = shaderParam.textureCoordLocation
        uTextureMatrixLocation = shaderParam.textureMatrixLocation
        uTextureSamplerLocation = shaderParam.textureSampler
        mWidth = width
        mHeight = height
    }

    protected abstract fun getParam(): ShaderParam

    open fun onDrawFrame(surfaceTexture: SurfaceTexture) {
        glClear(GL_COLOR_BUFFER_BIT)
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f)
        surfaceTexture.updateTexImage()
        surfaceTexture.getTransformMatrix(transformMatrix)
        doDraw(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, OESTextureId)
    }

    open fun doDraw(target: Int, textureId: Int) {
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(target, textureId)
        glUniformMatrix4fv(uTextureMatrixLocation, 1, false, transformMatrix, 0)

        glEnableVertexAttribArray(aPositionLocation)
        glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 0, vertexBuffer)

        glEnableVertexAttribArray(aTextureCoordLocation)
        glVertexAttribPointer(
            aTextureCoordLocation,
            2,
            GL_FLOAT,
            false,
            0,
            textureBuffer
        )
        glUniform1i(uTextureSamplerLocation, 0)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)
        glBindTexture(target, 0)
    }

}