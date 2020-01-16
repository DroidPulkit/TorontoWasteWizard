package pulkit.com.torontowastewizard.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import pulkit.com.torontowastewizard.R
import pulkit.com.torontowastewizard.activity.Common
import pulkit.com.torontowastewizard.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = HomeActivity::class.java.simpleName

    private var name = Common.name
    private lateinit var binding: ActivityHomeBinding

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

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

                    homeViewModel.callServer(this@HomeActivity, getQuery())

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
                homeViewModel.favoriteMenuOnSelect(this@HomeActivity)
            }
            R.id.menu_logout -> {
                homeViewModel.logout(this@HomeActivity)
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
}
