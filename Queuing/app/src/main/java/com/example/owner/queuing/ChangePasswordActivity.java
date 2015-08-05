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
public class ChangePasswordActivity extends FontActivity{
    FrameLayout back_btn;
    EditText ex_passwd;
    EditText new_passwd;
    EditText new_passwd_confirm;
    TextView save_setting;
    private String u_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        final DBManager_login dbManagerLogin = new DBManager_login(getApplicationContext(), "test2.db", null, 1);
        u_email = dbManagerLogin.returnEmail();

        ex_passwd = (EditText) findViewById(R.id.existing_passwd);
        new_passwd = (EditText) findViewById(R.id.new_passwd);
        new_passwd_confirm = (EditText) findViewById(R.id.new_passwd_confirm);
        save_setting = (TextView) findViewById(R.id.save_passwd);
        back_btn = (FrameLayout)findViewById(R.id.pass_back);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save_setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!ex_passwd.getText().toString().equals("")){
                    if(!new_passwd.getText().toString().equals("")){
                        if(!new_passwd_confirm.getText().toString().equals("")){
                            if(new_passwd.getText().toString().equals(new_passwd_confirm.getText().toString())){
                                new HttpPostRequest().execute(u_email, ex_passwd.getText().toString(), new_passwd.getText().toString());
                            }
                            else
                                Toast.makeText(getApplicationContext(),"Two new passwords different",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Input new password again",Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Input new password",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"Input existing password",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private class HttpPostRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... info) {
            String sResult = "Error";

            try {
                URL url = new URL("http://52.69.163.43/reset_account.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                String body = "mail=" + info[0] + "&"
                        + "ex_passwd=" + info[1] + "&"
                        + "new_passwd=" + info[2];

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
            if(result.equals("Existing Password Wrong"))
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }
}
