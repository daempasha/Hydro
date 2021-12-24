package com.example.hydro.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather {
    public String city;
    public String temperature;
    public String pressure;
    public String temperatureMin;
    public String temperatureMax;
    public String humidity;
    public String windSpeed;
    public String description;
    public String iconUrl;

    public Weather(String weatherResponse){
        String celsiusSymbol = "°";

        try {
            JSONObject responseObject = new JSONObject(weatherResponse);
            JSONObject mainObject = responseObject.getJSONObject("main");
            JSONObject windObject = responseObject.getJSONObject("wind");
            JSONArray weatherObject = responseObject.getJSONArray("weather");

            this.city = responseObject.getString("name");
            this.windSpeed = windObject.getString("speed") + " m/s";
            this.temperature = mainObject.getString("temp") + celsiusSymbol;
            this.temperatureMin = mainObject.getString("temp_min") + celsiusSymbol;
            this.temperatureMax = mainObject.getString("temp_max") + celsiusSymbol;
            this.humidity = mainObject.getString("humidity")+" %";
            this.pressure = mainObject.getString("pressure")+" hPa";

            JSONObject weatherData = weatherObject.getJSONObject(0);
            this.description = weatherData.getString("main");
            this.iconUrl = "https://openweathermap.org/img/wn/" + weatherData.getString("icon") + "@4x.png";

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
