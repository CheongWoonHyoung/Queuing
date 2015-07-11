package com.example.owner.queuing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("NPC", "SUCCESS1");
        mmap = (LinearLayout) findViewById(R.id.layout_map);
        View child = getLayoutInflater().inflate(R.layout.activity_maps, null);
        mmap.addView(child);

        Log.i("NPC", "SUCCESS2");

        int googlePlayServiceResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.this);
        if( googlePlayServiceResult !=   ConnectionResult.SUCCESS){ //구글 플레이 서비스를 활용하지 못할경우 <계정이 연결이 안되어 있는 경우
            //실패
            Log.i("NPC", "CHOICE_A");

            GooglePlayServicesUtil.getErrorDialog(googlePlayServiceResult, this, 0, new DialogInterface.OnCancelListener()
            {
                @Override
                public void onCancel(DialogInterface dialog)
                {
                    finish();
                }
            }).show();
        }else { //구글 플레이가 활성화 된 경우

            Log.i("NPC", "CHOICE_B");

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled) {  //위치정보 설정이 안되어 있으면 설정하는 엑티비티로 이동합니다

                Log.i("NPC", "FAIL_LOC");

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("위치서비스 동의")
                        .setNeutralButton("이동", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                            }
                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                })
                        .show();
            } else {   //위치 정보 설정이 되어 있으면 현재위치를 받아옵니다

                Log.i("NPC", "SUCCESS_LOC");

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, MainActivity.this); //기본 위치 값 설정
                setUpMapIfNeeded(); //Map
                setMyLocation();
            }

        }


        //AnimateCamera();
        //AddMarker();
    }

    /* public void AnimateCamera(){
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng());
    }*/

    public void AddMarker(){
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(36.144425, 128.393269)).title("KUMOKONGDAE").snippet("Maptest"));
    }

    private LatLng myLocation;
    double[] myGps;

    private void setMyLocation(){
        mGoogleMap.setOnMyLocationChangeListener(myLocationChangeListener);

    }

    Marker mMarker;
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            Log.d("KTH","location.getLatitude(), location.getLongitude() -> " + location.getLatitude() +","+ location.getLongitude());
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            mMarker = mGoogleMap.addMarker(new MarkerOptions().position(loc));
            if(mGoogleMap != null){
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//위치설정 엑티비티 종료 후
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if(!isGPSEnabled){//사용자가 위치설정동의 안했을때 종료
                    finish();
                }else{//사용자가 위치설정 동의 했을때
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1L, 2F, MainActivity.this);
                    Log.d("KTH","117 locationMaanger done");
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
        //setUpMapIfNeeded();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //locationManager.removeUpdates(this);
    }

    private void setUpMapIfNeeded() {

        Log.i("NPC", "setUpMapIfNeeded");
        if (mGoogleMap == null) {

            Log.i("NPC", "mGoogleMap == null");
            mGoogleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mGoogleMap != null) {
                Log.i("NPC", "mGoogleMap != null");

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
        if(locationTag){//한번만 위치를 가져오기 위해서 tag를 주었습니다
            Log.d("myLog"  , "onLocationChanged: !!"  + "onLocationChanged!!");
            double lat =  location.getLatitude();
            double lng = location.getLongitude();

            Toast.makeText(MainActivity.this, "위도  : " + lat +  " 경도: "  + lng ,  Toast.LENGTH_SHORT).show();
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