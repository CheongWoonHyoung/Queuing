package com.example.owner.queuing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class CustomerActivity extends FragmentActivity implements LocationListener{
    RelativeLayout mmap;
    GoogleMap mGoogleMap;
    private LocationManager locationManager;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean isLocationChangeTag = true;
    FrameLayout mFrame;
    FrameLayout loc_btn_frame;
    Boolean isOpen = false;
    FrameLayout sliding_menu;
    FrameLayout menu_btn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mmap = (RelativeLayout) findViewById(R.id.layout_map);
        View child = getLayoutInflater().inflate(R.layout.activity_maps, null);
        mmap.addView(child);
        mFrame = (FrameLayout)findViewById(R.id.frame);
        loc_btn_frame = (FrameLayout)findViewById(R.id.loc_btn_frame);

        int googlePlayServiceResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable(CustomerActivity.this);
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
                new AlertDialog.Builder(CustomerActivity.this)
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
                Log.d("KTH","location service on");
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 2000, CustomerActivity.this);
                setUpMapIfNeeded();
                setMyLocation();
            }

        }
        loc_btn_frame.bringToFront();
        mFrame.bringToFront();
        AddMarker();

        //top menu sliding animation
        final Animation tran_upward             = AnimationUtils.loadAnimation(this,R.anim.tran_upward);
        final Animation tran_downward           = AnimationUtils.loadAnimation(this,R.anim.tran_downward);

        SlidingAnimationListener animListener   = new SlidingAnimationListener();
        tran_upward.setAnimationListener(animListener);
        tran_downward.setAnimationListener(animListener);
        menu_btn = (FrameLayout) findViewById(R.id.menu_btn);
        sliding_menu = (FrameLayout) findViewById(R.id.sliding_menu);

        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sliding_menu.bringToFront();
                if (isOpen) {
                    sliding_menu.startAnimation(tran_downward);
                } else {
                    sliding_menu.startAnimation(tran_upward);
                }
            }
        });
    }

    public class SlidingAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
            if(!isOpen){
                sliding_menu.setVisibility(View.VISIBLE);
            }
            menu_btn.setClickable(false);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(isOpen){
                sliding_menu.setVisibility(View.INVISIBLE);
                isOpen = false;
            }
            else{
                isOpen = true;
            }
            menu_btn.setClickable(true);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


    public void AddMarker(){
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(34.058052, -118.302212)).title("Taylor's Steak House").snippet("Taylor's Steak House"));
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(CustomerActivity.this, RestaurantInfo.class);
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
                if (mGoogleMap != null) {
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
                }
            }
            isLocationChangeTag = false;
        }
    };

    protected GoogleMap.OnCameraChangeListener myCameraChangeListener = new GoogleMap.OnCameraChangeListener() {
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
            Log.e("NPC","CAMERA CHANGED");
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
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 2F, CustomerActivity.this);
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
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
        ImageButton loc_btn= (ImageButton)findViewById(R.id.loc);
        loc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMyLocation();
            }
        });

    }

    boolean locationTag=true;

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) return;

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