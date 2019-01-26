package com.scurab.android.uktrains.net

import org.simpleframework.xml.Element


class BoardResponse {
    @field:Element(name = "GetStationBoardResult")
    var stationBoardResult: StationBoardResult? = null
}

class ServiceDetailsResponse {
    @field:Element(name = "GetServiceDetailsResult")
    var serviceDetailsResult: ServiceDetailsResult? = null
}