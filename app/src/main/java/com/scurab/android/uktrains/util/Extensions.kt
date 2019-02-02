package com.scurab.android.uktrains.util

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.scurab.android.uktrains.App
import com.scurab.android.uktrains.ui.BaseFragment
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

fun Context.app(): App = applicationContext as App
fun BaseFragment.app(): App = requireContext().app()

fun View.findViehHolderPosition(): Int {
    var v: View? = this
    while (v != null) {
        (v.layoutParams as? RecyclerView.LayoutParams)?.let {
            return it.viewAdapterPosition
        }
        v = v.parent as? View
    }
    ise("Unable to find adapter position")
}

inline fun <T, R> T.letIfNull(block: (T) -> R): R {
//    contract {
//        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
//    }
    return block(this)
}