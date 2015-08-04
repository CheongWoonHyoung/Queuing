package com.example.owner.queuing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


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
    LinearLayout small_imglayout;
    ImageView small_img;
    AutoCompleteTextView search;
    private ArrayList<String> restaurants;
    ArrayAdapter<String> search_adapter;

    private Animation tran_upward = null;
    private Animation tran_downward = null;
    private HashMap<String, String> markers;
    private BackPressCloseHandler backPressCloseHandler;
    private LatLng myLocation;
    ArrayList markerlist;
    Marker marker;
    ArrayList<ResListItem> items;

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
        markerlist = new ArrayList();

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
        small_imglayout = (LinearLayout) findViewById(R.id.samll_imglayout);
        search = (AutoCompleteTextView) findViewById(R.id.search);
        small_img = (ImageView) findViewById(R.id.r_simage);
        restaurants = new ArrayList<>();
        upward_btn.bringToFront();

        menu_btn.setOnClickListener(myOnClick);
        submenu01.setOnClickListener(myOnClick);
        submenu02.setOnClickListener(myOnClick);
        submenu03.setOnClickListener(myOnClick);
        upward_btn.setOnClickListener(myOnClick);
        upward_btn2.setOnClickListener(myOnClick);


        //search.setOnClickListener(searchmap);


        //about Listview
        items = new ArrayList<ResListItem>();

        JSONArray jArray = null;
        String url = "http://52.69.163.43/get_info.php";
        String jsonString = MakeJson(url);
        Double x_cordinate;
        Double y_cordinate;
        try {
            JSONObject json_data = null;
            jArray = new JSONArray(jsonString);
            for(int i=0; i<jArray.length(); i++){
                json_data = jArray.getJSONObject(i);
                //Picasso.with(getApplicationContext()).load(json_data.getString("img_small")).resize(width_image, height_image).centerCrop().into(small_img);
                Log.d("LOCATION", "mylocatoin : " + mGoogleMap.getMyLocation());
                if(myLocation == null) {
                    items.add(new ResListItem(json_data.getString("img_small"), json_data.getString("name"), json_data.getString("cuisine"), "GPS DISABLED"));
                }
                else {
                    Log.d("LOCATION","x : " + myLocation.latitude + " y : " + myLocation.longitude);
                    //GET DISTANCE BETWEEN TWO LOCATION USING DISTNACETO OR DISTNACEBETWEEN OR DISTANCEFROM
                    x_cordinate = json_data.getDouble("x_cordinate");
                    y_cordinate = json_data.getDouble("y_cordinate");
                    float[] results = new float[1];
                    Location.distanceBetween(myLocation.latitude,myLocation.longitude,x_cordinate,y_cordinate,results);
                    items.add(new ResListItem(json_data.getString("img_small"), json_data.getString("name"), json_data.getString("cuisine"), Float.toString(results[0])));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ResListAdapter adapter = new ResListAdapter(this,R.layout.res_listview,items);
        ListView res_listview = (ListView) findViewById(R.id.res_listview);
        res_listview.setAdapter(adapter);


        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String jsonall = null;
                JSONArray jArray = null;
                String cuisine = null;
                int line_num = 0;
                try {
                    jsonall = new req_specific_info().execute(marker.getTitle()).get();
                } catch (Exception e){
                    Log.e("JSON", "Error in JSONPARSER : " + e.toString());
                }
                Log.d("JSON", "whole json result : " + jsonall);

                try {
                    jArray = new JSONArray(jsonall);
                    JSONObject json_data = null;

                    for (int i = 0; i < jArray.length(); i++) {
                        json_data = jArray.getJSONObject(i);
                        line_num = json_data.getInt("line_num");
                        cuisine = json_data.getString("cuisine");

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                BitmapDescriptor bitmapDescriptor;
                if(line_num >=0 && line_num <6){
                    bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                }else if(line_num >=6 && line_num <11){
                    bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                }else if(line_num >= 11 && line_num< 21){
                    bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                }else{
                    bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                }

                marker.setSnippet(cuisine + " / " + line_num + " waiting");
                marker.setIcon(bitmapDescriptor);
                marker.showInfoWindow();

                return false;
            }
        });
    }

   /* private View.OnClickListener searchmap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for(int i=0; i<restaurants.size(); i++){
                if(search.getText().toString().equals(restaurants.get(i))){
                    LatLng latlng_search = new LatLng();
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng_search,15));
                }
            }

        }
    } */

    private View.OnClickListener myOnClick=new View.OnClickListener(){
        @Override
        public void onClick(View view) {
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
                    break;
                }
                case R.id.submenu02: {
                    Intent intent2 = new Intent(CustomerActivity.this, ReservationInfo.class);
                    startActivity(intent2);
                    break;
                }
                case R.id.submenu03: {
                    Intent intent3 = new Intent(CustomerActivity.this, MypageActivity.class);
                    startActivity(intent3);
                    break;
                }
                case R.id.upward_btn: {
                    items.clear();
                    Location my_loc = mGoogleMap.getMyLocation();
                    Log.d("LOCATION", "mylocatoin : " + my_loc);
                    JSONArray jArray = null;
                    String url = "http://52.69.163.43/get_info.php";
                    String jsonString = MakeJson(url);
                    Double x_cordinate;
                    Double y_cordinate;
                    try {
                        JSONObject json_data = null;
                        jArray = new JSONArray(jsonString);
                        for(int i=0; i<jArray.length(); i++){
                            json_data = jArray.getJSONObject(i);
                            //Picasso.with(getApplicationContext()).load(json_data.getString("img_small")).resize(width_image, height_image).centerCrop().into(small_img);
                            if(my_loc == null) {
                                items.add(new ResListItem(json_data.getString("img_small"), json_data.getString("name"), json_data.getString("cuisine"), "GPS DISABLED"));
                            }
                            else {
                                Log.d("MY_LOCATION","x : " + my_loc.getLatitude() + " y : " + my_loc.getLongitude());
                                //GET DISTANCE BETWEEN TWO LOCATION USING DISTNACETO OR DISTNACEBETWEEN OR DISTANCEFROM
                                x_cordinate = json_data.getDouble("x_cordinate");
                                y_cordinate = json_data.getDouble("y_cordinate");
                                float[] results = new float[1];
                                Location.distanceBetween(my_loc.getLatitude(),my_loc.getLongitude(),x_cordinate,y_cordinate,results);
                                Log.d("DISTANCE", "DISTANCE : FROM ME TO " + json_data.getString("name") + " : " + results[0] + "meter");
                                items.add(new ResListItem(json_data.getString("img_small"), json_data.getString("name"), json_data.getString("cuisine"), String.format("%.2f mile",getMiles(results[0]))));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    fake.bringToFront();
                    upward_btn2.setVisibility(View.VISIBLE);
                    final SlidingAnimationListener2 ani_listener = new SlidingAnimationListener2();
                    final ExpandAnimation ex_Ani = new ExpandAnimation(res_list2,300);
                    ex_Ani.setAnimationListener(ani_listener);
                    res_list2.startAnimation(ex_Ani);
                    break;
                }
                case R.id.upward_btn2: {
                    Log.d("NPC","upward_btn2");
                    fake.bringToFront();
                    final SlidingAnimationListener2 ani_listener = new SlidingAnimationListener2();
                    final ExpandAnimation ex_Ani = new ExpandAnimation(res_list2,300);
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

    private void AddMarker(){
        String url = "http://52.69.163.43/get_info.php";
        String jsonString = MakeJson(url);
        JSONArray jArray = null;

        String res_name = null;
        String cuisine;
        double x, y;
        int remaining_num;


        try{
            jArray = new JSONArray(jsonString);
            JSONObject json_data = null;

            for(int i=0; i<jArray.length(); i++){
                json_data = jArray.getJSONObject(i);
                x = json_data.getDouble("x_cordinate");
                y = json_data.getDouble("y_cordinate");
                res_name = json_data.getString("name");
                cuisine = json_data.getString("cuisine");
                remaining_num = json_data.getInt("line_num");
                BitmapDescriptor bitmapDescriptor;
                restaurants.add(res_name);
                if(remaining_num >=0 && remaining_num <6){
                    bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                }else if(remaining_num >=6 && remaining_num <11){
                    bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                }else if(remaining_num >= 11 && remaining_num< 21){
                    bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                }else{
                    bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                }
                    marker = mGoogleMap.addMarker(new MarkerOptions()
                        .icon(bitmapDescriptor)
                            .position(new LatLng(x, y)).title(res_name).snippet(cuisine + " / " + remaining_num + " waiting"));
            }

            search_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,restaurants);
            search.setAdapter(search_adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String jsonall = null;
                JSONArray jArray = null;

                int line_num = 0;
                String location = null;
                String img_large = null;
                String phone_num = null;
                String timing = null;
                String kinds = null;
                String dummy_name = null;


                try {
                    jsonall = new req_specific_info().execute(marker.getTitle()).get();
                } catch (Exception e){
                    Log.e("JSON", "Error in JSONPARSER : " + e.toString());
                }
                Log.d("JSON", "whole json result : " + jsonall);

                try {
                    jArray = new JSONArray(jsonall);
                    JSONObject json_data = null;

                    for (int i = 0; i < jArray.length(); i++) {
                        json_data = jArray.getJSONObject(i);
                        line_num = json_data.getInt("line_num");
                        img_large = json_data.getString("img_large");
                        location = json_data.getString("location");
                        kinds = json_data.getString("cuisine");
                        phone_num = json_data.getString("phone_num");
                        timing = json_data.getString("timing");
                        dummy_name = json_data.getString("dummy_name");

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(CustomerActivity.this, RestaurantInfo.class);
                intent.putExtra("name",marker.getTitle());
                intent.putExtra("line_num",line_num);
                intent.putExtra("img_large",img_large);
                intent.putExtra("location",location);
                intent.putExtra("cuisine",kinds);
                intent.putExtra("phone_num",phone_num);
                intent.putExtra("timing",timing);
                intent.putExtra("dummy_name",dummy_name);


                startActivity(intent);
            }
        });

    }

    private String MakeJson(String url){
        String jsonall = null;

        try {
            jsonall =new JsonParser_toString().execute(url).get();
        } catch (Exception e){
            Log.e("JSON", "Error in JSONPARSER : " + e.toString());
        }
        Log.d("JSON", "whole json result : " + jsonall);
        return jsonall;
    }

    private class req_specific_info extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... info) {
            String sResult = null;

            try {
                Log.d("INFO","rest name is " + info[0]);
                URL url = new URL("http://52.69.163.43/one_restinfo.php");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                String post_value = "name=" + info[0];

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(post_value);
                osw.flush();

                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str;

                while ((str = reader.readLine()) != null) {
                    builder.append(str);
                }
                sResult = builder.toString();

            } catch (Exception e) {
                Log.e("HTTPPOST","Error in Http POST REQUEST : " + e.toString());
            }
            return sResult;
        }
    }

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
        Log.e("onResume", "welcome");

        AddMarker();

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
        mGoogleMap.clear();

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
        mGoogleMap.setInfoWindowAdapter(new InfoWindowAdapterMarker(this));
        ImageButton loc_btn= (ImageButton)findViewById(R.id.loc);
        loc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMyLocation();
            }
        });

    }

    boolean locationTag=true;

    private float getMiles(float meter){
        return (float) (meter*0.000621371192);
    }
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