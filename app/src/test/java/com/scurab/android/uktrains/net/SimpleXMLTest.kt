package com.scurab.android.uktrains.net

import junit.framework.Assert
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
    fun testDeserialization() {
        val xml = File("./src/test/res/sample_xml1.xml").readText()
        val clz: Class<Envelope<DepartureBoardResponse>> =
            Envelope::class.java as Class<Envelope<DepartureBoardResponse>>
        val envelope = persister.read(clz, xml, false)
        envelope.body.item.apply {
            Assert.assertNotNull(this.stationBoardResult)
        }
    }
}