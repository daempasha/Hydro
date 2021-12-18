package com.example.hydro;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather {
    public String city;
    public String temperature;
    public String temperatureMin;
    public String temperatureMax;
    public String humidity;

    public String description;
    public String iconUrl;

    public Weather(String weatherResponse){
        String celsiusSymbol = "°";

        try {
            JSONObject responseObject = new JSONObject(weatherResponse);
            JSONObject mainObject = responseObject.getJSONObject("main");
            JSONArray weatherObject = responseObject.getJSONArray("weather");

            this.city = responseObject.getString("name");
            this.temperature = mainObject.getString("temp") + celsiusSymbol;
            this.temperatureMin = mainObject.getString("temp_min") + celsiusSymbol;
            this.temperatureMax = mainObject.getString("temp_max") + celsiusSymbol;
            this.humidity = mainObject.getString("humidity");

            JSONObject weatherData = weatherObject.getJSONObject(0);
            this.description = weatherData.getString("main");
            this.iconUrl = "https://openweathermap.org/img/wn/" + weatherData.getString("icon") + "@4x.png";

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
