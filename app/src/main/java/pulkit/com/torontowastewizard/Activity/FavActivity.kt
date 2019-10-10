package pulkit.com.torontowastewizard.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.WindowManager

import java.util.ArrayList

import pulkit.com.torontowastewizard.Adapter.FavWasteAdapter
import pulkit.com.torontowastewizard.Model.Waste
import pulkit.com.torontowastewizard.R

class FavActivity : AppCompatActivity() {

    lateinit var wasteRecyclerViewFav: RecyclerView

    internal var wasteArrayList = ArrayList<Waste>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val intent = intent
        wasteArrayList = intent.getSerializableExtra("wasteArrayList") as ArrayList<Waste>
        wasteRecyclerViewFav = findViewById(R.id.wasteRecyclerViewFav)

        val layoutManager = LinearLayoutManager(this)
        wasteRecyclerViewFav.layoutManager = layoutManager

        val itemDecor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecor.setDrawable(this.resources.getDrawable(R.drawable.line_divider))
        wasteRecyclerViewFav.addItemDecoration(itemDecor)

        val adapter = FavWasteAdapter(wasteArrayList)
        wasteRecyclerViewFav.adapter = adapter
    }
}
