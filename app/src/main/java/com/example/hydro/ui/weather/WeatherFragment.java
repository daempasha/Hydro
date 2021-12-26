package com.example.hydro.ui.weather;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hydro.DatabaseHandler;
import com.example.hydro.R;
import com.example.hydro.databinding.FragmentWeatherBinding;
import com.example.hydro.models.Coordinates;
import com.example.hydro.models.Weather;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding binding;

    private DatabaseHandler localDatabaseHandler;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;

    private TextView cityText;
    private TextView temperatureText;
    private TextView weatherDescriptionText;
    private ImageView weatherIconText;
    private TextView windSpeedText;
    private TextView humidityText;
    private TextView pressureText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localDatabaseHandler = new DatabaseHandler(getActivity(), "hydro", null, 1);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        updateGPS();

    }

    private void getWeatherForLocation(double latitude, double longitude) {
        // Instantiate the RequestQueue.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String temperature_preference = sharedPreferences.getString("temperature_preference", "metric");


        Log.e("TEST", temperature_preference);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringBuilder urlBuilder = new StringBuilder().append("https://api.openweathermap.org/data/2.5/weather?lat=").append(latitude).append("&lon=").append(longitude).append("&appid=").append(getResources().getString(R.string.openweathermap_api_key));
        if(temperature_preference != "kelvin"){
            urlBuilder.append("&units=").append(temperature_preference);
        }

        String url = urlBuilder.toString();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Weather weather = new Weather(response);
                        updateWeather(weather.city, weather.temperature, weather.description, weather.iconUrl, weather.windSpeed, weather.humidity, weather.pressure);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private void updateWeather(String cityValue, String temperatureValue, String descriptionValue, String iconUrl, String windSpeedValue, String humidityValue, String pressureValue){
        cityText.setText(cityValue);
        temperatureText.setText(temperatureValue);
        weatherDescriptionText.setText(descriptionValue);
        windSpeedText.setText(windSpeedValue);
        humidityText.setText(humidityValue);
        pressureText.setText(pressureValue);
        Picasso.get().load(iconUrl).into(weatherIconText);

    }


    private void handleFailedLocationRequest(){
        Coordinates coordinates = localDatabaseHandler.getCoordinates();
        double latitude = coordinates.getLatitude();
        double longitude = coordinates.getLongitude();

        if(latitude != 0.0f && longitude != 0.0f){
            getWeatherForLocation(latitude, longitude);
            Toast.makeText(getActivity(), "Could not get current location, currently using cached location.", Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(getActivity(), "Could not get current location or cached location.", Toast.LENGTH_LONG).show();

        }
    }

    private void updateGPS() {
        // Get permission from user
        //Get current location
        //Update

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Granted permission
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //Got permission
                    if(location != null){

                        double latitude, longitude;

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        getWeatherForLocation(latitude, longitude);
                        localDatabaseHandler.addCoordinates(latitude, longitude);


                    } else {
                        handleFailedLocationRequest();
                    }

                }
            }).addOnFailureListener(getActivity(), new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    handleFailedLocationRequest();
                }
            });
        } else {
            //No permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    updateGPS();
                } else {
                    Toast.makeText(getActivity(), "This application requires the location permission to be granted in order to function.", Toast.LENGTH_SHORT).show();
                }
            });

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Get views
        this.cityText = binding.city;
        this.temperatureText = binding.temperature;
        this.weatherDescriptionText = binding.description;
        this.weatherIconText = binding.weatherIcon;
        this.windSpeedText = binding.windSpeed;
        this.humidityText = binding.humidity;
        this.pressureText = binding.pressure;


/*        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}