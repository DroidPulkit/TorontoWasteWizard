package pulkit.com.torontowastewizard.ui.main

import android.content.Context
import android.content.Intent
import android.text.Html
import android.text.Spanned
import android.widget.Toast
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pulkit.com.torontowastewizard.R
import pulkit.com.torontowastewizard.activity.RegisterActivity
import pulkit.com.torontowastewizard.datasource.DataSourceProvider
import pulkit.com.torontowastewizard.ui.home.HomeActivity

class MainViewModel : ViewModel() {
    private val dataSourceProvider = DataSourceProvider()

    private val userDataSource = dataSourceProvider.userDataSource()

    val scope = CoroutineScope(Dispatchers.Main)

    fun createAccountOnClick(context: Context) {
        val intent = Intent(context, RegisterActivity::class.java)
        context.startActivity(intent)
    }

    fun callServer(context: Context, email: String, password: String) {
        scope.launch {
            try {
                with(Dispatchers.IO) {
                    userDataSource.login(email, password)
                }

                Toast.makeText(context, context.getString(R.string.login_success_message), Toast.LENGTH_SHORT).show()
                val sharedPreferences = context.getSharedPreferences("loginSharedPreference", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("username", email)
                editor.apply()
                val intent = Intent(context, HomeActivity::class.java)
                context.startActivity(intent)

            } catch (exception: Exception) {
                exception.printStackTrace()
                Toast.makeText(context, context.getString(R.string.login_failed_message), Toast.LENGTH_SHORT).show()
            }
        }
    }

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