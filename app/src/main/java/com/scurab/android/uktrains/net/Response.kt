package com.scurab.android.uktrains.net

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "GetDepartureBoardResponse", strict = false)
class DepartureBoardResponse {
    @field:Element(name = "GetStationBoardResult")
    var stationBoardResult: StationBoardResult? = null
}

@Root(name = "GetStationBoardResult", strict = false)
class StationBoardResult {
    @field:Element(name = "generatedAt", required = false)
    var generatedAt: String? = null

    @field:Element(name = "locationName", required = false)
    var locationName: String? = null

    @field:Element(name = "crs", required = false)
    var stationCode: String? = null

    @field:Element(name = "platformAvailable", required = false)
    var platformAvailable: Boolean? = null

    @field:ElementList(name = "trainServices", required = false)
    var trainServices: List<TrainService>? = null
}

@Root(name = "service", strict = false)
class TrainService {
    @field:Element(name = "std", required = false) var std: String? = null
    @field:Element(name = "etd", required = false) var etd: String? = null
    @field:Element(name = "platform", required = false) var platform: Int? = null
    @field:Element(name = "operator", required = false) var operator: String? = null
    @field:Element(name = "operatorCode", required = false) var operatorCode: String? = null
    @field:Element(name = "serviceType", required = false) var serviceType: String? = null
    @field:Element(name = "serviceID", required = false) var serviceID: String? = null
    @field:Path("origin") @field:Element(name = "location") var origin: Location? = null
    @field:Path("destination") @field:Element(name = "location") var destination: Location? = null
}

@Root(name = "location", strict = false)
class Location {
    @field:Element(name = "locationName", required = false) var locationName: String? = null
    @field:Element(name = "crs", required = false) var stationCode: String? = null
    @field:Element(name = "via", required = false) var via: String? = null
}