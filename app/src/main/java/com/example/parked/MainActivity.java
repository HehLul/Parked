package com.example.parked;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.security.KeyPairGenerator;
import java.security.Permission;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
//FIELDS----------------------------------------------------------------------------------------------
    private GoogleMap map;
    private Button btnSetParked;
//PLACES VARIABLES
    private Places Places;
    private PlacesClient placesClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationServices LocationServices;
    private ContextCompat ContextCompat;
    private PackageManager PackageManager;
    private ActivityCompat ActivityCompat;
    private boolean locationPermissionGranted;
    private int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
    private Location lastKnownLocation;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
//ONCREATE----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getLocationPermission();
        setParked();
        //updateMap();
    }
//INITIALIZATION METHOD----------------------------------------------------------------------------------------------
    public void init(){
        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);

        // Construct a PlacesClient

        Places.initialize(getApplicationContext(), getString(R.string.API_KEY_GOOGLE_MAPS));
        placesClient = Places.createClient(this);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btnSetParked = findViewById(R.id.btnSetParked);
    }
//BUTTON METHODS----------------------------------------------------------------------------------------------
    public void setParked(){
        btnSetParked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Hello");
            }
        });
    }
//PERMISSION METHODS----------------------------------------------------------------------------------------------
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

//MAP METHODS----------------------------------------------------------------------------------------------
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));

        // Turn on the My Location layer and the related control on the map.
//        updateLocationUI();

       // Get the current location of the device and set the position of the map.
//        getDeviceLocation();
    }

//UNUSED----------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v) {
    }
}