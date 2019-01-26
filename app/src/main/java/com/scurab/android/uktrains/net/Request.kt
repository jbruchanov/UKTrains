package com.scurab.android.uktrains.net

import androidx.annotation.IntRange
import org.simpleframework.xml.*

abstract class BoardRequest {
    @field:Namespace(reference = NS_LDB) @field:Element(name = "crs") var stationCode: String
    @field:Namespace(reference = NS_LDB) @field:Element(name = "numRows") var rowsNumber: Int = 0
    @field:Namespace(reference = NS_LDB) @field:Element(name = "filterCrs", required = false) var filterStationCode: String? = null
    @field:Namespace(reference = NS_LDB) @field:Element(name = "filterType", required = false) var filterType: String? = null
    @IntRange(from = -119, to = 119)
    @field:Namespace(reference = NS_LDB) @field:Element(name = "timeOffset", required = false) var timeOffset: Int? = null
    @IntRange(from = -119, to = 119)
    @field:Namespace(reference = NS_LDB) @field:Element(name = "timeWindow", required = false) var timeWindow: Int? = null

    constructor(stationCode: String, rowsNumber: Int = 10) {
        this.stationCode = stationCode
        this.rowsNumber = rowsNumber
    }
}

class DepartureBoardRequest(stationCode: String, rowsNumber: Int = 10) : BoardRequest(stationCode, rowsNumber)
class DepartureBoardRequestWithDetails(stationCode: String, rowsNumber: Int = 10) : BoardRequest(stationCode, rowsNumber)
class ArrivalBoardRequest(stationCode: String, rowsNumber: Int = 10) : BoardRequest(stationCode, rowsNumber)
class ArrivalBoardRequestWithDetails(stationCode: String, rowsNumber: Int = 10) : BoardRequest(stationCode, rowsNumber)


class ServiceDetailsRequest(
    @field:Namespace(reference = NS_LDB) @field:Element(name = "serviceID") var serviceId: String
)