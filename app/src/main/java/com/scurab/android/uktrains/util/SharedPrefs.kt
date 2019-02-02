package com.scurab.android.uktrains.util

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scurab.android.uktrains.model.HasSavingId
import com.scurab.android.uktrains.model.StationBoard
import java.lang.reflect.Type
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPrefs(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) {
    var stationBoards: List<StationBoard>? by sharedPrefJson(BOARDS, BOARDS_TYPE)

    private fun sharedPrefJson(
        keyName: String,
        type: Type
    ): ReadWriteProperty<SharedPrefs, List<StationBoard>?> {
        return SharedPrefsJsonPropertyDelegate(sharedPrefs, gson, keyName, type)
    }

    companion object {
        @JvmStatic
        private val BOARDS = "BOARDS"
        @JvmStatic
        private val BOARDS_TYPE = object : TypeToken<List<StationBoard>>() {}.type
    }
}

class SharedPrefsJsonPropertyDelegate<T>(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson,
    private val key: String,
    private val type: Type
) : ReadWriteProperty<Any, T?> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        val json = sharedPrefs.getString(key, null)
        return json?.let { gson.fromJson(json, type) }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        initIds(value)
        sharedPrefs.edit().putString(key, if (value != null) gson.toJson(value) else null).apply()
    }

    private fun initIds(value: Any?) {
        (value as? HasSavingId)
            ?.let {
                if (it.id == null) {
                    it.id = System.nanoTime()
                }
            }
        (value as? Collection<*>)?.forEach {item ->
            item?.let { initIds(it) }
        }
    }
}