package pulkit.com.torontowastewizard.retrofit

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by Vincent on 2019-10-12
 */
interface WasteApi {

    @POST("register.php")
    @FormUrlEncoded
    suspend fun register(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("password") password: String,
            @Field("zip") zipCode: String
    )

    @POST("login.php")
    @FormUrlEncoded
    suspend fun login(
            @Field("email") email: String,
            @Field("password") password: String
    )

    @POST("search.php")
    @FormUrlEncoded
    suspend fun search(
            @Field("keyword") keyword: String
    ): GetWasteResponse

}