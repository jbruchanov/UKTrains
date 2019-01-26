package com.scurab.android.uktrains.net

import androidx.annotation.IntRange
import org.simpleframework.xml.*

open class DepartureBoardRequest {
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