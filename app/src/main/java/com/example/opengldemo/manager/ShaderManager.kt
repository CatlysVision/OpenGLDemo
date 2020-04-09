package com.example.opengldemo.manager

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glGetUniformLocation
import android.os.health.ServiceHealthStats
import android.util.SparseArray
import com.example.opengldemo.buildProgram


val SHADER_BASE = 0
val SHADER_1 = 1
val SHADER_2 = 2
val SHADER_3 = 3
val SHADER_4 = 4
val SHADER_5 = 5
val SHADER_6 = 6
val SHADER_7 = 7
val SHADER_FBO = 8
val SHADER_SOUL = 9

private val POSITION_ATTRIBUTE = "aPosition"
private val TEXTURE_MATRIX_UNIFORM = "uTextureMatrix"
private val TEXTURE_COORD_ATTRIBUTE = "aTextureCoordinate"
private val TEXTURE_SAMPLER_UNIFORM = "uTextureSampler"

private var mParamSparseArray = SparseArray<ShaderParam>()

public fun init(context: Context) {
    addParam(context, SHADER_BASE, "camera_vertex.glsl", "camera_fragment.glsl")
    addParam(context, SHADER_1, "camera_vertex.glsl", "camera_fragment_f1_test.glsl")
    addParam(context, SHADER_2, "camera_vertex.glsl", "camera_fragment_f2.glsl")
    addParam(context, SHADER_3, "camera_vertex.glsl", "camera_fragment_f3.glsl")
    addParam(context, SHADER_4, "camera_vertex.glsl", "camera_fragment_f4.glsl")
    addParam(context, SHADER_5, "camera_vertex.glsl", "camera_fragment_f5.glsl")
    addParam(context, SHADER_6, "camera_vertex.glsl", "camera_fragment_f6.glsl")
    addParam(context, SHADER_7, "camera_vertex.glsl", "camera_fragment_f7.glsl")
    addParam(context, SHADER_FBO, "camera_vertex_soul.glsl", "camera_fragment_soul.glsl")
    addParam(context, SHADER_SOUL, "camera_vertex_soul1.glsl", "camera_fragment_soul1.glsl")
}

public fun addParam(
    context: Context,
    type: Int,
    vertexFileName: String,
    fragmentFileName: String
) {
    val program = buildProgram(context, vertexFileName, fragmentFileName)
    val aPositionLocation = glGetAttribLocation(program, POSITION_ATTRIBUTE)
    val aTextureCoordLocation = glGetAttribLocation(program, TEXTURE_COORD_ATTRIBUTE)
    val uTextureMatrixLocation = glGetUniformLocation(program, TEXTURE_MATRIX_UNIFORM)
    val uTextureSamplerLocation = glGetUniformLocation(program, TEXTURE_SAMPLER_UNIFORM)

    val shaderParam =
        ShaderParam(
            program,
            aPositionLocation,
            aTextureCoordLocation,
            uTextureMatrixLocation,
            uTextureSamplerLocation
        )
    mParamSparseArray.append(type, shaderParam)
}

public fun getParamByType(type: Int): ShaderParam {
    return mParamSparseArray[type]
}

data class ShaderParam(
    var program: Int,
    var positionLocation: Int,
    var textureCoordLocation: Int,
    var textureMatrixLocation: Int,
    var textureSampler: Int
)
