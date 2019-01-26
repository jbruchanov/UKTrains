package com.scurab.android.uktrains.net

import com.scurab.android.uktrains.BuildConfig
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
            .addConverterFactory(UKTrainConverterFactory(token))
            .client(client)
            .build()
            .create(NationalRailAPI::class.java)
    }

    @Test
    fun testRealDepartureBoardRequest() {
        val execute = api
            .getDepartureBoard(DepartureBoardRequest(stationCode))
            .execute()
        val body = execute.body()
        assertEquals(stationCode, body?.stationBoardResult?.stationCode)
    }

    @Test
    fun testRealDepartureBoardWithDetailsRequest() {
        val execute = api
            .getDepartureBoardWithDetails(DepartureBoardRequestWithDetails(stationCode))
            .execute()
        val body = execute.body()
        assertEquals(stationCode, body?.stationBoardResult?.stationCode)
    }

    @Test
    fun testRealArrivalBoardRequest() {
        val execute = api
            .getArrivalBoard(ArrivalBoardRequest(stationCode))
            .execute()
        val body = execute.body()
        assertEquals(stationCode, body?.stationBoardResult?.stationCode)
    }

    @Test
    fun testRealArrivalBoardWithDetailsRequest() {
        val execute = api
            .getArrivalBoardWithDetails(ArrivalBoardRequestWithDetails(stationCode))
            .execute()
        val body = execute.body()
        assertEquals(stationCode, body?.stationBoardResult?.stationCode)
    }


    @Test
    fun testRealGetServiceDetails() {
        val execute = api
            .getDepartureBoard(DepartureBoardRequest(stationCode))
            .execute()
        val serviceId = execute.body()?.stationBoardResult?.trainServices?.first()?.serviceID
        serviceId ?: throw NullPointerException("ServiceId not found")
        val serviceDetails = api.getServiceDetials(ServiceDetailsRequest(serviceId))
            .execute()
            .body()
            ?.serviceDetailsResult

        serviceDetails?.apply {
            assertNotNull(stationCode)
            assertTrue(previousCallingPoints?.isNotEmpty() ?: false)
            assertTrue(subsequentCallingPoints?.isNotEmpty() ?: false)
        } ?: fail("null serviceDetails")
    }
}