package com.example.owner.queuing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class SplashActivity extends Activity {
    public String isLogin = null;
    public Context mycontext = this;
    public String auth = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {

            @Override
            public void run() {
                final DBManager_login dbManagerLogin = new DBManager_login(getApplicationContext(), "test2.db", null, 1);
                isLogin                     = dbManagerLogin.returnData();
                auth                        = dbManagerLogin.returnAuth();
                if(isLogin == "first"){
                    dbManagerLogin.insert("insert into IS_LOGIN values (null, 'no', null, null)");
                    isLogin = dbManagerLogin.returnData();
                }

                if(isLogin.length()==2){

                    startActivity(new Intent(mycontext, LoginActivity.class));
                }
                else{
                    if(auth.length() == 5){
                        startActivity(new Intent(mycontext, OwnerActivity.class));
                    }
                    else{
                        startActivity(new Intent(mycontext, CustomerActivity.class));
                    }
                }
                finish();
            }
        }, 1000);
    }


}
