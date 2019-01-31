package com.scurab.android.uktrains.net

import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NationalRailAPI {

    @Headers(value = ["Content-Type: application/soap+xml; charset=utf-8", "SOAPAction: http://thalesgroup.com/RTTI/2012-01-13/ldb/GetDepartureBoard"])
    @POST("ldb9.asmx")
    fun getDepartureBoardAsync(@Body departureBoardRequest: DepartureBoardRequest) : Deferred<BoardResponse>

    @Headers(value = ["Content-Type: application/soap+xml; charset=utf-8", "SOAPAction: http://thalesgroup.com/RTTI/2015-05-14/ldb/GetDepBoardWithDetails"])
    @POST("ldb9.asmx")
    fun getDepartureBoardWithDetailsAsync(@Body departureBoardRequest: DepartureBoardRequestWithDetails) : Deferred<BoardResponse>

    @Headers(value = ["Content-Type: application/soap+xml; charset=utf-8", "SOAPAction: http://thalesgroup.com/RTTI/2012-01-13/ldb/GetArrivalBoard"])
    @POST("ldb9.asmx")
    fun getArrivalBoardAsync(@Body arrivalBoardRequest: ArrivalBoardRequest) : Deferred<BoardResponse>

    @Headers(value = ["Content-Type: application/soap+xml; charset=utf-8", "SOAPAction: http://thalesgroup.com/RTTI/2015-05-14/ldb/GetArrBoardWithDetails"])
    @POST("ldb9.asmx")
    fun getArrivalBoardWithDetailsAsync(@Body arrivalBoardRequest: ArrivalBoardRequestWithDetails) : Deferred<BoardResponse>

    @Headers(value = ["Content-Type: application/soap+xml; charset=utf-8", "SOAPAction: http://thalesgroup.com/RTTI/2012-01-13/ldb/GetServiceDetails"])
    @POST("ldb9.asmx")
    fun getServiceDetailsAsync(@Body serviceDetailsRequest: ServiceDetailsRequest) : Deferred<ServiceDetailsResponse>

}