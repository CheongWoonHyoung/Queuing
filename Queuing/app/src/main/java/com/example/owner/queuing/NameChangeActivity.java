package com.example.owner.queuing;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

/**
 * Created by mark_mac on 2015. 7. 24..
 */
public class NameChangeActivity extends Activity{
    FrameLayout back_btn_name;
    LinearLayout clear_name;
    TextView save_name;
    EditText u_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        final DBManager_login dbManagerLogin = new DBManager_login(getApplicationContext(), "test2.db", null, 1);
        save_name = (TextView) findViewById(R.id.save_name);
        u_name = (EditText) findViewById(R.id.name_setting);
        back_btn_name = (FrameLayout)findViewById(R.id.name_back);
        clear_name = (LinearLayout) findViewById(R.id.clear_name);

        u_name.setText(dbManagerLogin.returnUser().toString());
        back_btn_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        clear_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u_name.setText(null);
            }
        });
        save_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!u_name.getText().toString().equals("")) {
                    new HttpPostRequest().execute(dbManagerLogin.returnEmail(), u_name.getText().toString());
                    dbManagerLogin.update("update IS_LOGIN set _user='" + u_name.getText().toString() + "' where _id =1");
                }
                else
                    Toast.makeText(getApplicationContext(), "User name Empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class HttpPostRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... info) {
            String sResult = "Error";

            try {
                URL url = new URL("http://52.69.163.43/reset_name.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                String body = "mail=" + info[0] + "&"
                        + "new_name=" + info[1];

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
                sResult = builder.toString();
                Log.e("pass", sResult);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Email Does not Exist / internal Error"))
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

}
