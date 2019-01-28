package com.scurab.android.uktrains.util

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scurab.android.uktrains.widget.config.ConfigureWidgetViewModel

class SharedPrefs(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) {

    var widgets: List<ConfigureWidgetViewModel>?
        get() {
            val json = sharedPrefs.getString(WIDGETS, null)
            return json?.let { gson.fromJson(json, WIDGETS_TYPE) }
        }
        set(value) {
            sharedPrefs.edit().putString(WIDGETS, if (value != null) gson.toJson(value) else null).apply()
        }

    companion object {
        @JvmStatic
        private val WIDGETS = "Widgets"
        @JvmStatic
        private val WIDGETS_TYPE = object : TypeToken<List<ConfigureWidgetViewModel>>() {}.type
    }
}