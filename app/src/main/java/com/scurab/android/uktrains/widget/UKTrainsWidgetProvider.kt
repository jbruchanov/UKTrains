package com.scurab.android.uktrains.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.scurab.android.uktrains.R
import com.scurab.android.uktrains.net.BoardResponse
import com.scurab.android.uktrains.net.DepartureBoardRequest
import com.scurab.android.uktrains.util.app
import com.scurab.android.uktrains.util.npe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class UKTrainsWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val api = context.app().api
        val widgets = context.app().sharedPrefs.widgets as List?

        appWidgetIds.forEach { appWidgetId ->
            widgets
                ?.find { it.widgetId == appWidgetId }
                ?.let { (origin, destination) ->
                    api
                        .getDepartureBoard(DepartureBoardRequest(origin?.code ?: npe("Origin undefined!"), 2))
                        .enqueue(object: Callback<BoardResponse> {
                            override fun onResponse(call: Call<BoardResponse>, response: Response<BoardResponse>) {
                                response.body()?.stationBoardResult
                                    ?.let {
                                        val views = RemoteViews(context.packageName, R.layout.item_tain_service)
                                        val train = it.trainServices?.first()
                                        views.setTextViewText(R.id.time, "${it.stationCode} ${train?.schedTime} ${train?.estTime}")
                                        views.setTextViewText(R.id.operator, train?.operator)
                                        views.setTextViewText(R.id.journey, train?.journey + "\n" + Date().toGMTString())
                                        // Tell the AppWidgetManager to perform an update on the current app widget
                                        appWidgetManager.updateAppWidget(appWidgetId, views)
                                    }
                            }

                            override fun onFailure(call: Call<BoardResponse>, t: Throwable) {
                                val views = RemoteViews(context.packageName, R.layout.item_tain_service)
                                views.setTextViewText(R.id.time, "")
                                views.setTextViewText(R.id.operator, "")
                                views.setTextViewText(R.id.journey, t.message)
                            }
                        })
                }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        val sharedPrefs = context.app().sharedPrefs
        val widgets = sharedPrefs.widgets as? MutableList
        widgets?.removeAll {
            appWidgetIds.contains(it.widgetId)
        }
        sharedPrefs.widgets = widgets
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }
}