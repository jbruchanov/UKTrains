package com.scurab.android.uktrains.widget.config

import android.appwidget.AppWidgetManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.scurab.android.uktrains.R


class ConfigureActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ConfigureWidgetViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        savedInstanceState?.let {
            viewModel.read(it.getParcelable(VIEW_MODEL))
        }

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
                .takeIf { it != AppWidgetManager.INVALID_APPWIDGET_ID }
                ?.let { widgetId ->
                    viewModel.widgetId = widgetId
                }
        }
        supportFragmentManager.apply {
            if (findFragmentById(R.id.fragment_container) == null) {
                beginTransaction()
                    .replace(R.id.fragment_container, ConfigureFragment())
                    .commit()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(VIEW_MODEL, viewModel)
    }

    companion object {
        @JvmStatic
        private val VIEW_MODEL = "VIEW_MODEL"
    }
}