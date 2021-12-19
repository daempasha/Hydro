package com.example.hydro.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.Image;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hydro.R;
import com.example.hydro.Weather;
import com.example.hydro.databinding.FragmentHomeBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;


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

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        updateGPS();
    }

    private void getWeatherForLocation(double latitude, double longitude) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://api.openweathermap.org/data/2.5/weather?lat="+latitude + "&lon=" + longitude + "&appid=" + getResources().getString(R.string.openweathermap_api_key);

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
                    double latitude, longitude;

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    getWeatherForLocation(latitude, longitude);
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
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
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