package pulkit.com.torontowastewizard.activity

import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import pulkit.com.torontowastewizard.R
import pulkit.com.torontowastewizard.databinding.ActivityMainBinding
import pulkit.com.torontowastewizard.datasource.DataSourceProvider

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG:String = MainActivity::class.java.simpleName

    private lateinit var binding: ActivityMainBinding

    private val dataSourceProvider = DataSourceProvider()

    private val userDataSource = dataSourceProvider.userDataSource()

    private val scope = CoroutineScope(Dispatchers.Main)

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
                if (validateData()) {
                    //Send data to server to check to login
                    callServer()
                }
            }
            else -> {
            }
        }
    }


    private fun getEmail(): String {
        return binding.editTextEmail.text.toString()
    }

    private fun getPassword(): String {
        return binding.editTextEmail.text.toString()
    }

    private fun validateData(): Boolean {
        var isDataRight = true

        if (getEmail().isEmpty()) {
            isDataRight = false
        } else if (getPassword().isEmpty()) {
            isDataRight = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches()) {
            isDataRight = false
        }

        return isDataRight
    }

    private fun callServer() {

        scope.launch {

            try {
                with(Dispatchers.IO) {

                    userDataSource.login(
                            getEmail(),
                            getPassword()
                    )
                }

                Toast.makeText(this@MainActivity, "Login Successfully", Toast.LENGTH_SHORT).show()
                val sharedPreferences = getSharedPreferences("loginSharedPreference", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("username", getEmail())
                editor.apply()
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()

            } catch (exception: Exception) {
                exception.printStackTrace()
                Toast.makeText(this@MainActivity, "Login failed, check username or password", Toast.LENGTH_SHORT).show()
            }
        }
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

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}
