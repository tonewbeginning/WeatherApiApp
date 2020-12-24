package com.example.weatherapiapp;

import android.content.Context;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {
    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_CITY_WEATHER_BY_ID ="https://www.metaweather.com/api/location/";
    String cityID;
    Context context;
    public WeatherDataService(Context context) {
        this.context = context;
    }
    public interface VolleyResponseListener{
        void onResponse(String cityID);

        void onError(String message);
    }
    public interface ForeCastByIdResponse{
        void onResponse(WeatherReportModel weatherReportModel);

        void onError(String message);
    }
    public void getCityID(String cityName, final VolleyResponseListener volleyResponseListener){
// Instantiate the RequestQueue.

        String url =QUERY_FOR_CITY_ID + cityName;

        JsonArrayRequest request =  new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject cityInfo = null;
                cityID = "";
                try {
                    cityInfo = response.getJSONObject(0);
                    cityID = cityInfo.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                volleyResponseListener.onResponse(cityID);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onError("Something wrong");
            }

        });
        MySingleton.getInstance(context).addToRequestQueue(request);

    }
    public void getCityForecastByID(String cityID, final ForeCastByIdResponse foreCastByIDResponse){
        List<WeatherReportModel> report = new ArrayList<>();
        String url = QUERY_FOR_CITY_WEATHER_BY_ID + cityID;
        // get the json object
        JsonObjectRequest request =  new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray consolidated_weather_list = response.getJSONArray("consolidated_weather");
                    // get the first item in the array
                    WeatherReportModel first_day = new WeatherReportModel();
                    JSONObject first_day_from_api = (JSONObject) consolidated_weather_list.get(0);
                    first_day.setId(first_day_from_api.getInt("id"));
                    first_day.setWeather_state_name(first_day_from_api.getString("weather_state_name"));
                    first_day.setWeather_state_abbr(first_day_from_api.getString("weather_state_abbr"));
                    first_day.setWind_direction_compass(first_day_from_api.getString("wind_direction_compass"));
                    first_day.setCreated(first_day_from_api.getString("created"));
                    first_day.setApplicable_date(first_day_from_api.getString("applicable_date"));
                    first_day.setMin_temp(first_day_from_api.getLong("min_temp"));
                    first_day.setMax_temp(first_day_from_api.getLong("max_temp"));
                    first_day.setWind_speed(first_day_from_api.getLong("wind_speed"));
                    first_day.setWind_direction(first_day_from_api.getLong("wind_direction"));
                    first_day.setAir_pressure(first_day_from_api.getInt("air_pressure"));
                    first_day.setHumidity(first_day_from_api.getLong("humidity"));
                    first_day.setVisibility(first_day_from_api.getLong("visibility"));
                    first_day.setPredictability(first_day_from_api.getInt("predictability"));
                    foreCastByIDResponse.onResponse(first_day);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
            // get the property called consolidated_weather
        // which is an array. Each item in the array can
        // give the properties of weatherReportModel
    });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

//    public List<WeatherReportModel> getCityForecastByName(String cityName){
//
//    }
}
