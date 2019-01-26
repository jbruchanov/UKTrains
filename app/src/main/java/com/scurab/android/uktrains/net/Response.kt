package com.scurab.android.uktrains.net

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "GetDepartureBoardResponse", strict = false)
open class DepartureBoardResponse {
    @field:Element(name = "GetStationBoardResult")
    var stationBoardResult: StationBoardResult? = null
}

@Root(name = "GetDepBoardWithDetailsResponse", strict = false)
class DepartureBoardWithDetailsResponse : DepartureBoardResponse()

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

    @field:ElementList(name = "trainServices", required = true)
    lateinit var trainServices: List<TrainService>
}