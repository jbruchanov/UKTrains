package com.scurab.android.uktrains.model

import com.scurab.android.uktrains.net.DepartureBoardRequest
import com.scurab.android.uktrains.util.npe
import java.io.Serializable

class StationBoard : HasSavingId, Serializable {
    //filled after saving
    override var id: Long? = null
    var origin: TrainStation? = null
    var destination: TrainStation? = null
    var filterType: String? = null
    var numRows: Int = 10


    operator fun component1(): Long? = id
    operator fun component2(): TrainStation? = origin
    operator fun component3(): TrainStation? = destination

    fun toRequest(): DepartureBoardRequest {
        val code = origin?.code ?: npe("Origin code station is null")
        return DepartureBoardRequest(code, numRows).let { req ->
            destination?.let { station ->
                req.filterStationCode = station.code
                req.filterType = filterType ?: "to"
            }
            req.rowsNumber = numRows
            req
        }
    }
}