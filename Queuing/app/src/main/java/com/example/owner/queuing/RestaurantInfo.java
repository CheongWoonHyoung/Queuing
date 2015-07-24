package com.example.owner.queuing;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by owner on 2015-07-12.
 */
public class RestaurantInfo extends Activity{

    FrameLayout mFrame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_info);

        mFrame = (FrameLayout)findViewById(R.id.res_back_btn);
        mFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
