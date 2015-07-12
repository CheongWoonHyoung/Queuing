package com.example.owner.queuing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends FragmentActivity {
    public Context mycontext    = this;
    LinearLayout map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //FrameLayout layout_map = (FrameLayout)findViewById(R.id.layout_map);
        //li.inflate(R.layout.activity_maps, layout_map, true);

        //startActivity(new Intent(this,SplashActivity.class));
        setContentView(R.layout.activity_main);

        map = (LinearLayout)findViewById(R.id.layout_map);
        View child = getLayoutInflater().inflate(R.layout.activity_maps, null);
        map.addView(child);




    }



}
