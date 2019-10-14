package pulkit.com.torontowastewizard.retrofit

import pulkit.com.torontowastewizard.datasource.WasteDataSource
import pulkit.com.torontowastewizard.model.Waste
import retrofit2.Retrofit

/**
 * Created by Vincent on 2019-10-12
 */
class RealWasteDataSource constructor(retrofit: Retrofit) : WasteDataSource {

    private val wasteApi = retrofit.create(WasteApi::class.java)

    override suspend fun searchWaste(query: String): List<Waste> {
        val wasteResponse = wasteApi.search(query)
        val wasteList = wasteResponse.data.records.map {
            Waste(
                    it.body,
                    it.category,
                    it.title,
                    it.keywords
            )
        }
        return wasteList
    }

}