package pulkit.com.torontowastewizard.retrofit

import pulkit.com.torontowastewizard.datasource.UserDataSource
import retrofit2.Retrofit

/**
 * Created by Vincent on 2019-10-12
 */
class RealUserDataSource
constructor(retrofit: Retrofit) : UserDataSource {

  private val wasteApi = retrofit.create(WasteApi::class.java)

  override suspend fun register(name: String, email: String, password: String, zipCode: String) {
    return wasteApi.register(name, email, password, zipCode)
  }

  override suspend fun login(email: String, password: String) {
    return wasteApi.login(email, password)
  }

}

