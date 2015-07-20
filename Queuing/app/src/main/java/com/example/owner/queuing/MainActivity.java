package com.example.owner.queuing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends FragmentActivity implements LocationListener{
    LinearLayout mmap;
    GoogleMap mGoogleMap;
    private LocationManager locationManager;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean isLocationChangeTag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mmap = (LinearLayout) findViewById(R.id.layout_map);
        View child = getLayoutInflater().inflate(R.layout.activity_maps, null);
        mmap.addView(child);

        int googlePlayServiceResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.this);
        if( googlePlayServiceResult !=   ConnectionResult.SUCCESS){
            //Google Play Service -- X
            GooglePlayServicesUtil.getErrorDialog(googlePlayServiceResult, this, 0, new DialogInterface.OnCancelListener()
            {
                @Override
                public void onCancel(DialogInterface dialog)
                {
                    finish();
                }
            }).show();
        }else {
            //Google Play Service -- O
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled) {
            //location service -- X
                setUpMapIfNeeded();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.loc_alert_title)
                        .setPositiveButton(R.string.loc_alert_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                            }
                        })
                        .setNegativeButton(R.string.loc_alert_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            } else {
            //location service -- O
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, MainActivity.this);
                setUpMapIfNeeded();
                setMyLocation();
            }

        }


        AddMarker();
    }


    public void AddMarker(){
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(35.57252, 129.19034)).title("UNIST").snippet("Ulsan national institute of science and technology"));
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MainActivity.this, RestaurantInfo.class);
                startActivity(intent);
            }
        });
    }

    private LatLng myLocation;
    double[] myGps;

    private void setMyLocation(){
        isLocationChangeTag = true;
        mGoogleMap.setOnMyLocationChangeListener(myLocationChangeListener);
    }

    Marker mMarker;

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if(isLocationChangeTag) {
                Log.d("KTH", "location.getLatitude(), location.getLongitude() -> " + location.getLatitude() + "," + location.getLongitude());
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                mMarker = mGoogleMap.addMarker(new MarkerOptions().position(loc));
                if (mGoogleMap != null) {
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14));
                }
            }
            isLocationChangeTag = false;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if(!isGPSEnabled){
                    finish();
                }else{
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1L, 2F, MainActivity.this);
                    Log.d("KTH","117 locationManger done");
                    setUpMapIfNeeded();
                    setMyLocation();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    private void setUpMapIfNeeded() {

        if (mGoogleMap == null) {
            mGoogleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mGoogleMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getMyLocation();

    }

    boolean locationTag=true;

    @Override
    public void onLocationChanged(Location location) {
        if(locationTag){
            Log.d("myLog"  , "onLocationChanged: !!"  + "onLocationChanged!!");
            locationTag=false;
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }




}

