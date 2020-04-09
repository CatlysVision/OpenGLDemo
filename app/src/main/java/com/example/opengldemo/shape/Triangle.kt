package com.example.opengldemo.shape

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.Matrix
import android.util.Log
import com.example.opengldemo.buildProgram
import com.example.opengldemo.toFloatBuffer
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.cos
import kotlin.math.sin

class Triangle(context: Context) : BaseShape() {

    private val U_COLOR = "uColor"
    private val A_POSITION = "vPosition"

    private val U_MODEL_MATRIX = "uModelMatrix"
    private val U_VIEW_MATRIX = "uViewMatrix"
    private val U_PROJECTION_MATRIX = "uProjectionMatrix"

    private var uColorLocation: Int = 0
    private var aPositionLocation: Int = 0
    //private var uModelMatrixAttr: Int = 0
    //private var uViewMatrixAttr: Int = 0
    private var uProjectionMatrixAttr: Int = 0

    private val triangleVertex = floatArrayOf(
        0f, 0f,
        -0.5f, -0.8f,
        0.5f, -0.8f,
        0.5f, 0.8f,
        -0.5f, 0.8f,
        -0.5f, -0.8f
    )
    private val color = floatArrayOf(255.0f, 0.0f, 0.0f, 1.0f)

    private val CIRCLE_VERTEX_NUM = 360
    private val circleVertex = FloatArray(CIRCLE_VERTEX_NUM * 2 + 4)
    private val radian = 2 * Math.PI / CIRCLE_VERTEX_NUM
    private val radius = 0.8f

    private val COORDS_PER_VERTEX = 2
    private val vertexCount: Int = circleVertex.size / COORDS_PER_VERTEX
    private val vertexStride = COORDS_PER_VERTEX * 4

    private var vertexBuffer: FloatBuffer

    init {
        mProgram = buildProgram(context, "triangle_vertext.glsl", "triangle_fragment.glsl")
        //mProgram = buildProgram(vertexShaderCode, fragmentShaderCode)
        glUseProgram(mProgram)
        //vertexBuffer = triangleVertex.toFloatBuffer()

        circleVertex[0] = 0f
        circleVertex[1] = 0f
        for (i in 0 until CIRCLE_VERTEX_NUM) {
            circleVertex[2 * i + 2] = (radius * cos(radian * i)).toFloat()
            circleVertex[2 * i + 1 + 2] = (radius * sin(radian * i)).toFloat()
        }
        circleVertex[CIRCLE_VERTEX_NUM * 2 + 2] = (radius * cos(radian)).toFloat()
        circleVertex[CIRCLE_VERTEX_NUM * 2 + 3] = (radius * sin(radian)).toFloat()
        vertexBuffer = circleVertex.toFloatBuffer()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        uColorLocation = glGetUniformLocation(mProgram, U_COLOR)
        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION)
        //uModelMatrixAttr = glGetUniformLocation(mProgram, U_MODEL_MATRIX)
        //uViewMatrixAttr = glGetUniformLocation(mProgram, U_VIEW_MATRIX)
        uProjectionMatrixAttr = glGetUniformLocation(mProgram, U_PROJECTION_MATRIX)

        Matrix.setIdentityM(mModelMatrix, 0)
        Matrix.setIdentityM(mViewMatrix, 0)

        glEnableVertexAttribArray(aPositionLocation)
        glVertexAttribPointer(
            aPositionLocation,
            COORDS_PER_VERTEX,
            GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer
        )

        glEnableVertexAttribArray(uColorLocation)
        glUniform4fv(uColorLocation, 1, color, 0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
        val aspectRatio =
            if (width > height) width.toFloat() / height.toFloat() else height.toFloat() / width.toFloat()
        if (width > height) {
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 0f, 10f)
        } else {
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, 0f, 10f)
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)

        //glUniformMatrix4fv(uModelMatrixAttr, 1, false, mModelMatrix, 0)
        //glUniformMatrix4fv(uViewMatrixAttr, 1, false, mViewMatrix, 0)
        glUniformMatrix4fv(uProjectionMatrixAttr, 1, false, mProjectionMatrix, 0)

        glDrawArrays(GL_TRIANGLE_FAN, 0, vertexCount)
    }

}