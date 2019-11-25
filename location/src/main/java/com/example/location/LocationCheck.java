package com.example.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lecture.experiments.roomdatabase.repository.LocTrackingRepo;
import lecture.experiments.roomdatabase.repository.PlacesVisitedRepo;

public class LocationCheck extends FragmentActivity implements GoogleMap.OnMarkerDragListener,OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        LocationListener,LocationSource {
    private GoogleMap googleMap;
    private static final int My_Permission_code = 11;
    private static final int Play_Services_res_request = 10;
    private Location mLastLocation;
    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 50.0f; // in meters
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 111;
    private static GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationSource.OnLocationChangedListener mListener;
    private double currentLatitude;
    private double beforeLatitude;
    private double beforeLongitude;
    private LocationManager locationManager;
    private double currentLongitude;
    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
    private int distance = 0;
    private LatLng fast;
    Marker myMarker, marker;
    private GeofencingClient geofencingClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Marker geoFenceMarker;
    private SupportMapFragment mapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_check);
        this.mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        //googleMap.setOnMarkerDragListener(this);
        mapFragment.getMapAsync(this);
        //buildGoogleApiClient();
    }

    private void setupLocation() {
//ion code
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheck1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck != PackageManager
                .PERMISSION_GRANTED && permissionCheck1 != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        My_Permission_code);
            }
        } else {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    // The next two lines tell the new client that “this” current class will handle connection stuff
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    //fourth line adds the LocationServices API endpoint from GooglePlayServices
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void createGeofence(double latitude, double longitude, int radius,
                                String geofenceType, String title, GoogleMap googleMap) {

        Marker stopMarker = googleMap.addMarker(new MarkerOptions()
                .draggable(true)
                .position(new LatLng(latitude, longitude))
                .title(title)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.common_full_open_on_phone)));

        googleMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude)).radius(radius)
                .fillColor(Color.parseColor("#B2A9F6")));
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng dragPosition = marker.getPosition();
        double dragLat = dragPosition.latitude;
        double dragLong = dragPosition.longitude;
        googleMap.clear();
        createGeofence(dragLat, dragLong, distance, "CIRCLE", "GEOFENCE", googleMap);
        Toast.makeText(
                LocationCheck.this,
                "onMarkerDragEnd dragLat :" + dragLat + " dragLong :"
                        + dragLong, Toast.LENGTH_SHORT).show();
        Log.i("info", "on drag end :" + dragLat + " dragLong :" + dragLong);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //googleMap.setOnMapClickListener(this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);


        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(LocationCheck.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                //If everything went fine lets get latitude and longitude
                beforeLatitude = location.getLatitude();
                beforeLongitude = location.getLongitude();
                Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();

            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        /*

         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            /*
            }
        } else {
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
                Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (myMarker != null) {
            myMarker.remove();
        }

        Location currlocation = null;
        currlocation.setLatitude(beforeLatitude);
        currlocation.setLongitude(beforeLongitude);

        if (location.distanceTo(currlocation) > 150 && (location.distanceTo(mLastLocation) <= 500)) {
            Toast.makeText(this, "Location Changed..Data Entered in Database.", Toast.LENGTH_SHORT).show();

            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            LocTrackingRepo locTrackingRepo = new LocTrackingRepo(this);
            locTrackingRepo.insertTask(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()),currentDate,currentTime,0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");
        //Disconnect from API onPause()
//        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            onLocationChangedListener.onLocationChanged(mLastLocation);
        }
    }

    @Override
    public void deactivate() {

    }

    public void registerEvents(Context context) {

    }
}


