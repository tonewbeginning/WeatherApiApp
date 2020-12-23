package com.example.weatherapiapp;
// Free code camp.org : https://www.youtube.com/watch?v=xPi-z3nOcn8
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Button btn_cityID, btn_getWeatherByID, btn_getWeatherByName;
    EditText et_dataInput;
    ListView lv_weatherReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_cityID = findViewById(R.id.btn_getCityID);
        btn_getWeatherByID = findViewById(R.id.btn_getWeatherByCityID);
        btn_getWeatherByName = findViewById(R.id.getWeatherByCityName);
        et_dataInput = findViewById(R.id.et_datainput);
        lv_weatherReport = findViewById(R.id.lv_weatherReports);
        // click listeners for each button.
        btn_cityID.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url ="https://www.metaweather.com/api/location/search/?query=" + et_dataInput.getText().toString();

                JsonArrayRequest request =  new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject cityInfo = null;
                        String cityID = "";
                        try {
                            cityInfo = response.getJSONObject(0);
                            cityID = cityInfo.getString("woeid");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MainActivity.this, "City id: " +cityID,Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText( MainActivity.this, "Something wront",Toast.LENGTH_SHORT).show();
                    }
                });
// Add the request to the RequestQueue.
                MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
                //Toast.makeText(MainActivity.this, "You clicked me 1.", Toast.LENGTH_SHORT).show();
            }
        });
        btn_getWeatherByID.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,
                        "You clicked me 2",
                        Toast.LENGTH_SHORT).show();
            }
        });
        btn_getWeatherByName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,
                        "You typed: " + et_dataInput.getText().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });



    }
}