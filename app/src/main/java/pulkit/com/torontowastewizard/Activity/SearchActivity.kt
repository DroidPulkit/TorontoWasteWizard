package pulkit.com.torontowastewizard.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import java.util.ArrayList
import pulkit.com.torontowastewizard.Adapter.WasteAdapter
import pulkit.com.torontowastewizard.Model.Waste
import pulkit.com.torontowastewizard.R
import pulkit.com.torontowastewizard.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    companion object {
        private val TAG: String = SearchActivity::class.java.simpleName
    }

    private lateinit var binding : ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val wasteArrayList = intent.getSerializableExtra("wasteArrayList") as ArrayList<Waste>
        val adapter = WasteAdapter(wasteArrayList)
        val layoutManager = LinearLayoutManager(this)
        val itemDecor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecor.setDrawable(this.resources.getDrawable(R.drawable.line_divider))

        binding.wasteRecyclerView.apply {
            this.layoutManager = layoutManager
            addItemDecoration(itemDecor)
            this.adapter = adapter
        }
    }
}
