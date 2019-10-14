package pulkit.com.torontowastewizard.activity

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import pulkit.com.torontowastewizard.R
import pulkit.com.torontowastewizard.databinding.ActivityRegisterBinding
import pulkit.com.torontowastewizard.datasource.DataSourceProvider

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private val TAG: String = RegisterActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityRegisterBinding

    private val dataSourceProvider = DataSourceProvider()

    private val userDataSource = dataSourceProvider.userDataSource()

    private val scope = CoroutineScope(Dispatchers.Main)


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

    private fun getEmail(): String {
        return binding.editTextEmail.text.toString()
    }


    private fun getPassword(): String {
        return binding.editTextPassword.text.toString()
    }


    private fun getUsername(): String {
        return binding.editTextUserName.text.toString()
    }

    private fun getZipCode(): String {
        return binding.editTextZipCode.text.toString()
    }


    private fun dataValidOrNot(): Boolean {
        var validity = true

        if (getUsername().isEmpty()) {
            validity = false
        } else if (getEmail().isEmpty()) {
            validity = false
        } else if (getPassword().isEmpty()) {
            validity = false
        } else if (getZipCode().isEmpty()) {
            validity = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches()) {
            validity = false
        }

        return validity
    }

    private fun callServer() {
        scope.launch {

            try {
                with(Dispatchers.IO) {

                    userDataSource.register(
                            getUsername(),
                            getEmail(),
                            getPassword(),
                            getZipCode()
                    )
                }

                Toast.makeText(this@RegisterActivity, "Registered Successfully", Toast.LENGTH_SHORT).show()
                finish()


            } catch (exception: Exception) {
                exception.printStackTrace()
                Toast.makeText(this@RegisterActivity, "Registeration failed, used same email address before", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}
