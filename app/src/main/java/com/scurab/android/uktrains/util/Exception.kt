package com.scurab.android.uktrains.util

typealias NPE = NullPointerException
typealias ISE = IllegalStateException

fun npe(msg: String): Nothing = throw NPE(msg)
fun ise(msg: String): Nothing = throw ISE(msg)