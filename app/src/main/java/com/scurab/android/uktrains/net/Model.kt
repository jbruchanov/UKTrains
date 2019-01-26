package com.scurab.android.uktrains.net

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

class StationBoardResult {
    @field:Element(name = "generatedAt") lateinit var generatedAt: String
    @field:Element(name = "locationName") lateinit var locationName: String
    @field:Element(name = "crs") lateinit var stationCode: String
    @field:Element(name = "platformAvailable", required = false) var platformAvailable: Boolean? = null
    @field:ElementList(name = "trainServices", required = false) var trainServices: List<TrainService>? = null
}

@Root(name = "service", strict = false)
class TrainService {
    @field:Element(name = "platform", required = false) var platform: Int? = null
    @field:Element(name = "operator", required = false) var operator: String? = null
    @field:Element(name = "operatorCode", required = false) var operatorCode: String? = null
    @field:Element(name = "serviceType", required = false) var serviceType: String? = null
    @field:Element(name = "serviceID", required = false) var serviceID: String? = null
    @field:Path("origin") @field:Element(name = "location") lateinit var origin: Location
    @field:Path("destination") @field:Element(name = "location") lateinit var destination: Location
    //departure
    @field:Element(name = "std", required = false) var stDeparture: String? = null
    @field:Element(name = "etd", required = false) var etDeparture: String? = null
    @field:Path("subsequentCallingPoints") @field:ElementList(name = "callingPointList", required = false) var subsequentCallingPoints: List<CallingPoint>? = null
    //arrival
    @field:Element(name = "sta", required = false) var stArrival: String? = null
    @field:Element(name = "eta", required = false) var etArrival: String? = null
    @field:Path("previousCallingPoints") @field:ElementList(name = "callingPointList", required = false) var previousCallingPoints: List<CallingPoint>? = null

    val st: String? get() = stDeparture ?: stArrival
    val et: String? get() = etDeparture ?: etArrival
    val callingPoints: List<CallingPoint>? get() = subsequentCallingPoints ?: previousCallingPoints
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
    @field:Element(name = "at", required = false) lateinit var timeDetailArrival: String
    @field:Element(name = "et", required = false) lateinit var timeDetailDeparture: String
}

class ServiceDetailsResult {
    @field:Element(name = "generatedAt") lateinit var generatedAt: String
    @field:Element(name = "serviceType") lateinit var serviceType: String
    @field:Element(name = "locationName") lateinit var locationName: String
    @field:Element(name = "crs") lateinit var stationCode:String
    @field:Element(name = "operator") lateinit var operator: String
    @field:Element(name = "operatorCode") lateinit var operatorCode: String
    @field:Element(name = "platform", required = false) var platform: Int? = null
    @field:Element(name = "sta") lateinit var sta: String
    @field:Element(name = "ata") lateinit var ata: String
    @field:Element(name = "std") lateinit var std: String
    @field:Element(name = "atd") lateinit var atd: String
    @field:Path("previousCallingPoints") @field:ElementList(name = "callingPointList", required = false) var previousCallingPoints: List<CallingPoint>? = null
    @field:Path("subsequentCallingPoints") @field:ElementList(name = "callingPointList", required = false) var subsequentCallingPoints: List<CallingPoint>? = null
}