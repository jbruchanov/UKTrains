package com.scurab.android.uktrains.net

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


class BoardResponse {
    @field:Element(name = "GetStationBoardResult")
    var stationBoardResult: StationBoardResult? = null
}

/*@Root(name = "GetDepartureBoardResponse", strict = false)
class DepartureBoardResponse : BoardResponse()

@Root(name = "GetDepBoardWithDetailsResponse", strict = false)
class DepartureBoardWithDetailsResponse : BoardResponse()*/

