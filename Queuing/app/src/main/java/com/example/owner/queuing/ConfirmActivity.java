package com.example.owner.queuing;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by cheongwh on 2015. 7. 24..
 */
public class ConfirmActivity extends Activity{

    private FrameLayout frame_back_btn_confirm;
    private FrameLayout realback_confirm;
    private TextView confirm;
    private TextView confirm_name;
    private TextView confirm_party;
    private TextView confirm_waitingtime;
    private TextView confirm_arrivaltime;
    private TextView confirm_fee;
    String dummy_name;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        Intent intent_num = getIntent();
        final DBManager_login dbManagerLogin = new DBManager_login(getApplicationContext(), "test2.db", null, 1);
        final String u_name = dbManagerLogin.returnUser();
        final String number = String.valueOf(intent_num.getExtras().getInt("reserve_num"));
        dummy_name = intent_num.getExtras().getString("dummy_name");

        confirm_name = (TextView) findViewById(R.id.confirm_name);
        confirm_party = (TextView) findViewById(R.id.confirm_party);
        confirm_waitingtime = (TextView) findViewById(R.id.confirm_waitingtime);
        confirm_arrivaltime = (TextView) findViewById(R.id.confirm_arrivaltime);
        confirm_fee = (TextView) findViewById(R.id.confirm_fee);

        confirm_name.setText(u_name);
        confirm_party.setText(number);
        confirm_arrivaltime.setText("Unknown");
        confirm_waitingtime.setText("Unknown");
        confirm_fee.setText("Unknown");



        frame_back_btn_confirm = (FrameLayout)findViewById(R.id.confirm_back_btn);
        realback_confirm = (FrameLayout)findViewById(R.id.realback_confirm);
        confirm = (TextView) findViewById(R.id.confirm);
        frame_back_btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("pass",dummy_name);
                new HttpPostRequest().execute("in", u_name, number, "Queuing", dummy_name);
            }
        });

        frame_back_btn_confirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    realback_confirm.setBackgroundResource(R.drawable.ic_navigate_before_black_24dp);
                }
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    realback_confirm.setBackgroundResource(R.drawable.ic_navigate_before_white_24dp);
                }
                return false;
            }
        });
    }

    private class HttpPostRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... info) {
            String sResult = "Error";

            try {
                URL url = new URL("http://52.69.163.43/line_test.php");
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
            DBManager_reserv manager = new DBManager_reserv(getApplicationContext(), "list_test2.db", null, 1);
            manager.insert("insert into RESERV_LIST values (null,'"+dummy_name+"','"+confirm_party.getText().toString()+"')");
            Log.e("123","datavalue :"+dummy_name+"  "+manager.returnName());
            Toast.makeText(getApplicationContext(),"Queuing complete!",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
