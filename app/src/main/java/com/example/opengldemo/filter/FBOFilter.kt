package com.example.opengldemo.filter

import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES20
import com.example.opengldemo.bindFBO
import com.example.opengldemo.createFBOTexture
import com.example.opengldemo.createFrameBuffer
import com.example.opengldemo.manager.SHADER_BASE
import com.example.opengldemo.manager.ShaderParam
import com.example.opengldemo.manager.getParamByType

class FBOFilter(id: Int) : BaseFilter(id) {

    override fun getParam(): ShaderParam {
        return getParamByType(SHADER_BASE)
    }

    private var mFrameBuffer: Int = -1

    private var mFBOTextureId: Int = -1

    override fun onCreate(width: Int, height: Int) {
        super.onCreate(width, height)
        mFrameBuffer = createFrameBuffer()
        mFBOTextureId = createFBOTexture(width, height)
        bindFBO(mFrameBuffer, mFBOTextureId)
    }

    override fun onDrawFrame(surfaceTexture: SurfaceTexture) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        surfaceTexture.updateTexImage()
        surfaceTexture.getTransformMatrix(transformMatrix)

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffer)
        doDraw(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, OESTextureId)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        doDraw(GLES20.GL_TEXTURE_2D, mFBOTextureId)
    }


}