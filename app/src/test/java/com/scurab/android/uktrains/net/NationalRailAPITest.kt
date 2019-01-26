package com.scurab.android.uktrains.net

import com.scurab.android.uktrains.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


class NationalRailAPITest {

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
    @Ignore
    fun testRealDepartureBoardRequest() {
        val execute = api
            .getDepartureBoard(DepartureBoardRequest("GNH", 5))
            .execute()
        val body = execute.body()
        assertEquals("GNH", body?.stationBoardResult?.stationCode)
    }

    @Test
    @Ignore
    fun testRealDepartureBoardWithDetailsRequest() {
        val execute = api
            .getDepartureBoardWithDetails(DepartureBoardRequestWithDetails("GNH", 5))
            .execute()
        val body = execute.body()
        assertEquals("GNH", body?.stationBoardResult?.stationCode)
    }
}