package pulkit.com.torontowastewizard.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.WindowManager
import java.util.ArrayList
import pulkit.com.torontowastewizard.Adapter.WasteAdapter
import pulkit.com.torontowastewizard.Model.Waste
import pulkit.com.torontowastewizard.R

class SearchActivity : AppCompatActivity() {

    lateinit var wasteRecyclerView: RecyclerView

    lateinit var wasteArrayList : ArrayList<Waste>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val intent = intent
        wasteArrayList = intent.getSerializableExtra("wasteArrayList") as ArrayList<Waste>

        wasteRecyclerView = findViewById(R.id.wasteRecyclerView)

        val layoutManager = LinearLayoutManager(this)
        wasteRecyclerView.layoutManager = layoutManager

        val itemDecor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecor.setDrawable(this.resources.getDrawable(R.drawable.line_divider))
        wasteRecyclerView.addItemDecoration(itemDecor)

        val adapter = WasteAdapter(wasteArrayList)
        wasteRecyclerView.adapter = adapter

        //        ParkingReportAdapter adapter = new ParkingReportAdapter(wasteArrayList);
        //        wasteRecyclerView.setAdapter(adapter);
    }
}
