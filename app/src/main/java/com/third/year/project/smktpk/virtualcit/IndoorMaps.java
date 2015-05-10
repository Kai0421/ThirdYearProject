package com.third.year.project.smktpk.virtualcit;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class IndoorMaps extends Activity implements LocationListener{
    //Constant Value - CIT Location
    private final LatLng LOCATION_CIT = new LatLng(53.3059955,-6.2219932);

    //Private Component from Google Service Play API
    private GoogleMap map;
    private LatLng latlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_maps);

        //Get Map then set it to be Indoor enabled to allow Viewing indoor maps
        map  = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setIndoorEnabled(true);

        //Enable the Location Feature and set Google map type to normal
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Tap the map to create a Marker for destination
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions marker = new MarkerOptions();
                map.addMarker(marker.position(latLng).draggable(true));
            }
        });
    }

    //Interact with CIT button to locate the college
    public void onClick_CIT(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.addMarker(new MarkerOptions().position(LOCATION_CIT).title("CIT"));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_CIT, 14);
        map.animateCamera(update);
    }

    //New Location will be loaded using Location Listener- It moves the camera and zoom into the new current Location
    @Override
    public void onLocationChanged(Location location) {
        latlng = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        map.animateCamera(CameraUpdateFactory.zoomTo(17));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
