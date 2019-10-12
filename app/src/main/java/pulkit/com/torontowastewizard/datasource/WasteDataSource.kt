package pulkit.com.torontowastewizard.datasource

import pulkit.com.torontowastewizard.model.Waste

/**
 * Created by Vincent on 2019-10-12
 */
interface WasteDataSource {

    suspend fun searchWaste(query: String): List<Waste>

}