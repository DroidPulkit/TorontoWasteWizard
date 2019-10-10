package pulkit.com.torontowastewizard.activity

import androidx.databinding.DataBindingUtil
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.WindowManager
import java.util.ArrayList
import pulkit.com.torontowastewizard.adapter.FavWasteAdapter
import pulkit.com.torontowastewizard.model.Waste
import pulkit.com.torontowastewizard.R
import pulkit.com.torontowastewizard.databinding.ActivityFavBinding

class FavActivity : AppCompatActivity() {

    private val TAG: String = FavActivity::class.java.simpleName

    private lateinit var binding : ActivityFavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_fav)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val wasteArrayList = intent.getSerializableExtra("wasteArrayList") as ArrayList<Waste>
        val layoutManager = LinearLayoutManager(this)
        val itemDecor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecor.setDrawable(this.resources.getDrawable(R.drawable.line_divider))
        val adapter = FavWasteAdapter(wasteArrayList)

        binding.wasteRecyclerViewFav.apply {
            this.layoutManager = layoutManager
            addItemDecoration(itemDecor)
            this.adapter = adapter
        }

    }
}
