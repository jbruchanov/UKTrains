package com.scurab.android.uktrains.widget

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.*
import android.widget.RemoteViews
import com.scurab.android.uktrains.BuildConfig
import com.scurab.android.uktrains.R
import com.scurab.android.uktrains.net.DepartureBoardRequest
import com.scurab.android.uktrains.net.NationalRailAPI
import com.scurab.android.uktrains.net.UKTrainConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class UKTrainsService : Service() {

    private val stationCode = "GNH"
    private val api: NationalRailAPI

    init {
        val okHttpClient = OkHttpClient.Builder()

        val interceptor = HttpLoggingInterceptor {
            println(it)
        }
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = okHttpClient
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.MINUTES)
            .readTimeout(15, TimeUnit.MINUTES)
            .addNetworkInterceptor(interceptor)
            .build()

        val token = BuildConfig.API_TOKEN
        api = Retrofit.Builder()
            .baseUrl("https://lite.realtime.nationalrail.co.uk/OpenLDBWS/")
            .addConverterFactory(UKTrainConverterFactory(token))
            .client(client)
            .build()
            .create(NationalRailAPI::class.java)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private var mServiceLooper: Looper? = null
    private var mServiceHandler: ServiceHandler? = null
    private var counter = 0
    // Handler that receives messages from the thread
    private inner class ServiceHandler(
        private val ukTrainsService: UKTrainsService
    ) : Thread() {

        override fun run() {
            super.run()
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            while (true) {
                try {
                    Thread.sleep(1000)
                    api.getDepartureBoard(DepartureBoardRequest("GNH", 5))
                        .execute()
                        .body()
                        ?.stationBoardResult
                        ?.trainServices?.let { trainServices ->
                        val wm = ukTrainsService.getSystemService(Context.APPWIDGET_SERVICE) as AppWidgetManager
                        val ids = wm.getAppWidgetIds(ComponentName(ukTrainsService, UKTrainsWidgetProvider::class.java))
                        ids.forEach { widgetId ->
                            val views = RemoteViews(
                                getPackageName(),
                                R.layout.item_tain_service
                            )
                            val trainService = trainServices[counter % trainServices.size]
                            views.setTextViewText(R.id.time, trainService.schedTimeDeparture)
                            views.setTextViewText(R.id.operator, trainService.operator)
                            views.setTextViewText(R.id.journey, trainService.journey)
                            wm.updateAppWidget(widgetId, views)
                            counter++
                        }
                    }
                    return
                } catch (e: Throwable) {
                    // Restore interrupt status.
                    Thread.currentThread().interrupt()
                    break
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job


        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        ServiceHandler(this).start()
    }
}