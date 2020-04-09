package com.example.opengldemo.utils

import android.content.Context
import android.widget.Toast
import java.time.Duration

var mToast: Toast? = null

fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
    if (mToast == null) {
        mToast = Toast.makeText(context, message, duration)
    } else {
        mToast?.setText(message)
    }
    mToast?.show()
}