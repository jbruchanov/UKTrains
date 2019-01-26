package com.scurab.android.uktrains

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.appwidget.AppWidgetManager
import android.widget.RemoteViews
import android.content.Intent
import com.scurab.android.uktrains.widget.UKTrainsService


class ConfigureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
                .takeIf { it != AppWidgetManager.INVALID_APPWIDGET_ID }
                ?.let { widgetId ->
                    val appWidgetManager = AppWidgetManager.getInstance(this)
                    val views = RemoteViews(
                        getPackageName(),
                        R.layout.item_tain_service
                    )
                    appWidgetManager.updateAppWidget(widgetId, views)

                    val resultValue = Intent()
                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                    setResult(Activity.RESULT_OK, resultValue)
                    finish()

                    startService(Intent(this, UKTrainsService::class.java))
                }
        }
    }
}