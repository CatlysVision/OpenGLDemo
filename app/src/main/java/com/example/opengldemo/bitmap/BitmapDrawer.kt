package com.example.opengldemo.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLES20.*
import android.opengl.GLUtils
import android.opengl.Matrix
import android.util.Log
import com.example.opengldemo.buildProgram
import com.example.opengldemo.toFloatBuffer
import java.nio.FloatBuffer

class BitmapDrawer(private val context: Context, private val mBitmap: Bitmap) {

    private val mVertexCoors = floatArrayOf(
        -1f, -1f,
        1f, -1f,
        -1f, 1f,
        1f, 1f
    )

    private val mTextureCoors = floatArrayOf(
        0f, 1f,
        1f, 1f,
        0f, 0f,
        1f, 0f
    )

    private var mTextureId = -1

    private var mProgram = -1

    private var mVertexPosHandler = -1

    private var mTexturePosHandler = -1

    private var mTextureHandler = -1

    private var uProjectionMatrixAttr = 0

    val mProjectionMatrix = FloatArray(16)

    private var mVertexBuffer: FloatBuffer

    private var mTextureBuffer: FloatBuffer

    init {
        mVertexBuffer = mVertexCoors.toFloatBuffer()
        mTextureBuffer = mTextureCoors.toFloatBuffer()
    }

    fun setTextureID(id: Int) {
        mTextureId = id
    }

    fun draw() {
        if (mTextureId != -1) {
            createGLProgram()
            activateTexture()
            bindBitmapToTexture()
            doDraw()
        }
    }

    fun onSurfaceChanged(width: Int, height: Int) {
        val aspectRatio =
            if (width > height) width.toFloat() / height.toFloat() else height.toFloat() / width.toFloat()
        if (width > height) {
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 0f, 10f)
        } else {
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, 0f, 10f)
        }
    }

    private fun createGLProgram() {
        if (mProgram == -1) {
            mProgram = buildProgram(context, "bitmap_vertext.glsl", "bitmap_fragment.glsl")

            mVertexPosHandler = glGetAttribLocation(mProgram, "aPosition")
            mTexturePosHandler = glGetAttribLocation(mProgram, "aCoordinate")
            mTextureHandler = glGetUniformLocation(mProgram, "uTexture")
            uProjectionMatrixAttr = glGetUniformLocation(mProgram, "uProjectionMatrix")
        }
        glUseProgram(mProgram)
    }

    private fun activateTexture() {
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, mTextureId)
        glUniform1i(mTextureHandler, 0)
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR.toFloat())
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR.toFloat())
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
    }

    private fun bindBitmapToTexture() {
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, mBitmap, 0)
    }

    private fun doDraw() {
        glEnableVertexAttribArray(mVertexPosHandler)
        glEnableVertexAttribArray(mTexturePosHandler)
        glUniformMatrix4fv(uProjectionMatrixAttr, 1, false, mProjectionMatrix, 0)
        glVertexAttribPointer(mVertexPosHandler, 2, GL_FLOAT, false, 0, mVertexBuffer)
        glVertexAttribPointer(mTexturePosHandler, 2, GL_FLOAT, false, 0, mTextureBuffer)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)
    }

    fun release() {
        glDisableVertexAttribArray(mVertexPosHandler)
        glDisableVertexAttribArray(mTexturePosHandler)
        glBindTexture(GL_TEXTURE_2D, 0)
        glDeleteTextures(1, intArrayOf(mTextureId), 0)
        glDeleteProgram(mProgram)
    }

}