package com.example.android.superweather;

import android.app.*;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hamweather.aeris.communication.AerisEngine;

public class MapActivity extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        AerisEngine.initWithKeys(this.getString(R.string.aeris_client_id), this.getString(R.string.aeris_client_secret), this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle extras = getIntent().getExtras();
        Double lat = extras.getDouble("latitude");
        Double lon = extras.getDouble("longitude");
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", lat);
        bundle.putDouble("longitude",lon);
        MapFragment map1 = new MapFragment();
        map1.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frame1, map1);
        ft.commit();

    }
}
