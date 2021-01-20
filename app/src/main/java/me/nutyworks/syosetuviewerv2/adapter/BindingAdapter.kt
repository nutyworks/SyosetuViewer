package me.nutyworks.syosetuviewerv2.adapter

import android.graphics.Typeface
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:textStyle")
fun setTypeface(textView: TextView, style: String) {
    when (style) {
        "bold" -> textView.setTypeface(null, Typeface.BOLD)
        "italic" -> textView.setTypeface(null, Typeface.ITALIC)
        else -> textView.setTypeface(null, Typeface.NORMAL)
    }
}

@BindingAdapter("android:textSize")
fun setTextSize(textView: TextView, size: Float) {
    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
}

@BindingAdapter("android:paddingVertical")
fun setPadding(view: View, vertical: Float) {
    val displayMetrics = view.context.resources.displayMetrics
    view.setPaddingRelative(
        view.paddingStart,
        vertical.dp(displayMetrics).toInt(),
        view.paddingEnd,
        vertical.dp(displayMetrics).toInt()
    )
}

@BindingAdapter("android:text")
fun setFloatToText(textView: TextView, float: Float) {
    textView.text = float.toString()
}

fun Float.dp(displayMetrics: DisplayMetrics) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displayMetrics)