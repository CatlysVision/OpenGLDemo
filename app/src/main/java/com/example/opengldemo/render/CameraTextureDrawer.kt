package com.example.opengldemo.render

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLES20.*
import com.example.opengldemo.buildProgram
import com.example.opengldemo.toFloatBuffer
import javax.microedition.khronos.opengles.GL10

class CameraTextureDrawer(val context: Context, OESTextureId: Int) {

    private var mOESTextureId = OESTextureId

    private var mProgram: Int = 0

    private var aPositionLocation = -1
    private var aTextureCoordLocation = -1
    private var uTextureMatrixLocation = -1
    private var uTextureSamplerLocation = -1

    private val transformMatrix = FloatArray(16)

    private val vertexData = floatArrayOf(
        1f, 1f,
        -1f, 1f,
        -1f, -1f,
        1f, 1f,
        -1f, -1f,
        1f, -1f
    )
    private val vertexBuffer = vertexData.toFloatBuffer()

    private val textureData = floatArrayOf(
        1f, 1f,
        0f, 1f,
        0f, 0f,
        1f, 1f,
        0f, 0f,
        1f, 0f
    )
    private val textureBuffer = textureData.toFloatBuffer()

    private val POSITION_ATTRIBUTE = "aPosition"
    private val TEXTURE_MATRIX_UNIFORM = "uTextureMatrix"
    private val TEXTURE_COORD_ATTRIBUTE = "aTextureCoordinate"
    private val TEXTURE_SAMPLER_UNIFORM = "uTextureSampler"

    init {
        mProgram = buildProgram(context, "camera_vertex.glsl", "camera_fragment_f5.glsl")
        glUseProgram(mProgram)
    }

    fun onCreate() {
        aPositionLocation = glGetAttribLocation(mProgram, POSITION_ATTRIBUTE)
        aTextureCoordLocation = glGetAttribLocation(mProgram, TEXTURE_COORD_ATTRIBUTE)
        uTextureMatrixLocation = glGetUniformLocation(mProgram, TEXTURE_MATRIX_UNIFORM)
        uTextureSamplerLocation = glGetUniformLocation(mProgram, TEXTURE_SAMPLER_UNIFORM)
    }

    fun onSurfaceChanged(width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    fun onDrawFrame(surfaceTexture: SurfaceTexture) {
        glClear(GL_COLOR_BUFFER_BIT)

        surfaceTexture.updateTexImage()
        surfaceTexture.getTransformMatrix(transformMatrix)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mOESTextureId)
        glUniform1i(uTextureSamplerLocation, 0)
        glUniformMatrix4fv(uTextureMatrixLocation, 1, false, transformMatrix, 0)

        glEnableVertexAttribArray(aPositionLocation)
        glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 8, vertexBuffer)

        glEnableVertexAttribArray(aTextureCoordLocation)
        glVertexAttribPointer(aTextureCoordLocation, 2, GL_FLOAT, false, 8, textureBuffer)

        glDrawArrays(GL_TRIANGLES, 0, 6)
    }

    companion object {

        fun createOESTextureObject(): Int {
            val tex = IntArray(1)
            glGenTextures(1, tex, 0)
            glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, tex[0])
            glTexParameterf(
                GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST.toFloat()
            )
            glTexParameterf(
                GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR.toFloat()
            )
            glTexParameterf(
                GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE.toFloat()
            )
            glTexParameterf(
                GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE.toFloat()
            )
            glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
            return tex[0]
        }

    }

}