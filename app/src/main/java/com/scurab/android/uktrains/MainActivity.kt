package com.scurab.android.uktrains

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.scurab.android.uktrains.ui.PickStationFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportFragmentManager.apply {
            if (findFragmentById(R.id.fragment_container) == null) {
                beginTransaction()
                    .replace(R.id.fragment_container, PickStationFragment())
                    .commit()
            }
        }
    }
}
