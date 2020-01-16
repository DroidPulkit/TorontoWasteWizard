package pulkit.com.torontowastewizard.ui.main

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.cancel
import pulkit.com.torontowastewizard.R
import pulkit.com.torontowastewizard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG:String = MainActivity::class.java.simpleName

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding.textViewCreateAccount.text = mainViewModel.fromHtml("<font color='#ffffff'>I don't have account yet. </font><font color='#00b8d4'>create one</font>")

        binding.textViewCreateAccount.setOnClickListener(this)
        binding.buttonLogin.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.textViewCreateAccount -> {
                mainViewModel.createAccountOnClick(this@MainActivity)
            }
            R.id.buttonLogin -> {
                if (validateData()) {
                    //Send data to server to check to login
                    mainViewModel.callServer(this@MainActivity, getEmail(), getPassword())
                    finish()
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

    override fun onDestroy() {
        mainViewModel.scope.cancel()
        super.onDestroy()
    }
}
