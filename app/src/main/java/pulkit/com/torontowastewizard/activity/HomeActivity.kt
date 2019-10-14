package pulkit.com.torontowastewizard.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pulkit.com.torontowastewizard.R
import pulkit.com.torontowastewizard.databinding.ActivityHomeBinding
import pulkit.com.torontowastewizard.datasource.DataSourceProvider
import pulkit.com.torontowastewizard.model.Waste

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = HomeActivity::class.java.simpleName

    private var name = Common.name
    private lateinit var binding: ActivityHomeBinding

    private val wasteDataSource = DataSourceProvider().wasteDataSource()


    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val myToolbar = binding.myToolbar
        setSupportActionBar(myToolbar)

        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // Create the adapter and set it to the AutoCompleteTextView
        //I am just saying, this is going to give terrible performance, but due
        //to time constraints for this assignment, I am skipping the custom adapter
        //So it's going to eat a lot of memory
        //Will improve in future. :)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, name)
        binding.autoCompleteWasteName.setAdapter(adapter)

        binding.buttonSubmit.setOnClickListener(this)
        binding.buttonScan.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.buttonSubmit -> {
                if (dataValid()) {
                    //do something
                    //parse DB

                    callServer()

                }
            }
            R.id.buttonScan -> {
            }
            else -> {
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_fav -> {
                val sharedPreferences3 =
                        this.getSharedPreferences("FavSharedPreferences", Context.MODE_PRIVATE)
                val stringSet = sharedPreferences3.getStringSet("fav", null)
                if (stringSet != null) {

                    if (stringSet.size > 0) {
                        val intent = Intent(this@HomeActivity, FavActivity::class.java)
                        startActivity(intent)

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
                        Toast.makeText(this, "No waste added to Favourite", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this, "No waste added to Favourite", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.menu_logout -> {
                val sharedPreferences = getSharedPreferences("loginSharedPreference", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.remove("username")
                editor.apply()

                val intent = Intent(this@HomeActivity, SplashActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return true
    }

    private fun dataValid(): Boolean {

        var isValid = true

        if (getQuery().isEmpty()) {
            isValid = false
        }

        return isValid
    }

    private fun getQuery(): String {
        return binding.autoCompleteWasteName.text.toString()
    }

    private fun callServer() {
        scope.launch {

            try {
                val wasteList = with(Dispatchers.IO) {
                    wasteDataSource.searchWaste(getQuery())
                }


                val intent = Intent(this@HomeActivity, SearchActivity::class.java)
                intent.putExtra("wasteArrayList", wasteList as ArrayList<Waste>)
                startActivity(intent)


            } catch (exception: Exception) {
                exception.printStackTrace()
                Toast.makeText(this@HomeActivity, "Error finding the keyword", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
