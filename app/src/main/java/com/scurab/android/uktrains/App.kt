package com.scurab.android.uktrains

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.scurab.android.uktrains.net.NationalRailAPI
import com.scurab.android.uktrains.net.UKTrainConverterFactory
import com.scurab.android.uktrains.util.SharedPrefs
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class App : Application() {
    private val gson = Gson()

    val sharedPrefs by lazy {
        SharedPrefs(getSharedPreferences("App", Context.MODE_PRIVATE), gson)
    }

    val api: NationalRailAPI by lazy {
        val okHttpClient = OkHttpClient.Builder()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = okHttpClient
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.MINUTES)
            .readTimeout(15, TimeUnit.MINUTES)
            .addNetworkInterceptor(interceptor)
            .build()

        val token = BuildConfig.API_TOKEN
        Retrofit.Builder()
            .baseUrl("https://lite.realtime.nationalrail.co.uk/OpenLDBWS/")
            .addConverterFactory(UKTrainConverterFactory(token))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(client)
            .build()
            .create(NationalRailAPI::class.java)
    }

    override fun onCreate() {
        super.onCreate()
    }
}