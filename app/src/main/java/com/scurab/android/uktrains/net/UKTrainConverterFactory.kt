package com.scurab.android.uktrains.net

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.io.OutputStreamWriter
import java.lang.reflect.Type

class UKTrainConverterFactory(
    private val accessToken: String,
    private val serializer: Serializer = Persister(AnnotationStrategy())
) : Converter.Factory() {

    private val header = EnvelopeHeader()
        .apply { accessToken = AccessToken(this@UKTrainConverterFactory.accessToken) }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return XmlRequestConverter(serializer, header)
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val clz = type as? Class<*>
        return clz?.let {
            XmlResponseBodyConverter(type, serializer, false)
        }
    }
}

class XmlRequestConverter(
    private val serializer: Serializer,
    private val header: EnvelopeHeader
) : Converter<Any, RequestBody> {
    override fun convert(item: Any): RequestBody {
        val envelope = Envelope<Any>().apply {
            this.header = this@XmlRequestConverter.header
            this.body = EnvelopeBody(item)
        }
        val buffer = Buffer()
        val osw = OutputStreamWriter(buffer.outputStream(), "utf-8")
        serializer.write(envelope, osw)
        osw.flush()
        return RequestBody.create(MediaType.get("text/xml"), buffer.readByteString())
    }
}

class XmlResponseBodyConverter<T : Any>(
    private val cls: Class<T>,
    private val serializer: Serializer,
    private val strict: Boolean
) : Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        value.use { value ->
            val response = value.charStream().readText()
            @Suppress("UNCHECKED_CAST")
            val read = serializer.read(Envelope::class.java as Class<Envelope<T>>, response, strict)
            return read?.body?.item
                ?: throw IllegalStateException("Could not deserialize body as $cls")
        }
    }
}