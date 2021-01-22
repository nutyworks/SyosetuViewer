package me.nutyworks.syosetuviewerv2.network

import org.apache.commons.codec.binary.Base64
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object PapagoRequester {
    private val mDeviceId = UUID.randomUUID()
    private const val mKey = "v1.5.3_e6026dbabc"

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://papago.naver.com/apis/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(PapagoService::class.java)

    fun request(source: String): String {
        val timestamp = System.currentTimeMillis()
        return service.translate(
            generateAuthorizationToken(timestamp),
            timestamp,
            mDeviceId.toString(),
            "ja",
            "ko",
            source.replace("(・)", "")
        ).execute().body()?.translatedText
            ?.replace("&lt;", "<")
            ?.replace("&gt;", ">")
            ?.replace("&amp;", "&") ?: ""
    }

    private fun generateAuthorizationToken(timestamp: Long): String {
        val hash = HMac.hmacmd5(
            mKey,
            "$mDeviceId\nhttps://papago.naver.com/apis/n2mt/translate\n$timestamp"
        ).let {
//            Base64.encodeToString(it, Base64.DEFAULT).trimEnd()
            Base64.encodeBase64String(it).trimEnd()
        }
        return "PPG $mDeviceId:$hash"
    }

    private object HMac {

        fun hmacmd5(key: String, message: String): ByteArray {
            return hmacmd5(
                key.toByteArray(),
                message.toByteArray()
            )
        }

        fun hmacmd5(key: ByteArray, message: ByteArray): ByteArray {
            val mac = Mac.getInstance("HmacMD5")
            val sks = SecretKeySpec(key, "HmacMD5")
            mac.init(sks)
            val hmacmd5 = mac.doFinal(message)

            return hmacmd5
        }
    }
}

interface PapagoService {
    @POST("n2mt/translate")
    @FormUrlEncoded
    fun translate(
        @Header("Authorization") authorization: String,
        @Header("Timestamp") timestamp: Long,
        @Field("deviceId") deviceId: String,
        @Field("source") source: String,
        @Field("target") target: String,
        @Field("text") text: String,
        @Field("locale") locale: String = "en-US",
        @Field("dict") dict: Boolean = false,
        @Field("dictDisplay") dictDisplay: Int = 0,
        @Field("honorific") honorific: Boolean = false,
        @Field("instant") instant: Boolean = false,
        @Field("paging") paging: Boolean = false,
    ): Call<TranslatedData>
}

data class TranslatedData(
    val translatedText: String,
)
