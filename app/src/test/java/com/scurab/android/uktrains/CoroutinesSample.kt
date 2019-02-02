package com.scurab.android.uktrains

import com.scurab.android.uktrains.net.NationalRailAPI
import com.scurab.android.uktrains.net.UKTrainConverterFactory
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class CoroutinesSample {

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

    @Test
    fun test() = runBlocking {

    }

    @Test
    fun test2() = runBlocking {
        val a = async { delay(1000); 25 }
        val b = async { delay(2000); 25 }

        val time = measureTimeMillis {
            a.join()
            b.join()
        }

        val context = newSingleThreadContext("z")
        context.close()
        launch(context){

        }

        println(time)
    }
}