package pulkit.com.torontowastewizard.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import org.json.JSONObject

import java.util.HashMap

import pulkit.com.torontowastewizard.R

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var username: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var zip: EditText
    lateinit var register: Button
    lateinit var goBack: TextView
    internal var strEmail = ""
    internal var strPassword = ""
    internal var strUsername = ""
    internal var strZip = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        username = findViewById(R.id.editTextUserName)
        email = findViewById(R.id.editTextEmail)
        password = findViewById(R.id.editTextPassword)
        zip = findViewById(R.id.editTextZipCode)

        register = findViewById(R.id.buttonRegister)
        goBack = findViewById(R.id.textViewLogin)

        register.setOnClickListener(this)
        goBack.setOnClickListener(this)


    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.buttonRegister -> {
                extractData()
                if (dataValidOrNot()) {
                    //Call server to register
                    callServer()
                } else {
                    Toast.makeText(this, "Data not valid", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.textViewLogin -> finish()
            else -> {
            }
        }
    }

    internal fun extractData() {
        strEmail = email.text.toString()
        strPassword = password.text.toString()
        strUsername = username.text.toString()
        strZip = zip.text.toString()
    }

    internal fun dataValidOrNot(): Boolean {
        var validity = true

        if (strUsername.isEmpty()) {
            validity = false
        } else if (strEmail.isEmpty()) {
            validity = false
        } else if (strPassword.isEmpty()) {
            validity = false
        } else if (strZip.isEmpty()) {
            validity = false
        } else if (android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches() == false) {
            validity = false
        }

        return validity
    }

    internal fun callServer() {

        val queue = Volley.newRequestQueue(this)

        val url = "https://pulkitkumar.me/api/register.php"

        val listener = Response.Listener<String> { response ->
            Log.d(TAG, "onResponse: $response")
            try {
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")
                if (success == "1") {
                    //Register Successfull
                    Toast.makeText(this@RegisterActivity, "Registered Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, "Registeration failed, used same email address before", Toast.LENGTH_SHORT).show()
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
                params["name"] = strUsername
                params["email"] = strEmail
                params["password"] = strPassword
                params["zip"] = strZip

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

        private val TAG = RegisterActivity::class.java.simpleName
    }
}
