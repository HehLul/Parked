package com.example.parked;

import static android.content.ContentValues.TAG;
import android.Manifest;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
//FIELDS----------------------------------------------------------------------------------------------
    private GoogleMap mMap;
    private Button btnSetParked;
    private Boolean parked;


//LOCATION VARS
    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean locationPermissionGranted;
    private int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
    private double[] cords = new double[2];
    private double[] parkedCords = new double[2];

//ONCREATE----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setParked();

    }
//INITIALIZATION METHOD----------------------------------------------------------------------------------------------
    public void init(){
        SupportMapFragment mMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMap.getMapAsync(this);
        parked = false;

        btnSetParked = findViewById(R.id.btnSetParked);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//init location
        locationListenerInit();
    }

    private  void locationListenerInit (){
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Update map with new location
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
            // Other LocationListener methods...
        };

        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 0, locationListener);
        } else {
            // Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

//BUTTON METHODS----------------------------------------------------------------------------------------------
    public void setParked(){
        btnSetParked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save current location
                //place a parked marker on saved location
                //change button state2

                parked = true;
                parkedCords = cords;
                LatLng parkedLocation = new LatLng(parkedCords[0], parkedCords[1]);
               // mMap.addMarker(new MarkerOptions().position(parkedLocation).title("Parked"));
                mMap.addCircle(new CircleOptions()
                        .center(parkedLocation)
                        .radius(100).fillColor(Color.BLUE)
                        .strokeColor(Color.DKGRAY));
                Log.d(TAG, ""+parkedCords[0]+", "+parkedCords[1]);
                btnSetParked.setText("FIND VEHICLE");
            }
        });
    }


    private void reroute(){
    }
//PERMISSION METHODS----------------------------------------------------------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Call super method, get rid of error
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, request location updates
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            0, 0, locationListener);
                }
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

//MAP METHODS----------------------------------------------------------------------------------------------
    @Override//MUST be last method
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        updateLocationUI();
        getDeviceLocation();
    }

    private void updateLocationUI() {
        UiSettings uiSettings = mMap.getUiSettings();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Assuming you have a GoogleMap instance called 'mMap'

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            // Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Get the best and most recent location of the device
            FusedLocationProviderClient fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(this);

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(
                    task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location location = task.getResult();
                            cords[0] = location.getLatitude();
                            cords[1] =  location.getLongitude();
                           // Log.d(TAG, ""+cords[0]+", "+cords[1]);

                            LatLng currentLatLng = new LatLng(cords[0], cords[1]);

                            // Move the camera to the user's current location
                            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 20));
                        }
                    });
        }

    }


//UNUSED----------------------------------------------------------------------------------------------

    @Override
    public void onClick(View v) {
    }
}