package com.example.opengldemo.filter

import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.Matrix
import com.example.opengldemo.manager.SHADER_SOUL
import com.example.opengldemo.manager.ShaderParam
import com.example.opengldemo.manager.getParamByType


class SoulFilter(id: Int) : BaseFilter(id) {

    protected var uAlphaLocation = -1
    protected var uMVPMatrixLocation = -1

    //当前动画进度
    private var mProgress = 0.0f
    //当前地帧数
    private var mFrames = 0
    //动画最大帧数
    private val mMaxFrames = 15
    //动画完成后跳过的帧数
    private val mSkipFrames = 8
    //放大矩阵
    private val mMvpMatrix = FloatArray(16)

    override fun getParam(): ShaderParam {
        return getParamByType(SHADER_SOUL)
    }

    override fun onCreate(width: Int, height: Int) {
        super.onCreate(width, height)
        uAlphaLocation = GLES20.glGetUniformLocation(mProgram, "uAlpha")
        uMVPMatrixLocation = GLES20.glGetUniformLocation(mProgram, "uMvpMatrix")
    }

    override fun onDrawFrame(surfaceTexture: SurfaceTexture) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.0f)
        surfaceTexture.updateTexImage()
        surfaceTexture.getTransformMatrix(transformMatrix)

        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_ALPHA)
        mProgress = mFrames.toFloat() / mMaxFrames
        if (mProgress > 1f) {
            mProgress = 0f
        }
        mFrames++
        if (mFrames > mMaxFrames + mSkipFrames) {
            mFrames = 0
        }
        Matrix.setIdentityM(mMvpMatrix, 0) //初始化矩阵

        GLES20.glUniformMatrix4fv(uMVPMatrixLocation, 1, false, mMvpMatrix, 0)
        var backAlpha = 1f
        var alpha = 0.0f
        if (mProgress > 0f) {
            alpha = 0.2f - mProgress * 0.2f
            backAlpha = 1 - alpha
        }
        GLES20.glUniform1f(uAlphaLocation, backAlpha)

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, OESTextureId)
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
        GLES20.glUniform1i(uTextureSamplerLocation, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        if (mProgress > 0f) { //这里绘制放大图层
            GLES20.glUniform1f(uAlphaLocation, alpha)
            val scale = 1.0f + 1f * mProgress
            Matrix.scaleM(mMvpMatrix, 0, scale, scale, scale)
            GLES20.glUniformMatrix4fv(uMVPMatrixLocation, 1, false, mMvpMatrix, 0)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        }

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
        GLES20.glDisable(GLES20.GL_BLEND)
    }

}