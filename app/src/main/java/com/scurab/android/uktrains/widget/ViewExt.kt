package com.scurab.android.uktrains.widget

import android.graphics.Typeface
import android.widget.TextView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan


fun TextView.textTextWithBoldLetters(text: String, boldIndexes: IntArray) {
    val str = SpannableStringBuilder(text)
    boldIndexes.forEach {
        str.setSpan(
            StyleSpan(Typeface.BOLD),
            it,
            it+1,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
    }
    setText(str)
}