package com.example.owner.queuing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class LoginActivity extends FontActivity {
    public Context mycontext    = this;
    public String isConnected   = null;

    // SharedPreferences에 저장할 때 key 값으로 사용됨.
    public static final String PROPERTY_REG_ID = "registration_id";

    // SharedPreferences에 저장할 때 key 값으로 사용됨.
    private static final String PROPERTY_APP_VERSION = "1.0";
    private static final String TAG = "NPC";
    private BackPressCloseHandler backPressCloseHandler;
    private String SENDER_ID = "870647983286";
    private String User_ID = "null";
    GoogleCloudMessaging gcm;
    SharedPreferences prefs;
    Context context;
    String regid;
    TextView login;
    String isRight;
    String auth;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        backPressCloseHandler = new BackPressCloseHandler(this);
        final EditText u_name   = (EditText)findViewById(R.id.u_name);
        final EditText u_email  = (EditText)findViewById(R.id.u_email);
        final EditText u_passwd = (EditText)findViewById(R.id.u_passwd);
        login = (TextView)findViewById(R.id.login);
        TextView sign_up          = (TextView)  findViewById(R.id.login_button);

        context = getApplicationContext();
        gcm = GoogleCloudMessaging.getInstance(this);
        regid = getRegistrationId(context);

        if (regid.isEmpty()) {
            Log.i(TAG,"PASS");
            registerInBackground();
        }

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("length", ":" + u_name.getText().toString().length() + " " + u_email.toString().length() + " " + u_passwd.toString().length());
                if(u_name.getText().toString().length()==0 || u_email.getText().toString().length()==0 || u_passwd.getText().toString().length()==0) Toast.makeText(getApplicationContext(),"Input All information",Toast.LENGTH_SHORT).show();
                else if(u_passwd.getText().toString().length()<8) Toast.makeText(getApplicationContext(),"Input at least 8 digit password",Toast.LENGTH_SHORT).show();
                else{
                    User_ID = u_name.getText().toString();
                    new HttpPostRequest().execute(u_name.getText().toString(), u_email.getText().toString(), u_passwd.getText().toString(), "up",regid);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    private class HttpPostRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... info) {
            String sResult = "Error";

            try {
                Log.e("LOGINACTIVITY","httppostrequest");
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
                isRight     = sResult;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result){
            Log.d("SINGUP_RESULT", result);
            JSONArray jarray = null;
            JSONObject json_data = null;

            if(!result.equals("SignUp Error") && !result.equals("Already Exist")){
                try {
                    jarray = new JSONArray(result);
                    for(int i = 0; i < jarray.length(); i++) {
                        json_data = jarray.getJSONObject(i);
                        User_ID = json_data.getString("name");
                        auth = json_data.getString("auth");
                        email = json_data.getString("email");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(auth.length() == 8 ) {//FOR CUSTOMER
                    final DBManager_login dbManagerLogin = new DBManager_login(getApplicationContext(), "test2.db", null, 1);
                    dbManagerLogin.update("update IS_LOGIN set is_login ='yes' where _id = 1");
                    dbManagerLogin.update("update IS_LOGIN set _auth='customer' where _id = 1");
                    dbManagerLogin.update("update IS_LOGIN set _user='" + User_ID + "' where _id =1");
                    dbManagerLogin.update("update IS_LOGIN set _email='" + email + "' where _id =1");
                    startActivity(new Intent(mycontext, CustomerActivity.class));
                    finish();
                }else if(auth.length() == 5 ){//FOR OWNER
                    final DBManager_login dbManagerLogin = new DBManager_login(getApplicationContext(), "test2.db", null, 1);
                    dbManagerLogin.update("update IS_LOGIN set is_login ='yes' where _id = 1");
                    dbManagerLogin.update("update IS_LOGIN set _auth='owner' where _id = 1");
                    dbManagerLogin.update("update IS_LOGIN set _user='" + User_ID + "' where _id =1");
                    dbManagerLogin.update("update IS_LOGIN set _email='" + email + "' where _id =1");
                    startActivity(new Intent(mycontext, OwnerActivity.class));
                    finish();
                }
            } else{
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            }
        }
    }

    // Methods for realizing GCM
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        // 앱이 업데이트 되었는지 확인하고, 업데이트 되었다면 기존 등록 아이디를 제거한다.
        // 새로운 버전에서도 기존 등록 아이디가 정상적으로 동작하는지를 보장할 수 없기 때문이다.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(LoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    Log.i(TAG,"sender_id =  " + SENDER_ID);
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // 서버에 발급받은 등록 아이디를 전송한다.
                    // 등록 아이디는 서버에서 앱에 푸쉬 메시지를 전송할 때 사용된다.
                    sendRegistrationIdToBackend();

                    // 등록 아이디를 저장해 등록 아이디를 매번 받지 않도록 한다.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            //@Override
            //protected void onPostExecute(String msg) {
            //    mDisplay.append(msg + "\n");
            //}

        }.execute(null, null, null);
    }

    private void storeRegistrationId(Context context, String regid) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regid);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private void sendRegistrationIdToBackend() {
        Log.e(TAG, "RegId = "+regid);
    }

}
