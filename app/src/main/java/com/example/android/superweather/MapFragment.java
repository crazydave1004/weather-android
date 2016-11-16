package com.example.android.superweather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.hamweather.aeris.communication.AerisCallback;
import com.hamweather.aeris.communication.EndpointType;
import com.hamweather.aeris.maps.AerisMapView;
import com.hamweather.aeris.maps.MapViewFragment;
import com.hamweather.aeris.model.AerisResponse;
import com.hamweather.aeris.tiles.AerisTile;

public class MapFragment extends MapViewFragment implements AerisCallback{
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Bundle bundle = this.getArguments();
        Double lat = bundle.getDouble("latitude");
        Double lon = bundle.getDouble("longitude");
        mapView = (AerisMapView)view.findViewById(R.id.aerisfragment_map);
        mapView.init(savedInstanceState, AerisMapView.AerisMapType.GOOGLE);
        mapView.moveToLocation(new LatLng(lat,lon),7);
        mapView.addLayer(AerisTile.RADSAT);
        return view;
    }

    public void onResult(EndpointType endpointType, AerisResponse aerisResponse) {

    }
}

