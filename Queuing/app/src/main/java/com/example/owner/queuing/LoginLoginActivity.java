package com.example.owner.queuing;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by cheongwh on 2015. 7. 29..
 */
public class LoginLoginActivity extends Activity{

    LinearLayout back_btn;
    LinearLayout realback_signin;
    EditText email;
    EditText passwd;
    String id;
    String auth;
    TextView sign_in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        sign_in = (TextView) findViewById(R.id.login_button);
        back_btn = (LinearLayout)findViewById(R.id.back_btn_login);
        realback_signin = (LinearLayout)findViewById(R.id.realback_signin);
        email = (EditText) findViewById(R.id.id);
        passwd = (EditText) findViewById(R.id.password);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        back_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    realback_signin.setBackgroundResource(R.drawable.ic_navigate_before_black_24dp);
                }
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    realback_signin.setBackgroundResource(R.drawable.ic_navigate_before_white_24dp);

                }
                return false;
            }
        });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().length()==0 || passwd.getText().toString().length()==0) Toast.makeText(getApplicationContext(),"Input All information",Toast.LENGTH_SHORT).show();
                else{
                    Log.e("WHOLE",email.getText().toString()+" "+passwd.getText().toString());
                    new HttpPostRequest().execute("", email.getText().toString(), passwd.getText().toString(), "in","");
                }
            }
        });
    }

    private class HttpPostRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... info) {
            String sResult = "Error";

            try {
                Log.e("LOGINACTIVITY", "httppostrequest");
                URL url = new URL("http://52.69.163.43/test.php/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                String body = "name=" + info[0] +"&"
                        +"email=" + info[1] + "&"
                        +"passwd=" + info[2] + "&"
                        +"in_up=" + info[3] + "&"
                        +"reg_id=" + info[4];

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
                Log.e("WHOLE",sResult);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result){
            JSONArray jarray = null;
            JSONObject json_data = null;
            if(!result.equals("Wrong Email or Password") && !result.equals("SignIn Error")) {
                try {
                    jarray = new JSONArray(result);
                    for (int i = 0; i < jarray.length(); i++) {
                        json_data = jarray.getJSONObject(i);
                        id = json_data.getString("name");
                        auth = json_data.getString("auth");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (auth.length() == 8) {
                    final DBManager_login dbManagerLogin = new DBManager_login(getApplicationContext(), "test2.db", null, 1);
                    dbManagerLogin.update("update IS_LOGIN set is_login ='yes' where _id = 1");
                    dbManagerLogin.update("update IS_LOGIN set _auth='customer' where _id = 1");
                    dbManagerLogin.update("update IS_LOGIN set _user='" + id + "' where _id =1");
                    startActivity(new Intent(getApplicationContext(), CustomerActivity.class));
                    finish();
                } else if (auth.length() == 5) {
                    final DBManager_login dbManagerLogin = new DBManager_login(getApplicationContext(), "test2.db", null, 1);
                    dbManagerLogin.update("update IS_LOGIN set is_login ='yes' where _id = 1");
                    dbManagerLogin.update("update IS_LOGIN set _auth='owner' where _id = 1");
                    dbManagerLogin.update("update IS_LOGIN set _user='" + id + "' where _id =1");
                    startActivity(new Intent(getApplicationContext(), OwnerActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                }
            }else Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
        }
    }
}
