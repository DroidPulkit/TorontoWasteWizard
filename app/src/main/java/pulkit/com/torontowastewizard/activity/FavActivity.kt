package pulkit.com.torontowastewizard.activity

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pulkit.com.torontowastewizard.R
import pulkit.com.torontowastewizard.adapter.FavWasteAdapter
import pulkit.com.torontowastewizard.databinding.ActivityFavBinding
import pulkit.com.torontowastewizard.datasource.DataSourceProvider

class FavActivity : AppCompatActivity() {

    private val TAG: String = FavActivity::class.java.simpleName

    private lateinit var binding: ActivityFavBinding

    private val wasteDataSource = DataSourceProvider().wasteDataSource()

    private val scope = CoroutineScope(Dispatchers.Main)

    private val favWasteAdapter = FavWasteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_fav)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val layoutManager = LinearLayoutManager(this)
        val itemDecor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecor.setDrawable(this.resources.getDrawable(R.drawable.line_divider))


        binding.wasteRecyclerViewFav.apply {
            this.layoutManager = layoutManager
            addItemDecoration(itemDecor)
            this.adapter = favWasteAdapter
        }

        getFavoriteList()

    }

    fun getFavoriteList() {

        val sharedPreferences3 =
                this.getSharedPreferences("FavSharedPreferences", Context.MODE_PRIVATE)
        val stringSet = sharedPreferences3.getStringSet("fav", null)
        if (stringSet != null) {


            val arrayList = ArrayList<String>()
            for (str in stringSet) {
                arrayList.add(str)
            }

            callServerForFav(arrayList)


        }
    }

    private fun callServerForFav(queryList: List<String>) {

        scope.launch {

            try {
                val wasteList = with(Dispatchers.IO) {
                    queryList.map { query ->
                        wasteDataSource.searchWaste(query)
                    }[0]
                }

                favWasteAdapter.submitList(wasteList)


            } catch (exception: Exception) {
                exception.printStackTrace()
                Toast.makeText(this@FavActivity, "Error finding the keyword", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
