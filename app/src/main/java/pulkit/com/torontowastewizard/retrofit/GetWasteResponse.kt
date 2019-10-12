package pulkit.com.torontowastewizard.retrofit

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Vincent on 2019-10-12
 */
@JsonClass(generateAdapter = true)
data class GetWasteResponse(
        @Json(name = "data") val data: GetWasteResponseData
)

@JsonClass(generateAdapter = true)
data class GetWasteResponseData(
        @Json(name = "records") val records: List<GetWasteResponseRecord>
)

@JsonClass(generateAdapter = true)
data class GetWasteResponseRecord(
        @Json(name = "body") val body: String,
        @Json(name = "category") val category: String,
        @Json(name = "title") val title: String,
        @Json(name = "keywords") val keywords: String
)