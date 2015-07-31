package com.example.owner.queuing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by cheongwh on 2015. 7. 29..
 */
public class LoginLoginActivity extends Activity{

    LinearLayout back_btn;
    LinearLayout realback_signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        back_btn = (LinearLayout)findViewById(R.id.back_btn_login);
        realback_signin = (LinearLayout)findViewById(R.id.realback_signin);
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
    }
}
