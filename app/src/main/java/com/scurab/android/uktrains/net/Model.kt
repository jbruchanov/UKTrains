package com.scurab.android.uktrains.net

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "service", strict = false)
class TrainService {
    @field:Element(name = "std", required = false) var std: String? = null
    @field:Element(name = "etd", required = false) var etd: String? = null
    @field:Element(name = "platform", required = false) var platform: Int? = null
    @field:Element(name = "operator", required = false) var operator: String? = null
    @field:Element(name = "operatorCode", required = false) var operatorCode: String? = null
    @field:Element(name = "serviceType", required = false) var serviceType: String? = null
    @field:Element(name = "serviceID", required = false) var serviceID: String? = null
    @field:Path("origin") @field:Element(name = "location") lateinit var origin: Location
    @field:Path("destination") @field:Element(name = "location") lateinit var destination: Location
    @field:Path("subsequentCallingPoints") @field:ElementList(name = "callingPointList") lateinit var callingPoints: List<CallingPoint>
}

@Root(name = "location", strict = false)
class Location {
    @field:Element(name = "locationName") lateinit var locationName: String
    @field:Element(name = "crs") lateinit var stationCode: String
    @field:Element(name = "via", required = false) var via: String? = null
}

@Root(name = "callingPoint", strict = false)
class CallingPoint {
    @field:Element(name = "locationName") lateinit var locationName: String
    @field:Element(name = "crs") lateinit var stationCode: String
    @field:Element(name = "st") lateinit var time: String
    @field:Element(name = "et") lateinit var timeDetail: String
}