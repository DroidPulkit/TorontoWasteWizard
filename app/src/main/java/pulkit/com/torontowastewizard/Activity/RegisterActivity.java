package pulkit.com.torontowastewizard.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pulkit.com.torontowastewizard.R;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = RegisterActivity.class.getSimpleName();

    EditText username, email, password, zip;
    Button register;
    TextView goBack;
    String strEmail = "", strPassword = "", strUsername = "", strZip = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        username = findViewById(R.id.editTextUserName);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        zip = findViewById(R.id.editTextZipCode);

        register = findViewById(R.id.buttonRegister);
        goBack = findViewById(R.id.textViewLogin);

        register.setOnClickListener(this);
        goBack.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonRegister:
                extractData();
                if (dataValidOrNot()){
                    //Call server to register
                    callServer();
                } else {
                    Toast.makeText(this, "Data not valid", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.textViewLogin:
                finish();
                break;
            default:

                break;
        }
    }

    void extractData(){
        strEmail = email.getText().toString();
        strPassword = password.getText().toString();
        strUsername = username.getText().toString();
        strZip = zip.getText().toString();
    }

    boolean dataValidOrNot(){
        boolean validity = true;

        if (strUsername.isEmpty()){
            validity = false;
        } else if (strEmail.isEmpty()){
            validity = false;
        }  else if (strPassword.isEmpty()){
            validity = false;
        }  else if (strZip.isEmpty()){
            validity = false;
        } else if(android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches() == false) {
            validity = false;
        }

        return validity;
    }

    void callServer(){

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://pulkitkumar.me/api/register.php";

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")){
                        //Register Successfull
                        Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registeration failed, used same email address before", Toast.LENGTH_SHORT).show();
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
                params.put("name",strUsername);
                params.put("email",strEmail);
                params.put("password",strPassword);
                params.put("zip",strZip);

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
