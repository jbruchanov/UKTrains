package com.scurab.android.uktrains.net

import org.simpleframework.xml.*

class DepartureBoardRequest {
    @field:Namespace(reference = NS_LDB)
    @field:Element(name = "crs")
    var stationCode: String? = null

    @field:Namespace(reference = NS_LDB)
    @field:Element(name = "numRows")
    var rowsNumber: Int = 0

    constructor(stationCode: String?, rowsNumber: Int) {
        this.stationCode = stationCode
        this.rowsNumber = rowsNumber
    }
}