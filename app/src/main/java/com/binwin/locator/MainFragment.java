package com.binwin.locator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;

public class MainFragment extends Fragment {
    double mLat;
    double mLon;
    EditText mNameEdit;
    EditText mLatEdit;
    EditText mLonEdit;
    //Google API client
    //We need to declare a fused location client for the location service and an ID for the callback
    FusedLocationProviderClient mFusedLocationClient;
    private static final int REQUEST_LOCATION_PERMISSIONS = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Creatingthe Client we get a reference to the fused location service
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mNameEdit = (EditText) v.findViewById(R.id.name_edit);
        mLatEdit = (EditText) v.findViewById(R.id.lat_edit);
        mLonEdit = (EditText) v.findViewById(R.id.lon_edit);
    //requesting the location; get location button
        Button mLocation = (Button) v.findViewById(R.id.location_button);
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasLocationPermission()) {
                    requestLocation();
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSIONS);
                }
            }
        });

        //map button
        Button mMap = (Button) v.findViewById(R.id.map_button);
        mMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLat != 0 && mLon != 0 && !mNameEdit.getText().toString().isEmpty()) {
                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                    intent.putExtra(MapsActivity.EXTRA_NAME, mNameEdit.getText().toString());
                    intent.putExtra(MapsActivity.EXTRA_LAT, mLat);
                    intent.putExtra(MapsActivity.EXTRA_LON, mLon);
                    startActivity(intent);
                }
            }
        });

        return v;
    }
    //Fetch LocationCallback
    //When we request a location update,we need to catch the result in a callback where we process the location.
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                Location location = locationList.get(locationList.size() - 1);
                mLat = location.getLatitude();
                mLon = location.getLongitude();
                mLatEdit.setText("" + mLat);
                mLonEdit.setText("" + mLon);
            }
        }
    };

    //permission request for location
    private void requestLocation() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setNumUpdates(1);
        mLocationRequest.setInterval(0);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    //check if permission have been granted
    private boolean hasLocationPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    //request user for permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != REQUEST_LOCATION_PERMISSIONS)// some other permission
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (hasLocationPermission()) {
            requestLocation();
        }
    }

    //Stopping the location serviceWhen we leave,we should remove the periodic update service requests. As we are only asking for one each time in this scenario this is only a precaution.
    @Override
    public void onPause() {
        super.onPause();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }
}