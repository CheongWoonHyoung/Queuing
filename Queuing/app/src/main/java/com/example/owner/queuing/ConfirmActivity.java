package com.example.owner.queuing;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
    private TextView confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        Intent intent_num = getIntent();
        final DBManager_login dbManagerLogin = new DBManager_login(getApplicationContext(), "test2.db", null, 1);
        final String u_name = dbManagerLogin.returnUser();
        final String number = String.valueOf(intent_num.getExtras().getInt("reserve_num"));
        final String dummy_name = intent_num.getExtras().getString("dummy_name");
        frame_back_btn_confirm = (FrameLayout)findViewById(R.id.confirm_back_btn);
        confirm = (TextView) findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("pass",dummy_name);
                new HttpPostRequest().execute("in", u_name,number,"using Queuing",dummy_name);
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
            Toast.makeText(getApplicationContext(),"Queuing complete!",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
