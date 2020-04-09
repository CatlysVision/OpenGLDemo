package com.example.opengldemo

import android.content.Context
import android.graphics.Matrix
import android.util.Size
import android.view.Display
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import java.lang.ref.WeakReference
import java.util.*

class AutoFitCameraView private constructor(
    config: PreviewConfig,
    viewFinderRef: WeakReference<TextureView>,
    val context: Context
) {

    val useCase: Preview

    private var bufferRotation: Int = 0
    private var viewFinderRotation: Int? = null
    private var bufferDimens: Size = Size(0, 0)
    private var viewFinderDimens: Size = Size(0, 0)

    init {
        val viewFinder = viewFinderRef.get() ?: throw IllegalArgumentException("error")
        viewFinderRotation = getDisplaySurfaceRotation(viewFinder.display) ?: 0

        useCase = Preview(config)

        useCase.setOnPreviewOutputUpdateListener {
            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder)
            //viewFinder.surfaceTexture = it.surfaceTexture

            val cameraSurfaceTextureManager = CameraSurfaceTextureManager(context, it.surfaceTexture)
            cameraSurfaceTextureManager.bindTextureView(viewFinder)

            bufferRotation = it.rotationDegrees
            val rotation = getDisplaySurfaceRotation(viewFinder.display)
            updateTransform(viewFinder, rotation, it.textureSize, viewFinderDimens)
        }

        viewFinder.addOnLayoutChangeListener { view, left, top, right, bottom, _, _, _, _ ->
            val viewFinderTmp = view as TextureView
            val newViewFinderDimens = Size(right - left, bottom - top)
            val rotation = getDisplaySurfaceRotation(viewFinderTmp.display)
            updateTransform(viewFinderTmp, rotation, bufferDimens, newViewFinderDimens)
        }
    }

    private fun updateTransform(
        textureView: TextureView?,
        rotation: Int?,
        newBufferDimens: Size,
        newViewFinderDimens: Size
    ) {

        if (textureView == null) {
            return
        }

        if (rotation == viewFinderRotation &&
            Objects.equals(newBufferDimens, bufferDimens) &&
            Objects.equals(newViewFinderDimens, viewFinderDimens)
        ) {
            return
        }

        if (rotation == null) {
            return
        } else {
            viewFinderRotation = rotation
        }

        if (newBufferDimens.width == 0 || newBufferDimens.height == 0) {
            return
        } else {
            bufferDimens = newBufferDimens
        }

        if (newViewFinderDimens.width == 0 || newViewFinderDimens.height == 0) {
            return
        } else {
            viewFinderDimens = newViewFinderDimens
        }

        val matrix = Matrix()

        val centerX = viewFinderDimens.width / 2f
        val centerY = viewFinderDimens.height / 2f
        matrix.postRotate(-viewFinderRotation!!.toFloat(), centerX, centerY)

        val bufferRatio = bufferDimens.height / bufferDimens.width.toFloat()

        val scaledWidth: Int
        val scaledHeight: Int
        if (viewFinderDimens.width > viewFinderDimens.height) {
            scaledHeight = viewFinderDimens.width
            scaledWidth = Math.round(viewFinderDimens.width * bufferRatio)
        } else {
            scaledHeight = viewFinderDimens.height
            scaledWidth = Math.round(viewFinderDimens.height * bufferRatio)
        }

        val xScale = scaledWidth / viewFinderDimens.width.toFloat()
        val yScale = scaledHeight / viewFinderDimens.height.toFloat()

        //matrix.preScale(xScale, yScale, centerX, centerY)
        textureView.setTransform(matrix)
    }

    companion object {

        fun getDisplaySurfaceRotation(display: Display?) = when (display?.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> null
        }

        fun build(config: PreviewConfig, viewFinder: TextureView, context: Context) =
            AutoFitCameraView(config, WeakReference(viewFinder), context).useCase
    }
}