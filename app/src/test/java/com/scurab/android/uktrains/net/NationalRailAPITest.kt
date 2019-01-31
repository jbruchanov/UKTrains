package com.scurab.android.uktrains.net

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.scurab.android.uktrains.BuildConfig
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Retrofit
import java.lang.NullPointerException
import java.util.concurrent.TimeUnit


class NationalRailAPITest {

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
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(UKTrainConverterFactory(token))
            .client(client)
            .build()
            .create(NationalRailAPI::class.java)
    }

    @Test
    fun testRealDepartureBoardRequest() {
        runBlocking {
            val body = api
                .getDepartureBoardAsync(DepartureBoardRequest(stationCode))
                .await()
            assertEquals(stationCode, body?.stationBoardResult?.stationCode)
        }
    }

    @Test
    fun testRealDepartureBoardWithDetailsRequest() {
        runBlocking {
            val body = api
                .getDepartureBoardWithDetailsAsync(DepartureBoardRequestWithDetails(stationCode))
                .await()
            assertEquals(stationCode, body?.stationBoardResult?.stationCode)
        }
    }

    @Test
    fun testRealArrivalBoardRequest() {
        runBlocking {
            val body = api
                .getArrivalBoardAsync(ArrivalBoardRequest(stationCode))
                .await()
            assertEquals(stationCode, body?.stationBoardResult?.stationCode)
        }
    }

    @Test
    fun testRealArrivalBoardWithDetailsRequest() {
        runBlocking {
            val body = api
                .getArrivalBoardWithDetailsAsync(ArrivalBoardRequestWithDetails(stationCode))
                .await()
            assertEquals(stationCode, body?.stationBoardResult?.stationCode)
        }
    }


    @Test
    fun testRealGetServiceDetails() {
        runBlocking {
            val board = api
                .getDepartureBoardAsync(DepartureBoardRequest(stationCode))
                .await()
            val serviceId = board?.stationBoardResult?.trainServices?.first()?.serviceID
            serviceId ?: throw NullPointerException("ServiceId not found")
            val serviceDetails = api.getServiceDetailsAsync(ServiceDetailsRequest(serviceId))
                .await()
                .serviceDetailsResult

            serviceDetails?.apply {
                assertNotNull(stationCode)
                assertTrue(previousCallingPoints?.isNotEmpty() ?: false)
                assertTrue(subsequentCallingPoints?.isNotEmpty() ?: false)
            } ?: fail("null serviceDetails")
        }
    }
}