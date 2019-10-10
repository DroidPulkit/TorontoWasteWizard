package pulkit.com.torontowastewizard.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast

import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import org.json.JSONArray
import org.json.JSONObject

import java.util.ArrayList
import java.util.HashMap

import pulkit.com.torontowastewizard.Model.Waste
import pulkit.com.torontowastewizard.R

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var wasteName: AutoCompleteTextView
    lateinit var submit: Button
    lateinit var scan: Button

    internal var strName = ""

    internal var name = Common.name

    internal var favWasteArrayList = ArrayList<Waste>()

    internal var isLoopFinished = false
    internal var isCallBackFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val myToolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(myToolbar)

        supportActionBar!!.setDisplayShowTitleEnabled(false)

        wasteName = findViewById(R.id.autoCompleteWasteName)
        submit = findViewById(R.id.buttonSubmit)
        scan = findViewById(R.id.buttonScan)

        // Create the adapter and set it to the AutoCompleteTextView
        //I am just saying, this is going to give terible performance, but due
        //to time constraints for this assignment, I am skipping the custom adapter
        //So it's going to eat a lot of memory
        //Will improve in future. :)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, name)
        wasteName.setAdapter(adapter)

        submit.setOnClickListener(this)
        scan.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.buttonSubmit -> {
                extractData()
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
                val sharedPreferences3 = this.getSharedPreferences("FavSharedPreferences", Context.MODE_PRIVATE)
                val stringSet = sharedPreferences3.getStringSet("fav", null)
                if (stringSet != null) {

                    if (stringSet.size > 0) {
                        favWasteArrayList.clear()

                        val arrayList = ArrayList<String>()
                        for (str in stringSet) {
                            arrayList.add(str)
                        }

                        val size = arrayList.size

                        for (i in 0 until size) {
                            val title = arrayList[i]
                            callServerForFav(title, i, size)
                        }

                        isLoopFinished = true


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

    internal fun extractData() {
        strName = wasteName.text.toString()
    }

    internal fun dataValid(): Boolean {

        var isValid = true

        if (strName.isEmpty()) {
            isValid = false
        }

        return isValid
    }

    internal fun callServer() {

        val queue = Volley.newRequestQueue(this)

        val url = "https://pulkitkumar.me/api/search.php"

        val listener = Response.Listener<String> { response ->
            Log.d(TAG, "onResponse: $response")
            try {
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")
                if (success == "1") {
                    //Register Successfull
                    Toast.makeText(this@HomeActivity, "Keyword found", Toast.LENGTH_SHORT).show()
                    //Intent intent = new Intent(HomeActivity.this, );
                    val data = jsonObject.getJSONObject("data")
                    val records = data.getJSONArray("records")
                    val wasteArrayList = ArrayList<Waste>()
                    for (i in 0 until records.length()) {
                        val singleRecord = records.getJSONObject(i)
                        val body = singleRecord.getString("body")
                        val category = singleRecord.getString("category")
                        val title = singleRecord.getString("title")
                        val keywords = singleRecord.getString("keywords")
                        val waste = Waste(body, category, title, keywords)
                        wasteArrayList.add(waste)
                    }

                    val intent = Intent(this@HomeActivity, SearchActivity::class.java)
                    intent.putExtra("wasteArrayList", wasteArrayList)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@HomeActivity, "Error finding the keyword", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val errorListener = Response.ErrorListener { error -> Log.d(TAG, "onErrorResponse: $error") }

        val postRequest = object : StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["keyword"] = strName

                return params
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Content-Type"] = "application/x-www-form-urlencoded"
                return params
            }
        }

        queue.add(postRequest)


    }

    internal fun callServerForFav(keyword: String, index: Int, totalSize: Int) {

        val i = index
        val t = totalSize

        val queue = Volley.newRequestQueue(this)

        val url = "https://pulkitkumar.me/api/search.php"

        val listener = Response.Listener<String> { response ->
            Log.d(TAG, "onResponse: $response")
            try {
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")
                if (success == "1") {
                    //Register Successfull
                    Toast.makeText(this@HomeActivity, "Keyword found", Toast.LENGTH_SHORT).show()
                    //Intent intent = new Intent(HomeActivity.this, );
                    val data = jsonObject.getJSONObject("data")
                    val records = data.getJSONArray("records")
                    val wasteArrayList = ArrayList<Waste>()
                    for (i in 0 until records.length()) {
                        val singleRecord = records.getJSONObject(i)
                        val body = singleRecord.getString("body")
                        val category = singleRecord.getString("category")
                        val title = singleRecord.getString("title")
                        val keywords = singleRecord.getString("keywords")
                        val waste = Waste(body, category, title, keywords)
                        wasteArrayList.add(waste)
                    }
                    favWasteArrayList.add(wasteArrayList[0])
                    isCallBackFinished = true

                    if (isCallBackFinished && isLoopFinished) {
                        val intent = Intent(this@HomeActivity, FavActivity::class.java)
                        intent.putExtra("wasteArrayList", favWasteArrayList)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@HomeActivity, "Error finding the keyword", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val errorListener = Response.ErrorListener { error -> Log.d(TAG, "onErrorResponse: $error") }

        val postRequest = object : StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["keyword"] = keyword

                return params
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Content-Type"] = "application/x-www-form-urlencoded"
                return params
            }
        }

        queue.add(postRequest)
    }

    companion object {

        private val TAG = HomeActivity::class.java.simpleName
    }
}
