package com.example.opengldemo.filter

import android.graphics.SurfaceTexture
import android.opengl.GLES11
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.Matrix
import com.example.opengldemo.bindFBO
import com.example.opengldemo.createFBOTexture
import com.example.opengldemo.createFrameBuffer
import com.example.opengldemo.manager.SHADER_FBO
import com.example.opengldemo.manager.ShaderParam
import com.example.opengldemo.manager.getParamByType
import com.example.opengldemo.toFloatBuffer

class SoulFBOFilter(id: Int) : BaseFilter(id) {

    private val mReserveVertexCoors = floatArrayOf(
        -1f, 1f,
        1f, 1f,
        -1f, -1f,
        1f, -1f
    )
    private val reserveBuffer = mReserveVertexCoors.toFloatBuffer()

    private var mVertexCoors = vertexBuffer

    override fun getParam(): ShaderParam {
        return getParamByType(SHADER_FBO)
    }

    private var mAlphaHandler: Int = -1
    // 灵魂帧缓冲
    private var mSoulFrameBuffer: Int = -1
    // 灵魂纹理ID
    private var mSoulTextureId: Int = -1
    // 灵魂纹理接收者
    private var mSoulTextureHandler: Int = -1
    // 灵魂缩放进度接收者
    private var mProgressHandler: Int = -1
    // 是否更新FBO纹理
    private var mDrawFbo: Int = 1
    // 更新FBO标记接收者
    private var mDrawFobHandler: Int = -1
    // 一帧灵魂的时间
    private var mModifyTime: Long = -1

    override fun onCreate(width: Int, height: Int) {
        super.onCreate(width, height)
        mSoulFrameBuffer = createFrameBuffer()
        mSoulTextureId = createFBOTexture(width, height)
        bindFBO(mSoulFrameBuffer, mSoulTextureId)

        mAlphaHandler = GLES20.glGetAttribLocation(mProgram, "alpha")
        mSoulTextureHandler = GLES20.glGetUniformLocation(mProgram, "uSoulTexture")
        mProgressHandler = GLES20.glGetUniformLocation(mProgram, "progress")
        mDrawFobHandler = GLES20.glGetUniformLocation(mProgram, "drawFbo")
    }

    override fun onDrawFrame(surfaceTexture: SurfaceTexture) {
        GLES20.glViewport(0, 0, mWidth, mHeight)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        //surfaceTexture.updateTexImage()
        surfaceTexture.getTransformMatrix(transformMatrix)

        updateFBO(surfaceTexture)
        activateSoulTexture()
        activateDefTexture()
        surfaceTexture.updateTexImage()
        soulDraw()
        //GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mSoulFrameBuffer)
        //doDraw(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, OESTextureId)
        //GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)

        //doDraw(GLES20.GL_TEXTURE_2D, mSoulTextureId)
    }

    private fun updateFBO(surfaceTexture: SurfaceTexture) {
        if (System.currentTimeMillis() - mModifyTime > 800) {
            mModifyTime = System.currentTimeMillis()
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mSoulFrameBuffer)
            configFboViewport()
            activateDefTexture()
            surfaceTexture.updateTexImage()
            soulDraw()
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
            configDefViewport(surfaceTexture)
        }
    }

    private fun soulDraw() {
        GLES20.glUniformMatrix4fv(uTextureMatrixLocation, 1, false, transformMatrix, 0)
        GLES20.glEnableVertexAttribArray(aPositionLocation)
        GLES20.glVertexAttribPointer(aPositionLocation, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer)

        GLES20.glEnableVertexAttribArray(aTextureCoordLocation)
        GLES20.glVertexAttribPointer(
            aTextureCoordLocation,
            2,
            GLES20.GL_FLOAT,
            false,
            0,
            textureBuffer
        )

        GLES20.glUniform1f(mProgressHandler, (System.currentTimeMillis() - mModifyTime) / 500f)
        GLES20.glUniform1i(mDrawFobHandler, mDrawFbo)
        GLES20.glVertexAttrib1f(mAlphaHandler, 1.0f)

        //GLES20.glUniform1i(uTextureSamplerLocation, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    private fun configDefViewport(surfaceTexture: SurfaceTexture) {
        mDrawFbo = 0
        mVertexCoors = vertexBuffer
        surfaceTexture.getTransformMatrix(transformMatrix)
        GLES20.glViewport(0, 0, mWidth, mHeight)
    }

    private fun configFboViewport() {
        mDrawFbo = 1
        Matrix.setIdentityM(transformMatrix, 0)
        mVertexCoors = reserveBuffer
        GLES20.glViewport(0, 0, mWidth, mHeight)
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
    }

    private fun activateDefTexture() {
        activateTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, OESTextureId, 0, uTextureSamplerLocation)
    }

    private fun activateSoulTexture() {
        activateTexture(GLES11.GL_TEXTURE_2D, mSoulTextureId, 1, mSoulTextureHandler)
    }

    private fun activateTexture(type: Int, textureId: Int, index: Int, textureHandler: Int) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + index)
        GLES20.glBindTexture(type, textureId)
        GLES20.glUniform1i(textureHandler, index)
        GLES20.glTexParameterf(type, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR.toFloat())
        GLES20.glTexParameterf(type, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR.toFloat())
        GLES20.glTexParameteri(type, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(type, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
    }
}