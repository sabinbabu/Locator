package com.binwin.locator;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final String EXTRA_NAME = "Name";
    public static final String EXTRA_LAT = "Lat";
    public static final String EXTRA_LON = "Lon";
    String mName;
    double mLat;
    double mLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //passing in the locaton data ; getting the data from the extras
        mName = getIntent().getStringExtra(EXTRA_NAME);
        mLat = getIntent().getDoubleExtra(EXTRA_LAT, 0);
        mLon = getIntent().getDoubleExtra(EXTRA_LON, 0);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //set the marker location
        LatLng venueLatLon = new LatLng(mLat, mLon);
        //setting the marker onClick as name
        mMap.addMarker(new MarkerOptions().position(venueLatLon).title(mName));
        //move the camera to location and zoom to a factor of 14
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(venueLatLon, 14));
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}