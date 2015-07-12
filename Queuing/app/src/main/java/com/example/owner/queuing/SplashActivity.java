package com.example.owner.queuing;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SplashActivity extends ActionBarActivity {
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
                final DBManager dbManager   = new DBManager(getApplicationContext(), "test.db", null, 1);
                isLogin                     = dbManager.PrintData();
                if(isLogin == "first"){
                    dbManager.insert("insert into IS_LOGIN values (null, 'no')");
                    isLogin = dbManager.PrintData();
                }
                if(isLogin.length()==2){
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
