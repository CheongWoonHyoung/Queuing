package com.example.owner.queuing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ReservationInfo extends Activity{

    FrameLayout frame_back_btn_reservationinfo;
    FrameLayout frame_back_btn_reservationinfo_blank;
    FrameLayout realback_res;
    FrameLayout realback_res_blank;
    int marking =0;

    private TextView reserv_name;
    private TextView reserv_party;
    private TextView reserv_waitingtime;
    private TextView reserv_fee;
    private TextView reserv_left;
    private TextView reserv_cancel;

    String res_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBManager_reserv manager = new DBManager_reserv(getApplicationContext(), "list_test.db", null, 1);
        Log.e("123",":"+manager.returnData());
        if(manager.returnData().length()==1){
            marking = 0;
        }else{
            marking = 1;
        }
        switch (marking){
            case 0:
                setContentView(R.layout.activity_reservation_blank);
                frame_back_btn_reservationinfo_blank = (FrameLayout)findViewById(R.id.reservationinfo_blank_back_btn);
                realback_res_blank = (FrameLayout)findViewById(R.id.realback_res_info_blank);
                frame_back_btn_reservationinfo_blank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

                frame_back_btn_reservationinfo_blank.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            realback_res_blank.setBackgroundResource(R.drawable.ic_navigate_before_black_24dp);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                            realback_res_blank.setBackgroundResource(R.drawable.ic_navigate_before_white_24dp);
                        }
                        return false;
                    }
                });
                break;
            case 1:
                setContentView(R.layout.activity_reservation_info);

                final DBManager_login dbManagerLogin = new DBManager_login(getApplicationContext(), "test2.db", null, 1);
                final String u_name = dbManagerLogin.returnUser();

                reserv_name = (TextView) findViewById(R.id.reserv_name);
                reserv_party = (TextView) findViewById(R.id.reserv_party);
                reserv_waitingtime = (TextView) findViewById(R.id.reserv_waitingtime);
                reserv_fee = (TextView) findViewById(R.id.reserv_fee);
                reserv_left = (TextView) findViewById(R.id.reserv_left);
                reserv_cancel = (TextView) findViewById(R.id.reserv_cancel);
                frame_back_btn_reservationinfo = (FrameLayout)findViewById(R.id.reservationinfo_back_btn);
                realback_res = (FrameLayout)findViewById(R.id.realback_res_info);
                final DBManager_reserv dbManagerReserv = new DBManager_reserv(getApplicationContext(), "list_test2.db", null, 1);
                reserv_name.setText(u_name);
                reserv_party.setText(dbManagerReserv.returnParty());
                reserv_waitingtime.setText("Unkown");
                reserv_fee.setText("Unknown");

                res_name = dbManagerReserv.returnName();


                frame_back_btn_reservationinfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                frame_back_btn_reservationinfo.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            realback_res.setBackgroundResource(R.drawable.ic_navigate_before_black_24dp);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                            realback_res.setBackgroundResource(R.drawable.ic_navigate_before_white_24dp);
                        }
                        return false;
                    }
                });
                reserv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new HttpPostRequest().execute("out",reserv_name.getText().toString(),reserv_party.getText().toString(),"using Queuing",res_name);
                    }
                });
                break;
        }
    }

    private class HttpPostRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... info) {
            String sResult = "Error";

            try {
                URL url = new URL("http://52.69.163.43/line_test.php/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                String body = "in_out=" + info[0] +"&"
                        +"name=" + info[1] + "&"
                        +"number=" + info[2] + "&"
                        +"method=" + info[3] + "&"
                        +"resname=" + info[4];

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(body);
                osw.flush();

                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str;

                while ((str = reader.readLine()) != null) {
                    builder.append(str);
                }
                sResult     = builder.toString();
                Log.e("pass",sResult);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result){
            final DBManager_reserv dbManagerReserv = new DBManager_reserv(getApplicationContext(), "list_test2.db", null, 1);
            dbManagerReserv.update("delete from RESERV_LIST where res_name='"+dbManagerReserv.returnName()+"'");
            Toast.makeText(getApplicationContext(),"Queuing Caceled!",Toast.LENGTH_SHORT).show();
            finish();

        }



    }

    @Override
    public void onBackPressed() {
        finish();
    }



}
