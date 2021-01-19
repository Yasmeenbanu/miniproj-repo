package com.example.wasted;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private static FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    View mapView;
    int initValue;
    public static final int DEFAULT_ZOOM = 20;
    private long UPDATE_INTERVAL = 20 * 1000;
    private long FASTEST_INTERVAL = UPDATE_INTERVAL / 2;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 34;

    private LocationRequest mLocationRequest;
    FusedLocationProviderClient locationClient = null;
    private LocationCallback mLocationCallback = null;
    Double localLat, localLng;
    private Double globalMyLat = null;
    private Double globalMyLng = null;

    String name,phnum;

    private String dbRefPath = "/driverdata";
    private String trackingDataPath = "/driverdata";
    private String predefinedDataPath = "/locationcentres";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent=getIntent();
        name=   intent.getStringExtra("customernam");
        phnum= intent.getStringExtra("customernum");

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dbRef=database.getReference(dbRefPath).child(cleanEmail(currentUser.getEmail()));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#384950")));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (!checkPermissions()) {
            requestPermissions();
        }

        createLocationCallback();
        startMapService();
        startLocationUpdates();
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(HomeActivity.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
    }
    public void onLocationChanged(Location location) {
        double lat = (double) (location.getLatitude());
        double lng = (double) (location.getLongitude());
        globalMyLat = lat;
        globalMyLng = lng;
        if (initValue == 0) {
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), DEFAULT_ZOOM);
            mMap.animateCamera(yourLocation);
            initValue++;
        }
        writeDataToDB(lat,lng);
    }

    private void writeDataToDB(double lat, double lng) {
        DatabaseReference.CompletionListener completionListener =
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                                           DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Toast.makeText(HomeActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        //Log.d("Tag","PhoneId" +devId);
        dbRef = database.getReference(dbRefPath).child(cleanEmail(currentUser.getEmail()));
        dbRef.child("Latitude").setValue(lat, completionListener);
        dbRef.child("Longitude").setValue(lng, completionListener);
        dbRef.child("Status").setValue("Online");
        if(name!=null)
        {
            dbRef.child("name").setValue(name, completionListener);

        }
        if(phnum!=null)
        {
            dbRef.child("phnum").setValue(phnum, completionListener);

        }

        //  Toast.makeText(MapLoadActivity.this, "phone id:"+devId, Toast.LENGTH_SHORT).show();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderEnabled(String provider) {

    }

    public void onProviderDisabled(String provider) {
        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    HomeActivity.this.finish();
                }
                return;
            }
        }
    }

    private void startMapService() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        //    mCancel= findViewById(R.id.btnB);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        if (ContextCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            mMap.setMyLocationEnabled(true);
                        } else {
                            requestPermissions();
                        }
                        mMap.setMyLocationEnabled(true);
                        if (mapView != null &&
                                mapView.findViewById(Integer.parseInt("1")) != null) {
                            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                            rlp.setMargins(0, 250, 250, 0);
                        }
                        //  mapLoader.setVisibility(View.GONE);
                    }
                });
            }
        });
    }



    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationClient = getFusedLocationProviderClient(this);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        if (mLocationCallback != null) {
            locationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
       if(id==R.id.report) {
           Intent intent = new Intent(HomeActivity.this, UpdateActivity.class);
           startActivity(intent);
       }
if (id == R.id.points) {

            Intent intent=new Intent(HomeActivity.this,PointsActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.centres:
                    database = FirebaseDatabase.getInstance();
                    dbRef = database.getReference(predefinedDataPath);
                    dbRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Double localLat, localLng;
                            for (final DataSnapshot dsp : dataSnapshot.getChildren()) {
                                HomeActivity.tempLocation savedLocation = dsp.getValue(HomeActivity.tempLocation.class);
                                localLat = savedLocation.Latitude;
                                localLng = savedLocation.Longitude;

                                //Toast.makeText(Home.this, dsp.getKey(), Toast.LENGTH_SHORT).show();
                                addMarkerOnMap(localLat, localLng, dsp.getKey());
                                //stringArrayList.add(dsp.getKey());


                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(HomeActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
                        }
                    });

            }
            return false;
        }
    };


    private void addMarkerOnMap (double mapLat, double mapLng, String placeName) {
        if (mMap != null) {
            LatLng nearbyLocation = new LatLng(mapLat,mapLng);
            mMap.addMarker(new MarkerOptions().position(nearbyLocation).title(placeName));
        } else {
            Toast.makeText(HomeActivity.this, "Map failed to load",Toast.LENGTH_SHORT).show();
        }
    }
    private String cleanEmail(String originalEmail) {
        return originalEmail.replaceAll("\\.", ",");
    }
    public static class tempLocation {

        public Double Latitude;
        public Double Longitude;
        public String Name;

        public tempLocation() {

        }

        public tempLocation(Double Latitude, Double Longitude) {

        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        deleteUserLocationData();
    }

    private void deleteUserLocationData() {
        database=FirebaseDatabase.getInstance();
        dbRef = database.getReference(trackingDataPath).child(cleanEmail(currentUser.getEmail()));
        dbRef.child("Status").setValue("Offline");
    }
}
