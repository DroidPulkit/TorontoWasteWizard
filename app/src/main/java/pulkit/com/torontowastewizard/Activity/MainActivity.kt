package pulkit.com.torontowastewizard.Activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import org.json.JSONObject

import java.util.HashMap

import pulkit.com.torontowastewizard.R

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var createAccount: TextView
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var login: Button
    internal var strEmail = ""
    internal var strPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        createAccount = findViewById(R.id.textViewCreateAccount)
        email = findViewById(R.id.editTextEmail)
        password = findViewById(R.id.editTextPassword)
        login = findViewById(R.id.buttonLogin)

        createAccount.text = fromHtml("<font color='#ffffff'>I don't have account yet. </font><font color='#00b8d4'>create one</font>")

        createAccount.setOnClickListener(this)
        login.setOnClickListener(this)
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

    internal fun extractData() {
        strEmail = email.text.toString()
        strPassword = password.text.toString()
    }

    internal fun validateData(): Boolean {
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

    internal fun callServer() {

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


    }

    companion object {

        private val TAG = MainActivity::class.java.simpleName

        fun fromHtml(html: String): Spanned {
            val result: Spanned
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            } else {
                result = Html.fromHtml(html)
            }
            return result
        }
    }
}
