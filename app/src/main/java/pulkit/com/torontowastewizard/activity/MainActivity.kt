package pulkit.com.torontowastewizard.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.WindowManager
import pulkit.com.torontowastewizard.R
import pulkit.com.torontowastewizard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG:String = MainActivity::class.java.simpleName

    private lateinit var binding: ActivityMainBinding

    private var strEmail = ""
    private var strPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding.textViewCreateAccount.text = fromHtml("<font color='#ffffff'>I don't have account yet. </font><font color='#00b8d4'>create one</font>")

        binding.textViewCreateAccount.setOnClickListener(this)
        binding.buttonLogin.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.textViewCreateAccount -> {
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
            R.id.buttonLogin -> {
                extractData()
                if (validateData()) {
                    //Send data to server to check to login
                    callServer()
                }
            }
            else -> {
            }
        }
    }

    private fun extractData() {
        strEmail = binding.editTextEmail.text.toString()
        strPassword = binding.editTextPassword.text.toString()
    }

    private fun validateData(): Boolean {
        var isDataRight = true

        if (strEmail.isEmpty()) {
            isDataRight = false
        } else if (strPassword.isEmpty()) {
            isDataRight = false
        } else if (android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches() == false) {
            isDataRight = false
        }

        return isDataRight
    }

    private fun callServer() {
        //TODO: ADD Retrofit code
        /*
        val queue = Volley.newRequestQueue(this)

        val url = "https://pulkitkumar.me/api/login.php"

        val listener = Response.Listener<String> { response ->
            Log.d(TAG, "onResponse: $response")
            try {
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")
                if (success == "1") {
                    //Register Successfull
                    Toast.makeText(this@MainActivity, "Login Successfully", Toast.LENGTH_SHORT).show()
                    val sharedPreferences = getSharedPreferences("loginSharedPreference", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("username", strEmail)
                    editor.apply()
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@MainActivity, "Login failed, check username or password", Toast.LENGTH_SHORT).show()
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
                params["email"] = strEmail
                params["password"] = strPassword

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
         */

    }

    private fun fromHtml(html: String): Spanned {
        val result: Spanned
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            result = Html.fromHtml(html)
        }
        return result
    }
}
