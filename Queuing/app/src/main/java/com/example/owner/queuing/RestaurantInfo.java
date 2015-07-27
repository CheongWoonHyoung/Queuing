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
public class RestaurantInfo extends Activity implements NumberPicker.OnValueChangeListener{

    FrameLayout frame_back_btn_resinfo;
    RelativeLayout btn_queue;
    LinearLayout btn_confirm;
    LinearLayout btn_cancel;
    NumberPicker numberPicker;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_info);

        frame_back_btn_resinfo = (FrameLayout)findViewById(R.id.res_back_btn);
        frame_back_btn_resinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_queue = (RelativeLayout)findViewById(R.id.btn_queue);
        btn_queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_dialog();
            }
        });



    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void show_dialog(){
        try{

            dialog = new Dialog(RestaurantInfo.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            dialog.setContentView(R.layout.activity_pop_up);
            btn_cancel = (LinearLayout)dialog.findViewById(R.id.btn_cancel);
            btn_confirm = (LinearLayout)dialog.findViewById(R.id.btn_confirm);
            numberPicker =(NumberPicker) dialog.findViewById(R.id.picker_popup);

            btn_cancel.setOnClickListener(myOnClick);
            btn_confirm.setOnClickListener(myOnClick);

            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(10);
            numberPicker.setValue(2);
            numberPicker.setOnValueChangedListener(this);
            numberPicker.setWrapSelectorWheel(false);
            dialog.show();
        } catch (Exception e){
            Log.d("NPC","PASS_FAIL DUE TO " + e);


        }
    }


    private View.OnClickListener myOnClick=new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Log.d("NPC", "onClick");
            // TODO Auto-generated method stub
            switch(view.getId()){
                case R.id.btn_confirm: {
                    Intent intent = new Intent(RestaurantInfo.this, ConfirmActivity.class);
                    intent.putExtra("reserve_num",numberPicker.getValue());
                    startActivity(intent);
                    break;
                }
                case R.id.btn_cancel: {
                    dialog.dismiss();
                    break;
                }
            }
        }
    };

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.d("NUMBERPICKER","Number Changed from " + oldVal + " to " + newVal);
    }
}
