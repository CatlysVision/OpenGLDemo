package com.example.opengldemo

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.Log
import android.view.TextureView
import com.example.opengldemo.render.CameraRenderer

class CameraSurfaceTextureManager(
    val context: Context,
    private val surfaceTexture: SurfaceTexture
) :
    TextureView.SurfaceTextureListener {

    private lateinit var mTextureView: TextureView
    private lateinit var mCameraRenderer: CameraRenderer

    fun bindTextureView(textureView: TextureView) {
        mTextureView = textureView
        mCameraRenderer = CameraRenderer()
        mTextureView.surfaceTextureListener = this
    }

    fun setFilter(type: Int) {
        mCameraRenderer.setFilter(type)
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        Log.d("ymc_test", "onSurfaceChanged width=$width height=$height")
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        val mOESTextureId = createOESTextureObject()
        Log.d("ymc_test", "onSurfaceTextureAvailable width=$width height=$height")
        mCameraRenderer.init(mTextureView, mOESTextureId, context, width, height)
        mCameraRenderer.initOESTexture(surfaceTexture)
    }
}