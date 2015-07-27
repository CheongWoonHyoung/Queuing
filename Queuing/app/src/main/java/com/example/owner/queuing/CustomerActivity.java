package com.example.owner.queuing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.util.ArrayList;


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
    LinearLayout submenu01;
    LinearLayout submenu02;
    LinearLayout submenu03;
    FrameLayout menu_btn;
    LinearLayout res_list;
    FrameLayout upward_btn;
    LinearLayout fake;
    LinearLayout res_list2;
    FrameLayout  upward_btn2;
    LinearLayout real;
    LinearLayout up;
    EditText search;
    private Animation tran_upward = null;
    private Animation tran_downward = null;

    private BackPressCloseHandler backPressCloseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer);

        backPressCloseHandler = new BackPressCloseHandler(this);
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
        tran_upward             = AnimationUtils.loadAnimation(this,R.anim.tran_upward);
        tran_downward           = AnimationUtils.loadAnimation(this,R.anim.tran_downward);
        SlidingAnimationListener animListener   = new SlidingAnimationListener();
        tran_upward.setAnimationListener(animListener);
        tran_downward.setAnimationListener(animListener);

        menu_btn = (FrameLayout) findViewById(R.id.menu_btn);
        sliding_menu = (FrameLayout) findViewById(R.id.sliding_menu);
        upward_btn = (FrameLayout) findViewById(R.id.upward_btn);
        res_list   = (LinearLayout) findViewById(R.id.res_list);
        upward_btn2= (FrameLayout) findViewById(R.id.upward_btn2);
        res_list2 = (LinearLayout) findViewById(R.id.res_list2);
        fake = (LinearLayout) findViewById(R.id.fake);
        real = (LinearLayout) findViewById(R.id.real);
        submenu01 = (LinearLayout) findViewById(R.id.submenu01);
        submenu02 = (LinearLayout) findViewById(R.id.submenu02);
        submenu03 = (LinearLayout) findViewById(R.id.submenu03);
        search = (EditText) findViewById(R.id.search);

        menu_btn.setOnClickListener(myOnClick);
        submenu01.setOnClickListener(myOnClick);
        submenu02.setOnClickListener(myOnClick);
        submenu03.setOnClickListener(myOnClick);
        upward_btn.setOnClickListener(myOnClick);
        upward_btn2.setOnClickListener(myOnClick);




        //about Listview
        ArrayList<ResListItem> items = new ArrayList<ResListItem>();
        for(int i=0;i<15;i++) {
            items.add(new ResListItem(null, "taylor's steak house", "1.5miles"));
        }
        ResListAdapter adapter = new ResListAdapter(this,R.layout.res_listview,items);
        ListView res_listview = (ListView) findViewById(R.id.res_listview);
        res_listview.setAdapter(adapter);

    }

    private View.OnClickListener myOnClick=new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Log.d("NPC","onClick");
            // TODO Auto-generated method stub
            switch(view.getId()){
                case R.id.menu_btn: {
                    sliding_menu.bringToFront();
                    if (isOpen) {
                        sliding_menu.startAnimation(tran_upward);
                    } else {
                        sliding_menu.startAnimation(tran_downward);
                    }
                    break;
                }
                case R.id.submenu01: {
                    Log.d("NPC","SUBMENU01_CLICKED");
                    break;
                }
                case R.id.submenu02: {
                    Log.d("NPC","SUBMENU02_CLICKED");
                    break;
                }
                case R.id.submenu03: {
                    Log.d("NPC","SUBMENU03_CLICKED");
                    Intent intent = new Intent(CustomerActivity.this, MypageActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.upward_btn: {
                    Log.d("NPC","upward_btn");
                    fake.bringToFront();
                    final SlidingAnimationListener2 ani_listener = new SlidingAnimationListener2();
                    final ExpandAnimation ex_Ani = new ExpandAnimation(res_list2,500);
                    ex_Ani.setAnimationListener(ani_listener);
                    res_list2.startAnimation(ex_Ani);
                    break;
                }
                case R.id.upward_btn2: {
                    Log.d("NPC","upward_btn2");
                    fake.bringToFront();
                    final SlidingAnimationListener2 ani_listener = new SlidingAnimationListener2();
                    final ExpandAnimation ex_Ani = new ExpandAnimation(res_list2,500);
                    ex_Ani.setAnimationListener(ani_listener);
                    res_list2.startAnimation(ex_Ani);
                }
            }
        }
    };

    public class SlidingAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
            if(!isOpen) {
                sliding_menu.setVisibility(View.VISIBLE);
                search.setEnabled(false);
            }
            menu_btn.setClickable(false);
            upward_btn.setClickable(false);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(isOpen){
                sliding_menu.setVisibility(View.GONE);
                isOpen = false;
                search.setEnabled(true);
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
        if(mGoogleMap.getMyLocation()==null) {
            mGoogleMap.setOnMyLocationChangeListener(myLocationChangeListener);
        }
        else {
            Location new_location = mGoogleMap.getMyLocation();
            LatLng loc = new LatLng(new_location.getLatitude(), new_location.getLongitude());
            Log.d("KTH", "location.getLatitude(), location.getLongitude() -> " + new_location.getLatitude() + "," + new_location.getLongitude());
            if (mGoogleMap != null) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15),400, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
            isLocationChangeTag = false;
        }
    }

    Marker mMarker;

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if(isLocationChangeTag) {
                Log.d("KTH", "location.getLatitude(), location.getLongitude() -> " + location.getLatitude() + "," + location.getLongitude());
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                if (mGoogleMap != null) {
                    //mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,15));
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
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 2F, CustomerActivity.this);
                    Log.d("KTH","117 locationManger done");
                    setUpMapIfNeeded();
                    setMyLocation();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
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