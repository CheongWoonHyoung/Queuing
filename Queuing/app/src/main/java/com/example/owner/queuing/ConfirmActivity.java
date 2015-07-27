package com.example.owner.queuing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by cheongwh on 2015. 7. 24..
 */
public class ConfirmActivity extends Activity{

    private FrameLayout frame_back_btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        Intent intent_num = getIntent();
        Log.d("GET NUM FROM RES.java","# of people = " + intent_num.getExtras().getInt("reserve_num"));
        frame_back_btn_confirm = (FrameLayout)findViewById(R.id.confirm_back_btn);
        frame_back_btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
