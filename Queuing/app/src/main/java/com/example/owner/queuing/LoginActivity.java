package com.example.owner.queuing;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class LoginActivity extends ActionBarActivity {
    public Context mycontext    = this;
    public String isConnected   = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText u_name   = (EditText)findViewById(R.id.login_name);
        final EditText u_email  = (EditText)findViewById(R.id.login_mail);
        final EditText u_passwd = (EditText)findViewById(R.id.login_password);
        Button sign_in          = (Button)  findViewById(R.id.login_button);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpPostRequest().execute(u_name.getText().toString(), u_email.getText().toString(), u_passwd.getText().toString(), "in");
                //startActivity(new Intent(mycontext, MainActivity.class));
                //finish();
            }
        });
    }

    private class HttpPostRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... info) {
            String sResult = "Error";

            try {
                URL url = new URL("http://52.69.163.43/login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                String body = "name=" + info[0] +"&"
                        +"email=" + info[1] + "&"
                        +"passwd=" + info[2] + "&"
                        +"in_up=" + info[3];

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
                isConnected = sResult;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result){
            Log.e("result", result);
            if(result.length() == 2){
                Toast.makeText(mycontext,"Check the input",Toast.LENGTH_SHORT).show();
            }else{
                final DBManager dbManager   = new DBManager(getApplicationContext(), "test.db", null, 1);
                dbManager.update("update IS_LOGIN set is_login ='yes' where _id = 1");
                startActivity(new Intent(mycontext,MainActivity.class));
                finish();
            }
        }
    }


}
