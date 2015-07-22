package com.example.owner.queuing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


public class SplashActivity extends Activity {
    public String isLogin = null;
    public Context mycontext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {

            @Override
            public void run() {
                final DBManager_login dbManagerLogin = new DBManager_login(getApplicationContext(), "test.db", null, 1);
                isLogin                     = dbManagerLogin.PrintData();
                if(isLogin == "first"){
                    dbManagerLogin.insert("insert into IS_LOGIN values (null, 'no')");
                    isLogin = dbManagerLogin.PrintData();
                }
                if(true /*isLogin.length()==2*/){
                    startActivity(new Intent(mycontext, LoginActivity.class));
                }
                else{
                    startActivity(new Intent(mycontext, MainActivity.class));
                }
                finish();
            }
        }, 1000);
    }


}
