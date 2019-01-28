package com.scurab.android.uktrains.widget

import android.graphics.Color
import android.graphics.Typeface
import android.widget.TextView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan


fun TextView.textTextWithBoldLetters(text: String, boldIndexes: IntArray) {
    val str = SpannableStringBuilder(text)
    boldIndexes.forEach {
        str.setSpan(StyleSpan(Typeface.BOLD), it, it+1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        str.setSpan(ForegroundColorSpan(Color.WHITE), it, it+1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    }
    setText(str)
}