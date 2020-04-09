package com.example.opengldemo

import android.content.Context
import android.content.res.AssetManager
import android.opengl.GLES20.*
import java.nio.charset.Charset

fun buildProgram(context: Context, vertexFileName: String, fragmentFileName: String): Int {
    val vertexString = context.assets.glsl2String(vertexFileName)
    val fragmentString = context.assets.glsl2String(fragmentFileName)
    return buildProgram(vertexString, fragmentString)
}

fun buildProgram(vertexString: String, fragmentString: String): Int {
    val vertexShaderId = compileVertexShader(vertexString)
    val fragmentShaderId = compileFragmentShader(fragmentString)
    return linkProgram(vertexShaderId, fragmentShaderId)
}

fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
    val programId = glCreateProgram()
    glAttachShader(programId, vertexShaderId)
    glAttachShader(programId, fragmentShaderId)
    glLinkProgram(programId)
    return programId
}

fun compileVertexShader(shaderCode: String): Int {
    return compileShader(GL_VERTEX_SHADER, shaderCode)
}

fun compileFragmentShader(shaderCode: String): Int {
    return compileShader(GL_FRAGMENT_SHADER, shaderCode)
}

fun compileShader(type: Int, shaderCode: String): Int {
    val shaderId = glCreateShader(type)
    glShaderSource(shaderId, shaderCode)
    glCompileShader(shaderId)
    return shaderId
}
