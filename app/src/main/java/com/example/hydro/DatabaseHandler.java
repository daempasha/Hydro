package com.example.hydro;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.hydro.models.Coordinates;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "hydro";
    private static final String TABLE_WEATHER = "weather";

    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WEATHER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_WEATHER + "(id VARCHAR(3) PRIMARY KEY, latitude FLOAT, longitude FLOAT);";
        db.execSQL(CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);

        onCreate(db);
    }

    public Coordinates getCoordinates() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT latitude, longitude FROM " + TABLE_WEATHER, null);

        if(cursor.moveToFirst()){
            Log.e("TEST", "Move to first");
            double latitude = Double.parseDouble(cursor.getString(0));
            double longitude = Double.parseDouble(cursor.getString(1));

            Coordinates coordinates = new Coordinates(latitude, longitude);

            return coordinates;

        } else {
            Coordinates coordinates = new Coordinates();
            return coordinates;
        }
    }

    public void addCoordinates(double latitude, double longitude){
        SQLiteDatabase db = this.getWritableDatabase();


        try {
            db.execSQL("REPLACE INTO " + TABLE_WEATHER + " VALUES (" + "'CRD'" + ", " + latitude + ", " + longitude + ");");

        } catch (Exception e){
            Log.e("TEST", e.toString());
        }

    }
}
