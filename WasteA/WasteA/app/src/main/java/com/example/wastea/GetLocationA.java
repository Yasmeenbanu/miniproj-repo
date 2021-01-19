package com.example.wastea;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GetLocationA extends AppCompatActivity {
    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    FusedLocationProviderClient locationClient = null;
    private LocationCallback mLocationCallback = null;
    public static final int DEFAULT_ZOOM = 20;
    private Double globalMyLat ;
    private Double globalMyLng ;
    View mapView;
    int initValue;
    public  static  final int def=15;
    Button mLocte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#384950")));

        mLocte=findViewById(R.id.lctA);
        Intent intent=getIntent();
        globalMyLat=   intent.getDoubleExtra("latk",0.0000);
        globalMyLng= intent.getDoubleExtra("lngk",0.0000);
        Toast.makeText(GetLocationA.this,"lat  :"+globalMyLat,Toast.LENGTH_SHORT).show();
        createLocationCallback();
        startMapService();
mLocte.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        updateMap(globalMyLat,globalMyLng);

    }
});
       // startLocationUpdates();
    }
    public void updateMap(double mapLat, double mapLng) {
        if (mMap != null) {
            // mapLoader.setVisibility(View.GONE);
            mMap.clear();
            LatLng trucklocation = new LatLng(mapLat, mapLng);
            //MarkerOptions markerOptions=new MarkerOptions();
            // Toast.makeText(MapLoadActivity.this,"latlng: "+ambulancelocation,Toast.LENGTH_LONG).show();
            mMap.addMarker(new MarkerOptions().position(trucklocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(trucklocation,def));


        }
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
//        globalMyLat = lat;
//        globalMyLng = lng;
        if (initValue == 0) {
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), DEFAULT_ZOOM);
            mMap.animateCamera(yourLocation);
            initValue++;

        }

    }

    private void startMapService() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapS);
        mapView = mapFragment.getView();
        //    mCancel= findViewById(R.id.btnB);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        if (ContextCompat.checkSelfPermission(GetLocationA.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            mMap.setMyLocationEnabled(true);
                        }
                           // requestPermissions();

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
}
