package com.scurab.android.uktrains.ui

import androidx.fragment.app.Fragment
import com.scurab.android.uktrains.R
import com.scurab.android.uktrains.util.app

abstract class BaseFragment : Fragment() {

    protected val sharedPrefs by lazy { app().sharedPrefs }

    fun openFragment(baseFragment: BaseFragment, addToBackStack: Boolean = true) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, baseFragment)
            .apply {
                if (addToBackStack) {
                    addToBackStack(null)
                }
            }
            .commit()
    }

    fun popBackStack() {
        requireActivity()
            .supportFragmentManager
            .popBackStack()
    }
}