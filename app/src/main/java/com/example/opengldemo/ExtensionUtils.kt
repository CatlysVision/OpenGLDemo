package com.example.opengldemo

import android.content.res.AssetManager
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.charset.Charset

fun AssetManager.glsl2String(fileName: String): String {

    return this.open(fileName).use {
        it.readBytes().toString(Charset.defaultCharset())
    }
}

fun FloatArray.toFloatBuffer(): FloatBuffer {
    val buffer =  ByteBuffer
        .allocateDirect(this.size * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(this)
        .position(0)
    return buffer as FloatBuffer
}

