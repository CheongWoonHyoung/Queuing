package com.example.owner.queuing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * Created by owner on 2015-07-12.
 */
public class ReservationInfo extends Activity{

    FrameLayout frame_back_btn_reservationinfo;
    FrameLayout frame_back_btn_reservationinfo_blank;
    int marking =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switch (marking){
            case 0:
                setContentView(R.layout.activity_reservation_blank);
                frame_back_btn_reservationinfo_blank = (FrameLayout)findViewById(R.id.reservationinfo_blank_back_btn);
                frame_back_btn_reservationinfo_blank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                break;
            case 1:
                setContentView(R.layout.activity_reservation_info);
                frame_back_btn_reservationinfo = (FrameLayout)findViewById(R.id.reservationinfo_back_btn);
                frame_back_btn_reservationinfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                break;
        }





    }

    @Override
    public void onBackPressed() {
        finish();
    }



}
