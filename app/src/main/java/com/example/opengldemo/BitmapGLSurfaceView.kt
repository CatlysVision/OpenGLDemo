package com.example.opengldemo

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import com.example.opengldemo.bitmap.BitmapDrawer
import com.example.opengldemo.render.BitmapRender

class BitmapGLSurfaceView(context: Context) : GLSurfaceView(context) {

    private var mRenderer: BitmapRender

    init {
        setEGLContextClientVersion(2)
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_splash_logo_glow_orange)
        val bitmapDrawer = BitmapDrawer(context, bitmap)
        mRenderer = BitmapRender(bitmapDrawer)
        setRenderer(mRenderer)
    }
}