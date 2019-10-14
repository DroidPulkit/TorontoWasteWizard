package pulkit.com.torontowastewizard.datasource

/**
 * Created by Vincent on 2019-10-12
 */
interface UserDataSource {

  suspend fun register(name: String, email: String, password: String, zipCode: String)

  suspend fun login(email: String, password: String)

}