package com.scurab.android.uktrains.ui

import androidx.fragment.app.Fragment
import com.scurab.android.uktrains.R
import com.scurab.android.uktrains.net.NationalRailAPI
import com.scurab.android.uktrains.util.app
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class BaseFragment : Fragment() {

    protected val api: NationalRailAPI by lazy { app().api }
    protected val uiScope = CoroutineScope(Dispatchers.Main)
    protected val ioScope = Dispatchers.IO

    protected fun showFragment(fragment: BaseFragment, addToBackStack: Boolean) {
        requireFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .apply {
                if (addToBackStack) {
                    addToBackStack(null)
                }
            }
            .commit()
    }

    protected fun popBackStack() {
        requireFragmentManager().popBackStack()
    }
}