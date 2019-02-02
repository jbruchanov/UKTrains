package com.scurab.android.uktrains

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.scurab.android.uktrains.ui.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportFragmentManager.apply {
            if (findFragmentById(R.id.fragment_container) == null) {
                beginTransaction()
                    .replace(R.id.fragment_container, MainFragment())
                    .commit()
            }
        }
    }
}
