package com.example.hydro.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private double[] mCoordinates;
    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

        mCoordinates = new double[]{0.0f, 0.0f};
    }


    public void setCoordinates(double latitude, double longitude){
        mCoordinates = new double[]{latitude, longitude};
    }


    public LiveData<String> getText() {
        return mText;
    }
}