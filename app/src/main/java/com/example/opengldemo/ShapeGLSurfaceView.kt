package com.example.opengldemo

import android.content.Context
import android.opengl.GLSurfaceView
import com.example.opengldemo.render.ShapeRender

class ShapeGLSurfaceView(context: Context) : GLSurfaceView(context) {

    private var mRenderer: ShapeRender

    init {
        setEGLContextClientVersion(2)
        mRenderer = ShapeRender(context)
        setRenderer(mRenderer)
    }
}