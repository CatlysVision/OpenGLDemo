package com.example.opengldemo

import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLES20.*
import javax.microedition.khronos.opengles.GL10


fun createTextureIds(count: Int): IntArray {
    val texture = IntArray(count)
    glGenTextures(count, texture, 0)
    return texture
}

fun createFBOTexture(width: Int, height: Int): Int {
    val textures = IntArray(1)
    glGenTextures(1, textures, 0)
    glBindTexture(GL_TEXTURE_2D, textures[0])
    glTexImage2D(
        GL_TEXTURE_2D, 0, GL_RGBA, width, height,
        0, GL_RGBA, GL_UNSIGNED_BYTE, null
    )
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST.toFloat())
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST.toFloat())
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE.toFloat())
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE.toFloat())
    glBindTexture(GL_TEXTURE_2D, 0)
    return textures[0]
}


fun createFrameBuffer(): Int {
    val fbs = IntArray(1)
    glGenFramebuffers(1, fbs, 0)
    return fbs[0]
}

fun bindFBO(fb: Int, textureId: Int) {
    glBindTexture(GL_TEXTURE_2D, textureId)
    glBindFramebuffer(GL_FRAMEBUFFER, fb)
    glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureId, 0)
    glBindTexture(GL_TEXTURE_2D, 0)
    glBindFramebuffer(GL_FRAMEBUFFER, 0)
}

fun unbindFBO() {
    glBindFramebuffer(GL_FRAMEBUFFER, GL_NONE)
    glBindTexture(GL_TEXTURE_2D, 0)
}

fun deleteFBO(frame: IntArray, texture: IntArray) {
    glBindFramebuffer(GL_FRAMEBUFFER, GL_NONE)
    glDeleteBuffers(1, frame, 0)
    glBindTexture(GL_TEXTURE_2D, 0)
    glDeleteTextures(1, texture, 0)
}

fun activateTexture(type: Int, textureId: Int, index: Int, textureHandler: Int) {
    glActiveTexture(GL_TEXTURE0 + index)
    glBindTexture(type, textureId)
    glUniform1i(textureHandler, index)
    glTexParameterf(type, GL_TEXTURE_MIN_FILTER, GL_LINEAR.toFloat())
    glTexParameterf(type, GL_TEXTURE_MAG_FILTER, GL_LINEAR.toFloat())
    glTexParameteri(type, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
    glTexParameteri(type, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
}

fun createOESTextureObject(): Int {
    val tex = IntArray(1)
    glGenTextures(1, tex, 0)
    glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, tex[0])
    glTexParameterf(
        GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
        GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST.toFloat()
    )
    glTexParameterf(
        GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
        GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR.toFloat()
    )
    glTexParameterf(
        GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
        GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE.toFloat()
    )
    glTexParameterf(
        GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
        GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE.toFloat()
    )
    glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
    return tex[0]
}