package com.scurab.android.uktrains.net

import org.simpleframework.xml.*

internal const val NS_LDB = "http://thalesgroup.com/RTTI/2016-02-16/ldb/"
internal const val NS_TYPES = "http://thalesgroup.com/RTTI/2013-11-28/Token/types"
internal const val NS_SOAP = "http://www.w3.org/2003/05/soap-envelope"

@Root(name = "Envelope")
@Namespace(prefix = "soap", reference = NS_SOAP)
@NamespaceList(
    Namespace(reference = NS_LDB, prefix = "ldb"),
    Namespace(reference = NS_TYPES, prefix = "types")
)
class Envelope<T : Any> {
    //must be before body
    @field:Namespace(reference = NS_SOAP)
    @field:Element(name = "Header", required = false)
    var header: EnvelopeHeader? = null

    @field:Namespace(reference = NS_SOAP)
    @field:Element(name = "Body", required = true)
    lateinit var body: EnvelopeBody<T>
}


class EnvelopeHeader {
    @field:Namespace(reference = NS_TYPES)
    @field:Element(name = "AccessToken")
    var accessToken: AccessToken? = null
}

class AccessToken(
    @field:Namespace(reference = NS_TYPES)
    @field:Element(name = "TokenValue") var accessToken: String
)

@Namespace(reference = NS_SOAP)
class EnvelopeBody<T : Any>() {

    @field:Namespace(reference = NS_LDB)
    @field:ElementUnion(
        Element(name = "GetDepartureBoardRequest", type = DepartureBoardRequest::class),
        Element(name = "GetDepartureBoardResponse", type = BoardResponse::class),
        Element(name = "GetDepBoardWithDetailsRequest", type = DepartureBoardRequestWithDetails::class),
        Element(name = "GetDepBoardWithDetailsResponse", type = BoardResponse::class),
        Element(name = "GetArrivalBoardRequest", type = ArrivalBoardRequest::class),
        Element(name = "GetArrivalBoardResponse", type = BoardResponse::class),
        Element(name = "GetArrBoardWithDetailsRequest", type = ArrivalBoardRequestWithDetails::class),
        Element(name = "GetArrBoardWithDetailsResponse", type = BoardResponse::class)
    )
    lateinit var item: T

    constructor(item: T) : this() {
        this.item = item
    }
}