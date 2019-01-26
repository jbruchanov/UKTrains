package com.scurab.android.uktrains.net

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NationalRailAPI {

    @Headers(value = ["Content-Type: application/soap+xml; charset=utf-8", "SOAPAction: http://thalesgroup.com/RTTI/2012-01-13/ldb/GetDepartureBoard"])
    @POST("ldb9.asmx")
    fun getDepartureBoard(@Body departureBoardRequest: DepartureBoardRequest) : Call<BoardResponse>

    @Headers(value = ["Content-Type: application/soap+xml; charset=utf-8", "SOAPAction: http://thalesgroup.com/RTTI/2015-05-14/ldb/GetDepBoardWithDetails"])
    @POST("ldb9.asmx")
    fun getDepartureBoardWithDetails(@Body departureBoardRequest: DepartureBoardRequestWithDetails) : Call<BoardResponse>

    @Headers(value = ["Content-Type: application/soap+xml; charset=utf-8", "SOAPAction: http://thalesgroup.com/RTTI/2012-01-13/ldb/GetArrivalBoard"])
    @POST("ldb9.asmx")
    fun getArrivalBoard(@Body arrivalBoardRequest: ArrivalBoardRequest) : Call<BoardResponse>

    @Headers(value = ["Content-Type: application/soap+xml; charset=utf-8", "SOAPAction: http://thalesgroup.com/RTTI/2015-05-14/ldb/GetArrBoardWithDetails"])
    @POST("ldb9.asmx")
    fun getArrivalBoardWithDetails(@Body arrivalBoardRequest: ArrivalBoardRequestWithDetails) : Call<BoardResponse>
}