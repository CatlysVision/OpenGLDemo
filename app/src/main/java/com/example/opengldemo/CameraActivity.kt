package com.example.opengldemo

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Size
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.opengldemo.manager.*
import com.example.opengldemo.utils.showToast
import com.example.opengldemo.widget.ShaderViewAdapter

class CameraActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private val REQUEST_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    private lateinit var textureView: TextureView

    private lateinit var mCameraSurfaceTextureManager: CameraSurfaceTextureManager

    val shaders = intArrayOf(
        SHADER_BASE,
        SHADER_1,
        SHADER_2,
        SHADER_3,
        SHADER_4,
        SHADER_5,
        SHADER_6,
        SHADER_7
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        textureView = findViewById(R.id.camera_preview)
        initViewPager()
        if (permissionGranted()) {
            textureView.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(this, REQUEST_PERMISSIONS, 10)
        }
        textureView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> updateTransform() }
    }

    private fun initViewPager() {
        val list = mutableListOf<View>()
        for (i in 0..7) {
            val view = View(this@CameraActivity)
            list.add(view)
        }
        val viewPager = findViewById<ViewPager>(R.id.shader_pager)
        val adapter = ShaderViewAdapter(list)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(this)
    }

    private fun permissionGranted() = REQUEST_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 10) {
            if (permissionGranted()) {
                textureView.post { startCamera() }
            }
        }

    }

    private fun startCamera() {
        //val metrics = DisplayMetrics().also { textureView.display.getRealMetrics(it) }
        //val screenSize = Size(metrics.widthPixels, metrics.heightPixels)
        val previewConfig = PreviewConfig.Builder().apply {
            setTargetAspectRatio(AspectRatio.RATIO_16_9)
        }.build()
        val preView = Preview(previewConfig)
        preView.setOnPreviewOutputUpdateListener {
            val parent = textureView.parent as ViewGroup
            parent.removeView(textureView)
            parent.addView(textureView, 0)

            //textureView.surfaceTexture = it.surfaceTexture
            mCameraSurfaceTextureManager =
                CameraSurfaceTextureManager(this@CameraActivity, it.surfaceTexture)
            mCameraSurfaceTextureManager.bindTextureView(textureView)

            updateTransform()
        }
        CameraX.bindToLifecycle(this, preView)
    }

    private fun updateTransform() {
        val matrix = Matrix()
        val centerX = textureView.width / 2f
        val centerY = textureView.height / 2f
        val rotationDegrees = when (textureView.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)
        textureView.setTransform(matrix)
    }


    override fun onDestroy() {
        super.onDestroy()
        CameraX.unbindAll()
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        val type = shaders[position]
        var name = "None"
        when (type) {
            SHADER_BASE -> {
                name = "None"
            }
            SHADER_1 -> {
                name = "怀旧"
            }
            SHADER_2 -> {
                name = "负片"
            }
            SHADER_3 -> {
                name = "连环画"
            }
            SHADER_4 -> {
                name = "灰度"
            }
            SHADER_5 -> {
                name = "高亮"
            }
            SHADER_6 -> {
                name = "发光"
            }
            SHADER_7 -> {
                name = "分屏"
            }
        }
        showToast(this@CameraActivity, name)
        //mCameraSurfaceTextureManager.setFilter(type)
    }

}
