package com.scurab.android.uktrains.net

import junit.framework.Assert
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStreamWriter

class SimpleXMLTest {

    private val persister = Persister(AnnotationStrategy())

    @Test
    fun testSerialization() {
        val value = Envelope<DepartureBoardRequest>().apply {
            body = EnvelopeBody<DepartureBoardRequest>().apply {
                header = EnvelopeHeader().apply { accessToken = AccessToken("XYZ") }
                item = DepartureBoardRequest("GNH", 10)
            }
        }

        val persister = Persister(AnnotationStrategy())
        val buffer = ByteArrayOutputStream()
        val osw = OutputStreamWriter(buffer, "utf-8")
        persister.write(value, osw)
        osw.flush()
        val message = buffer.toString("utf-8")
        println(message)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun testDeserializationGetDepartureBoard() {
        val xml = File("./src/test/res/sample_get_departure_board.xml").readText()
        val clz: Class<Envelope<BoardResponse>> =
            Envelope::class.java as Class<Envelope<BoardResponse>>
        val envelope = persister.read(clz, xml, false)
        envelope.body.item.apply {
            Assert.assertNotNull(this.stationBoardResult)
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun testDeserializationGetDepartureBoardWithDetails() {
        val xml = File("./src/test/res/sample_get_departure_board_with_details.xml").readText()
        val clz: Class<Envelope<BoardResponse>> =
            Envelope::class.java as Class<Envelope<BoardResponse>>
        val envelope = persister.read(clz, xml, false)
        envelope.body.item.apply {
            Assert.assertNotNull(this.stationBoardResult)
            this.stationBoardResult?.trainServices?.first()?.let {
                assertNotNull(it)
                assertTrue(it.subsequentCallingPoints?.isNotEmpty() ?: false)
                assertNotNull(it.subsequentCallingPoints?.first()?.locationName)
            }
        }
    }
}