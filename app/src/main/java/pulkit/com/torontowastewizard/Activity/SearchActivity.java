package pulkit.com.torontowastewizard.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.WindowManager;

import java.util.ArrayList;

import pulkit.com.torontowastewizard.Adapter.WasteAdapter;
import pulkit.com.torontowastewizard.Model.Waste;
import pulkit.com.torontowastewizard.R;

public class SearchActivity extends AppCompatActivity {

    RecyclerView wasteRecyclerView;

    ArrayList<Waste> wasteArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        wasteArrayList = (ArrayList<Waste>) intent.getSerializableExtra("wasteArrayList");

        wasteRecyclerView = findViewById(R.id.wasteRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        wasteRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecor.setDrawable(this.getResources().getDrawable(R.drawable.line_divider));
        wasteRecyclerView.addItemDecoration(itemDecor);

        WasteAdapter adapter = new WasteAdapter(wasteArrayList);
        wasteRecyclerView.setAdapter(adapter);

//        ParkingReportAdapter adapter = new ParkingReportAdapter(wasteArrayList);
//        wasteRecyclerView.setAdapter(adapter);
    }
}
