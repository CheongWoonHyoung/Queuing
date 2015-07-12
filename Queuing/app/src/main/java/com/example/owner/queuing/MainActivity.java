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

        Log.i("NPC", "SUCCESS1");
        mmap = (LinearLayout) findViewById(R.id.layout_map);
        View child = getLayoutInflater().inflate(R.layout.activity_maps, null);
        mmap.addView(child);

        Log.i("NPC", "SUCCESS2");

        int googlePlayServiceResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.this);
        if( googlePlayServiceResult !=   ConnectionResult.SUCCESS){ //���� �÷��� ���񽺸� Ȱ������ ���Ұ�� <������ ������ �ȵǾ� �ִ� ���
            //����
            Log.i("NPC", "CHOICE_A");

            GooglePlayServicesUtil.getErrorDialog(googlePlayServiceResult, this, 0, new DialogInterface.OnCancelListener()
            {
                @Override
                public void onCancel(DialogInterface dialog)
                {
                    finish();
                }
            }).show();
        }else { //���� �÷��̰� Ȱ��ȭ �� ���

            Log.i("NPC", "CHOICE_B");

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled) {  //��ġ���� ������ �ȵǾ� ������ �����ϴ� ��Ƽ��Ƽ�� �̵��մϴ�

                Log.i("NPC", "FAIL_LOC");

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("��ġ���� ����")
                        .setNeutralButton("�̵�", new DialogInterface.OnClickListener() {
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
            } else {   //��ġ ���� ������ �Ǿ� ������ ������ġ�� �޾ƿɴϴ�

                Log.i("NPC", "SUCCESS_LOC");

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, MainActivity.this); //�⺻ ��ġ �� ����
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
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                }
            }
            isLocationChangeTag = false;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//��ġ���� ��Ƽ��Ƽ ���� ��
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if(!isGPSEnabled){//����ڰ� ��ġ�������� �������� ����
                    finish();
                }else{//����ڰ� ��ġ���� ���� ������
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
        if(locationTag){//�ѹ��� ��ġ�� �������� ���ؼ� tag�� �־����ϴ�
            Log.d("myLog"  , "onLocationChanged: !!"  + "onLocationChanged!!");
            double lat =  location.getLatitude();
            double lng = location.getLongitude();

            Toast.makeText(MainActivity.this, "����  : " + lat +  " �浵: "  + lng ,  Toast.LENGTH_SHORT).show();
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

