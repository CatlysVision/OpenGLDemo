package com.example.opengldemo.shape

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.*
import android.opengl.GLUtils
import android.opengl.Matrix
import android.os.SystemClock
import android.util.Log
import com.example.opengldemo.buildProgram
import com.example.opengldemo.toFloatBuffer
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ColorTriangle(context: Context) : BaseShape() {

    private val U_COLOR = "u_Color"
    private val A_POSITION = "a_Position"

    private var aColorLocation: Int = 0
    private var aPositionLocation: Int = 0
    private var mMVPMatrixHandle: Int = 0
    private val COORDS_PER_VERTEX = 2

    val mRotationMatrix = FloatArray(16)

    private val triangleVertex = floatArrayOf(
        0.0f, 0.5f,
        -0.5f, -0.5f,
        0.5f, -0.5f
    )

    //private val color = floatArrayOf(255f, 0f, 0f, 1.0f)

    private val color = floatArrayOf(
        1.0f, 0f, 0f, 1.0f,
        0f, 1.0f, 0f, 1.0f,
        0f, 0f, 1.0f, 1.0f
    )

    private var vertexBuffer: FloatBuffer

    private var colorBuffer: FloatBuffer

    init {
        mProgram = buildProgram(context, "triangle_color_vertext.glsl", "triangle_fragment.glsl")
        glUseProgram(mProgram)
        vertexBuffer = triangleVertex.toFloatBuffer()
        colorBuffer = color.toFloatBuffer()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        aColorLocation = glGetAttribLocation(mProgram, U_COLOR)
        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION)
        mMVPMatrixHandle = glGetUniformLocation(mProgram, "uMVPMatrix")

        glVertexAttribPointer(
            aPositionLocation,
            COORDS_PER_VERTEX,
            GL_FLOAT,
            false,
            COORDS_PER_VERTEX * 4,
            vertexBuffer
        )
        glEnableVertexAttribArray(aPositionLocation)

        glVertexAttribPointer(aColorLocation, 4, GL_FLOAT, false, 0, colorBuffer)
        glEnableVertexAttribArray(aColorLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
        val ratio = (width.toFloat() / height.toFloat())
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)

        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1f, 0f)
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)

        val scratch = FloatArray(16)
        val time = SystemClock.uptimeMillis() % 4000L
        val angle = 0.090f * time
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0f, 0f, -1f)
        //Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0)

        //glUniform4fv(aColorLocation, 1, color, 0)
        glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0)

        glDrawArrays(GL_TRIANGLES, 0, 3)
    }

}