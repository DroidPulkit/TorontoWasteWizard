package pulkit.com.torontowastewizard.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pulkit.com.torontowastewizard.Model.Waste;
import pulkit.com.torontowastewizard.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    AutoCompleteTextView wasteName;
    Button submit, scan;

    String strName = "";

    String name[] = Common.getName();

    ArrayList<Waste> favWasteArrayList = new ArrayList<>();

    boolean isLoopFinished = false;
    boolean isCallBackFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        wasteName = findViewById(R.id.autoCompleteWasteName);
        submit = findViewById(R.id.buttonSubmit);
        scan = findViewById(R.id.buttonScan);

        // Create the adapter and set it to the AutoCompleteTextView
        //I am just saying, this is going to give terible performance, but due
        //to time constraints for this assignment, I am skipping the custom adapter
        //So it's going to eat a lot of memory
        //Will improve in future. :)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, name);
        wasteName.setAdapter(adapter);

        submit.setOnClickListener(this);
        scan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSubmit:
                extractData();
                if (dataValid()) {
                    //do something
                    //parse DB

                    callServer();

                }
                break;
            case R.id.buttonScan:

                break;
            default:

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_fav:
                SharedPreferences sharedPreferences3 = this.getSharedPreferences("FavSharedPreferences", Context.MODE_PRIVATE);
                Set<String> stringSet = sharedPreferences3.getStringSet("fav", null);
                if (stringSet != null) {

                    if (stringSet.size() > 0) {
                        favWasteArrayList.clear();

                        ArrayList<String> arrayList = new ArrayList<String>();
                        for (String str : stringSet) {
                            arrayList.add(str);
                        }

                        int size = arrayList.size();

                        for (int i = 0; i < size; i++) {
                            String title = arrayList.get(i);
                            callServerForFav(title, i, size);
                        }

                        isLoopFinished = true;


                    } else {
                        Toast.makeText(this, "No waste added to Favourite", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "No waste added to Favourite", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.menu_logout:
                SharedPreferences sharedPreferences = getSharedPreferences("loginSharedPreference", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("username");
                editor.apply();

                Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }

    void extractData(){
        strName = wasteName.getText().toString();
    }

    boolean dataValid(){

        boolean isValid = true;

        if (strName.isEmpty()){
            isValid = false;
        }

        return isValid;
    }

    void callServer(){

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://pulkitkumar.me/api/search.php";

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")){
                        //Register Successfull
                        Toast.makeText(HomeActivity.this, "Keyword found", Toast.LENGTH_SHORT).show();
                        //Intent intent = new Intent(HomeActivity.this, );
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray records = data.getJSONArray("records");
                        ArrayList<Waste> wasteArrayList = new ArrayList<>();
                        for (int i = 0; i < records.length(); i++){
                            JSONObject singleRecord = records.getJSONObject(i);
                            String body = singleRecord.getString("body");
                            String category = singleRecord.getString("category");
                            String title = singleRecord.getString("title");
                            String keywords = singleRecord.getString("keywords");
                            Waste waste = new Waste(body, category, title, keywords);
                            wasteArrayList.add(waste);
                        }

                        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                        intent.putExtra("wasteArrayList", wasteArrayList);
                        startActivity(intent);
                    } else {
                        Toast.makeText(HomeActivity.this, "Error finding the keyword", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
            }
        };

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, listener, errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("keyword",strName);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(postRequest);


    }

    void callServerForFav(String keyword, int index, int totalSize){

        final String k = keyword;

        final int i = index;
        final int t = totalSize;

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://pulkitkumar.me/api/search.php";

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")){
                        //Register Successfull
                        Toast.makeText(HomeActivity.this, "Keyword found", Toast.LENGTH_SHORT).show();
                        //Intent intent = new Intent(HomeActivity.this, );
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray records = data.getJSONArray("records");
                        ArrayList<Waste> wasteArrayList = new ArrayList<>();
                        for (int i = 0; i < records.length(); i++){
                            JSONObject singleRecord = records.getJSONObject(i);
                            String body = singleRecord.getString("body");
                            String category = singleRecord.getString("category");
                            String title = singleRecord.getString("title");
                            String keywords = singleRecord.getString("keywords");
                            Waste waste = new Waste(body, category, title, keywords);
                            wasteArrayList.add(waste);
                        }
                        favWasteArrayList.add(wasteArrayList.get(0));
                        isCallBackFinished = true;

                        if (isCallBackFinished && isLoopFinished){
                            Intent intent = new Intent(HomeActivity.this, FavActivity.class);
                            intent.putExtra("wasteArrayList", favWasteArrayList);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(HomeActivity.this, "Error finding the keyword", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
            }
        };

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, listener, errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("keyword",k);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(postRequest);
    }
}
