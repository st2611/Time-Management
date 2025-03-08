package com.example.jetpackcompose.toast

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

fun Context.showCustomToast(message: String, isLong: Boolean = false) {
    val toast = Toast.makeText(this, message, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
    val layout = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
        setPadding(32, 16, 32, 16)
        background = GradientDrawable().apply {
            cornerRadius = 24f
            setColor(Color.parseColor("#FF393939"))
        }
    }
    val textView = TextView(this).apply {
        text = message
        setTextColor(Color.WHITE)
        textSize = 16f
        gravity = Gravity.CENTER
        setPadding(16, 8, 16, 8)
    }
    layout.addView(textView)

    toast.view = layout
    toast.show()
}