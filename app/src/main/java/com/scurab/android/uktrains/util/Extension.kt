package com.scurab.android.uktrains.util

import android.content.Context
import com.scurab.android.uktrains.App
import com.scurab.android.uktrains.ui.BaseFragment

fun Context.app(): App = applicationContext as App
fun BaseFragment.app(): App = requireContext().app()