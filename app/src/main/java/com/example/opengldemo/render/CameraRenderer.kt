package com.example.opengldemo.render

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.EGL14
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.view.TextureView
import com.example.opengldemo.filter.*
import com.example.opengldemo.manager.*
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLSurface

class CameraRenderer : SurfaceTexture.OnFrameAvailableListener {

    companion object {
        val INIT = 0X10
        val RENDER = 0X11
        val DEINIE = 0X12
        val SURFACE_INIT = 0x13
    }

    private lateinit var mContext: Context
    private lateinit var mHandlerThread: HandlerThread
    private lateinit var mHandler: Handler


    private lateinit var mTextureView: TextureView
    private var mOESTextureId = 0

    private lateinit var mEgl: EGL10
    private var mEGLDisplay = EGL10.EGL_NO_DISPLAY
    private var mEGLContext = EGL10.EGL_NO_CONTEXT
    private val mEGLConfig = arrayOfNulls<EGLConfig>(1)
    private lateinit var mEglSurface: EGLSurface
    private lateinit var mOESSurfaceTexture: SurfaceTexture

    protected var mWidth = 0
    protected var mHeight = 0

    private var mDrawer: BaseFilter? = null

    fun init(
        textureView: TextureView,
        oesTextureId: Int,
        context: Context,
        width: Int,
        height: Int
    ) {
        mTextureView = textureView
        mOESTextureId = oesTextureId
        mContext = context
        mWidth = width
        mHeight = height
        mHandlerThread = HandlerThread("Renderer Thread")
        mHandlerThread.start()
        mHandler = object : Handler(mHandlerThread.looper) {

            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    INIT -> {
                        initEGL()
                        return
                    }
                    RENDER -> {
                        drawFrame()
                        return
                    }
                    SURFACE_INIT -> {
                        initSurfaceTexture()
                        return
                    }
                    DEINIE -> {
                        deInitSurfaceTexture()
                        return
                    }
                }
            }
        }
        mHandler.sendEmptyMessage(INIT)
    }

    private fun initEGL() {
        mEgl = EGLContext.getEGL() as EGL10

        mEGLDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
        if (mEGLDisplay === EGL10.EGL_NO_DISPLAY) {
            throw RuntimeException("eglGetDisplay failed! " + mEgl.eglGetError())
        }

        val version = IntArray(2)
        if (!mEgl.eglInitialize(mEGLDisplay, version)) {
            throw RuntimeException("eglInitialize failed! " + mEgl.eglGetError())
        }

        val attributes = intArrayOf(
            EGL10.EGL_RED_SIZE,
            8,
            EGL10.EGL_GREEN_SIZE,
            8,
            EGL10.EGL_BLUE_SIZE,
            8,
            EGL10.EGL_ALPHA_SIZE,
            8,
            EGL10.EGL_BUFFER_SIZE,
            32,
            EGL10.EGL_RENDERABLE_TYPE,
            4,
            EGL10.EGL_SURFACE_TYPE,
            EGL10.EGL_WINDOW_BIT,
            EGL10.EGL_NONE
        )
        val configsNum = IntArray(1)
        if (!mEgl.eglChooseConfig(mEGLDisplay, attributes, mEGLConfig, 1, configsNum)) {
            throw RuntimeException("eglChooseConfig failed! " + mEgl.eglGetError())
        }

        val surfaceTexture = mTextureView.surfaceTexture ?: return
        mEglSurface = mEgl.eglCreateWindowSurface(mEGLDisplay, mEGLConfig[0], surfaceTexture, null)

        val contextAttributes = intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE)
        mEGLContext = mEgl.eglCreateContext(
            mEGLDisplay,
            mEGLConfig[0],
            EGL10.EGL_NO_CONTEXT,
            contextAttributes
        )

        if (mEGLDisplay === EGL10.EGL_NO_DISPLAY || mEGLContext === EGL10.EGL_NO_CONTEXT) {
            return
        }

        if (!mEgl.eglMakeCurrent(mEGLDisplay, mEglSurface, mEglSurface, mEGLContext)) {
            throw RuntimeException("eglMakeCurrent failed! " + mEgl.eglGetError())
        }

        init(mContext)
        mDrawer = SoulFilter(mOESTextureId)
        mDrawer?.onCreate(mWidth, mHeight)
    }

    private var hasChangeFilter = false
    private var filterType = SHADER_BASE

    fun setFilter(type: Int) {
        if (filterType == type) {
            return
        }
        filterType = type
        hasChangeFilter = true
    }

    private fun drawFrame() {
        mEgl.eglMakeCurrent(mEGLDisplay, mEglSurface, mEglSurface, mEGLContext)
        if (hasChangeFilter) {
            //choiceFilter()
        }
        mDrawer?.onDrawFrame(mOESSurfaceTexture)
        mEgl.eglSwapBuffers(mEGLDisplay, mEglSurface)
    }

    private fun choiceFilter() {
        mDrawer = null
        when (filterType) {
            SHADER_BASE -> {
                //mDrawer = NormalFilter(mOESTextureId)
            }
            SHADER_1 -> {
                //mDrawer = Filter1(mOESTextureId)
            }
            SHADER_2 -> {
                //mDrawer = Filter2(mOESTextureId)
            }
            SHADER_3 -> {
                //mDrawer = Filter3(mOESTextureId)
            }
            SHADER_4 -> {
                //mDrawer = Filter4(mOESTextureId)
            }
            SHADER_5 -> {
                //mDrawer = Filter5(mOESTextureId)
            }
            SHADER_6 -> {
                //mDrawer = Filter6(mOESTextureId)
            }
            SHADER_7 -> {
                //mDrawer = Filter7(mOESTextureId)
            }
        }
        mDrawer?.onCreate(mWidth, mHeight)
        hasChangeFilter = false
    }

    private fun initSurfaceTexture() {
        mOESSurfaceTexture.attachToGLContext(mOESTextureId)
        mOESSurfaceTexture.setOnFrameAvailableListener(this)
    }

    private fun deInitSurfaceTexture() {

    }

    fun initOESTexture(surfaceTexture: SurfaceTexture) {
        mOESSurfaceTexture = surfaceTexture
        mHandler.sendEmptyMessage(SURFACE_INIT)
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        mHandler.sendEmptyMessage(RENDER)
    }
}