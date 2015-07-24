package com.example.owner.queuing;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * Created by owner on 2015-07-12.
 */
public class RestaurantInfo extends Activity{

    FrameLayout mFrame;
    RelativeLayout btn_queue;
    LinearLayout btn_confirm;
    LinearLayout btn_cancel;
    private PopupWindow pwindo;
    private int mWidthPixels, mHeightPixels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_info);


        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);

        mWidthPixels = metrics.widthPixels;
        mHeightPixels = metrics.heightPixels;
        mFrame = (FrameLayout)findViewById(R.id.res_back_btn);
        mFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_queue = (RelativeLayout)findViewById(R.id.btn_queue);
        btn_queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiatePopupWindow();
            }
        });






    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initiatePopupWindow(){
        try{
            LayoutInflater inflater = (LayoutInflater)RestaurantInfo.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_pop_up, (ViewGroup)findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, mWidthPixels-200, mHeightPixels-800, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            btn_cancel = (LinearLayout)layout.findViewById(R.id.btn_cancel);
            btn_cancel.setOnClickListener(cancel_button_click_listener);

        }catch (Exception e){

        }
    }

    private View.OnClickListener cancel_button_click_listener= new View.OnClickListener(){
        public void onClick(View v){
            pwindo.dismiss();
        }
    };
}
