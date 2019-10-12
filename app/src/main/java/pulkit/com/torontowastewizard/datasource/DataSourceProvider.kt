package pulkit.com.torontowastewizard.datasource

import pulkit.com.torontowastewizard.retrofit.RealUserDataSource
import pulkit.com.torontowastewizard.retrofit.RealWasteDataSource
import pulkit.com.torontowastewizard.retrofit.RetrofitProvider

/**
 * Created by Vincent on 2019-10-12
 */
class DataSourceProvider {

    fun userDataSource(): UserDataSource {
        return RealUserDataSource(RetrofitProvider.retrofit())
    }

    fun wasteDataSource(): WasteDataSource {
        return RealWasteDataSource(RetrofitProvider.retrofit())
    }

}