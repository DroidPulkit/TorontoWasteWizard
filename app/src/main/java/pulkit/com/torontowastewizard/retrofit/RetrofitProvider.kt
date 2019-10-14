package pulkit.com.torontowastewizard.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import pulkit.com.torontowastewizard.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Vincent on 2019-10-12
 * This should be injected with dagger but for now, going with this approach
 */
object RetrofitProvider {

  private var retrofit: Retrofit? = null

  fun retrofit(): Retrofit {

    if (retrofit == null) {
      val okHttpClientBuilder = OkHttpClient.Builder()

      if (BuildConfig.DEBUG) {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = BODY

        okHttpClientBuilder.addNetworkInterceptor(loggingInterceptor)

      }

      val baseUrl = "https://pulkitkumar.me/api/"

      retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    }

    return retrofit!!
  }

}

