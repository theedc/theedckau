package com.kaugirls.dalal.theedc;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kaugirls.dalal.theedc.tasks.GetShortestDir;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsActivityCurrentPlace extends AppCompatActivity
        implements OnMapReadyCallback, LocationListener {
    //never name any of this class the same name ever, even if they are in different packages.
    private static final String TAG = MapsActivityCurrentPlace.class.getSimpleName();
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    // ARRAY SHELTER
    String[] shelter;
    String Points;
    //google map declere
    private GoogleMap mMap;
    //  class camera postion
    private CameraPosition mCameraPosition;
    // The entry points to the Places API.
    // GeoDataClient provides access to Google's database of local place.
    private GeoDataClient mGeoDataClient;
    //PlaceDetectionClient provides quick access to the device's current place,
    // and offers the opportunity to report the location of the device at a particular place.
    private PlaceDetectionClient mPlaceDetectionClient;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private Location myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        Points = getIntent().getStringExtra("Points");


        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initializeLocationManager();


    }


    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }


    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     * Implement the OnMapReadyCallback interface and override the onMapReady() method
     */
    @Override
    public void onMapReady(final GoogleMap map) {
        mMap = map;


        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {

                if (((InfoWindowData) marker.getTag()).shelterorAssemblty.equals("s")) { //Toast.makeText(MapsActivityCurrentPlace.this,"1"+((InfoWindowData)marker.getTag()).shelter_name,Toast.LENGTH_LONG).show();

                    final InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

                    final Dialog dialog = new Dialog(MapsActivityCurrentPlace.this);
                    dialog.setContentView(R.layout.marker_info_dialog);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);


                    // set the custom dialog components - text, image and button
                    TextView shelter_name = dialog.findViewById(R.id.shelter_name);
                    TextView shelter_type = dialog.findViewById(R.id.shelter_type);
                    TextView shelter_status = dialog.findViewById(R.id.shelter_status);
                    TextView shelter_capacity = dialog.findViewById(R.id.shelter_capacity);
                    TextView shelter_description = dialog.findViewById(R.id.shelter_description);
                    shelter_status.setVisibility(View.GONE);


                    shelter_name.setText(infoWindowData.shelter_name);
                    shelter_type.setText("Type  :" + infoWindowData.shelter_type);
                    // shelter_status.setText("Status  :"+infoWindowData.shelter_status);
                    shelter_capacity.setText("Capacity  :" + infoWindowData.shelter_capacity);
                    shelter_description.setText(infoWindowData.shelter_description);
                    Button Share = dialog.findViewById(R.id.Share);


                    Share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String[] gpsVal = infoWindowData.shelter_Location.split(",");
                            double lat = Double.parseDouble(gpsVal[0]);
                            double lon = Double.parseDouble(gpsVal[1]);

                            String uri = "http://maps.google.com/maps?saddr=" + lat + "," + lon;

                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            String ShareSub = infoWindowData.shelter_name;
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, ShareSub);
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri);
                            startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        }
                    });


                    dialog.show();
                    marker.hideInfoWindow();
                }

            }
        });


        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


        try {
            JSONObject reader = new JSONObject(Points);

            JSONArray ShelterJS = reader.getJSONArray("shelter");

            shelter = new String[ShelterJS.length()];

            for (int i = 0; i < ShelterJS.length(); i++) {

                Bitmap markerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.shelter, null);

                Bitmap smallMarker = Bitmap.createScaledBitmap(markerBitmap, 70, 70, false);
                MarkerOptions marker = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                JSONObject Data = (JSONObject) ShelterJS.get(i);


                InfoWindowData info = new InfoWindowData(Data.getString("shelter_name"), Data.getString("shelter_type"), Data.getString("shelter_status"), Data.getString("capacity"), Data.getString("description"), "s", Data.getString("shelter_location"));
                CustomShelterInfoWindow customInfoWindow = new CustomShelterInfoWindow(this);
                mMap.setInfoWindowAdapter(customInfoWindow);
                shelter[i] = Data.getString("shelter_location");
                String[] gpsVal = Data.getString("shelter_location").split(",");
                double lat = Double.parseDouble(gpsVal[0]);
                double lon = Double.parseDouble(gpsVal[1]);
                Marker mp = map.addMarker(marker.position(new LatLng(lat, lon))
                        .title(Data.getString("shelter_name")));
                mp.setTag(info);

            }
            JSONArray AssemblyJS = reader.getJSONArray("assembly");

            for (int i = 0; i < AssemblyJS.length(); i++) {

                Bitmap markerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.assmply, null);

                Bitmap smallMarker = Bitmap.createScaledBitmap(markerBitmap, 70, 70, false);
                MarkerOptions marker = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                JSONObject Data = (JSONObject) AssemblyJS.get(i);


                InfoWindowData info = new InfoWindowData(Data.getString("assembly_name"), "", "", "", "", "A", Data.getString("assembly_location"));
                CustomShelterInfoWindow customInfoWindow = new CustomShelterInfoWindow(this);
                mMap.setInfoWindowAdapter(customInfoWindow);

                String[] gpsVal = Data.getString("assembly_location").split(",");
                double lat = Double.parseDouble(gpsVal[0]);
                double lon = Double.parseDouble(gpsVal[1]);
                Marker mp = map.addMarker(marker.position(new LatLng(lat, lon))
                        .title(Data.getString("assembly_name")));
                mp.setTag(info);
            }


        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    // get shortest path for user to nearest shelter site.
    public void GetDirction(String CurrentLocation) {

        GetShortestDir SD = new GetShortestDir(MapsActivityCurrentPlace.this, CurrentLocation, mMap);

        SD.execute(shelter);


    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            myLocation = mLastKnownLocation;
                            GetDirction(myLocation.getLatitude() + "," + myLocation.getLongitude());


                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    //the if logic ===============
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */


    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void initializeLocationManager() {

        //get the location manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        //define the location manager criteria
        Criteria criteria = new Criteria();

        String locationProvider = locationManager.getBestProvider(criteria, false);
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling

                return;
            }
            Location location = locationManager.getLastKnownLocation(locationProvider);


            //initialize the location
            if (location != null) {

                onLocationChanged(location);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    //------------------------------------------
    //	Summary: Location Listener  methods
    //------------------------------------------
    @Override
    public void onLocationChanged(Location location) {

        Log.i("called", "onLocationChanged");
        GetDirction(location.getLatitude() + "," + location.getLongitude());
        //when the location changes, update the map by zooming to the location
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
        mMap.moveCamera(center);

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.animateCamera(zoom);


    }

    @Override
    public void onProviderDisabled(String arg0) {

        Log.i("called", "onProviderDisabled");
    }

    @Override
    public void onProviderEnabled(String arg0) {

        Log.i("called", "onProviderEnabled");
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

        Log.i("called", "onStatusChanged");
    }


}