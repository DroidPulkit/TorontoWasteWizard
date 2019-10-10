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
import androidx.databinding.DataBindingUtil
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import org.json.JSONObject

import java.util.HashMap

import pulkit.com.torontowastewizard.R
import pulkit.com.torontowastewizard.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private val TAG: String = RegisterActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityRegisterBinding

    internal var strEmail = ""
    internal var strPassword = ""
    internal var strUsername = ""
    internal var strZip = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding.buttonRegister.setOnClickListener(this)
        binding.textViewLogin.setOnClickListener(this)
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

    private fun extractData() {
        strEmail = binding.editTextEmail.text.toString()
        strPassword = binding.editTextPassword.text.toString()
        strUsername = binding.editTextUserName.text.toString()
        strZip = binding.editTextZipCode.text.toString()
    }

    private fun dataValidOrNot(): Boolean {
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

    private fun callServer() {

        val queue = Volley.newRequestQueue(this)

        val url = "https://pulkitkumar.me/api/register.php"

        val listener = Response.Listener<String> { response ->
            Log.d(TAG, "onResponse: $response")
            try {
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")
                if (success == "1") {
                    //Register Successful
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
}
