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
    Boolean isOpen2 = false;
    FrameLayout sliding_menu;
    FrameLayout menu_btn;
    LinearLayout res_list;
    FrameLayout upward_btn;

    LinearLayout fake;
    LinearLayout res_list2;
    FrameLayout  upward_btn2;
    LinearLayout real;

    LinearLayout up;




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
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, CustomerActivity.this);
                setUpMapIfNeeded();
                setMyLocation();
            }

        }
        loc_btn_frame.bringToFront();
        mFrame.bringToFront();
        //AddMarker();

        //top menu sliding animation
        final Animation tran_upward             = AnimationUtils.loadAnimation(this,R.anim.tran_upward);
        final Animation tran_downward           = AnimationUtils.loadAnimation(this,R.anim.tran_downward);
        SlidingAnimationListener animListener   = new SlidingAnimationListener();
        tran_upward.setAnimationListener(animListener);
        tran_downward.setAnimationListener(animListener);
        menu_btn = (FrameLayout) findViewById(R.id.menu_btn);
        sliding_menu = (FrameLayout) findViewById(R.id.sliding_menu);
        sliding_menu.bringToFront();
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sliding_menu.bringToFront();
                if(isOpen){
                    sliding_menu.startAnimation(tran_upward);
                }else{
                    sliding_menu.startAnimation(tran_downward);
                }
            }
        });


        //bottom restaurant list animation
        upward_btn = (FrameLayout) findViewById(R.id.upward_btn);
        res_list   = (LinearLayout) findViewById(R.id.res_list);
        upward_btn2= (FrameLayout) findViewById(R.id.upward_btn2);
        res_list2 = (LinearLayout) findViewById(R.id.res_list2);
        fake = (LinearLayout) findViewById(R.id.fake);
        real = (LinearLayout) findViewById(R.id.real);
        final SlidingAnimationListener2 ani_listener = new SlidingAnimationListener2();
        upward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("f", "f");
                fake.bringToFront();
                final ExpandAnimation ex_Ani = new ExpandAnimation(res_list2,500);
                ex_Ani.setAnimationListener(ani_listener);
                res_list2.startAnimation(ex_Ani);
            }
        });
        upward_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fake.bringToFront();
                final ExpandAnimation ex_Ani = new ExpandAnimation(res_list2,500);
                ex_Ani.setAnimationListener(ani_listener);
                res_list2.startAnimation(ex_Ani);
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
            upward_btn.setClickable(false);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(isOpen){
                sliding_menu.setVisibility(View.GONE);
                isOpen = false;
            }
            else{
                isOpen = true;
            }
            menu_btn.setClickable(true);
            upward_btn.setClickable(true);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
    public class SlidingAnimationListener2 implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(isOpen2){
                real.bringToFront();
                isOpen2 = false;
            }else{
                isOpen2 = true;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


    public void AddMarker(){
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(35.57252, 129.19034)).title("UNIST").snippet("Ulsan national institute of science and technology"));
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
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1L, 2F, CustomerActivity.this);
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
       /* ImageButton loc_btn= (ImageButton)findViewById(R.id.loc);
        loc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log.e("ONCLICK NPC", "loc is " + loc);
                if(loc!=null)
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 15));
            }
        });*/

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
       // Criteria criteria = new Criteria();
      //  String provider = locationManager.getBestProvider(criteria, false);
        //Log.e("NPC","loc is " + loc);
        final Location loc = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 2000, 1, this);
        loc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loc!=null)
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 15));
            }
        });

    }

    boolean locationTag=true;

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) return;

        if(locationTag){
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