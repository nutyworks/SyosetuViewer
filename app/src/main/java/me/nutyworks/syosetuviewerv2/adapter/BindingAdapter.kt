package me.nutyworks.syosetuviewerv2.adapter

import android.graphics.Typeface
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