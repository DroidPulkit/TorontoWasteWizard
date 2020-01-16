package pulkit.com.torontowastewizard.ui.home

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pulkit.com.torontowastewizard.R
import pulkit.com.torontowastewizard.activity.FavActivity
import pulkit.com.torontowastewizard.activity.SearchActivity
import pulkit.com.torontowastewizard.activity.SplashActivity
import pulkit.com.torontowastewizard.datasource.DataSourceProvider
import pulkit.com.torontowastewizard.model.Waste

class HomeViewModel : ViewModel() {

    private val wasteDataSource = DataSourceProvider().wasteDataSource()

    private val scope = CoroutineScope(Dispatchers.Main)

    fun callServer(context: Context, query: String) {
        scope.launch {

            try {
                val wasteList = with(Dispatchers.IO) {
                    wasteDataSource.searchWaste(query)
                }


                val intent = Intent(context, SearchActivity::class.java)
                intent.putExtra("wasteArrayList", wasteList as ArrayList<Waste>)
                context.startActivity(intent)


            } catch (exception: Exception) {
                exception.printStackTrace()
                Toast.makeText(context, context.getString(R.string.error_keyword_search_message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun favoriteMenuOnSelect(context: Context) {
        val sharedPreferences3 = context.getSharedPreferences("FavSharedPreferences", Context.MODE_PRIVATE)
        val stringSet = sharedPreferences3.getStringSet("fav", null)
        if (stringSet != null) {

            if (stringSet.size > 0) {
                val intent = Intent(context, FavActivity::class.java)
                context.startActivity(intent)

                //                        favWasteArrayList.clear()
                //
                //                        val arrayList = ArrayList<String>()
                //                        for (str in stringSet) {
                //                            arrayList.add(str)
                //                        }
                //
                //                        val size = arrayList.size
                //
                //                        for (i in 0 until size) {
                //                            val title = arrayList[i]
                //                            callServerForFav(title, i, size)
                //                        }
                //
                //
                //
                //                        isLoopFinished = true

            } else {
                Toast.makeText(context, context.getString(R.string.no_waste_added_to_fav), Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(context, context.getString(R.string.no_waste_added_to_fav), Toast.LENGTH_SHORT).show()
        }
    }

    fun logout(context: Context) {
        val sharedPreferences = context.getSharedPreferences("loginSharedPreference", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("username")
        editor.apply()

        val intent = Intent(context, SplashActivity::class.java)
        context.startActivity(intent)
    }
}