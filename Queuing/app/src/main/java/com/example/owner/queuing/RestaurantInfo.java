package com.example.owner.queuing;

import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by owner on 2015-07-12.
 */
public class RestaurantInfo extends Activity implements NumberPicker.OnValueChangeListener{

    FrameLayout frame_back_btn_resinfo;
    FrameLayout real_back;
    LinearLayout btn_confirm;
    LinearLayout btn_cancel;
    NumberPicker numberPicker;
    Dialog dialog;
    int height_image;
    int width_image;
    TextView btn_queue;
    TextView name_v;
    TextView loc_v;
    TextView phone_v;
    TextView timing_v;
    TextView cuisine_v;
    TextView num_lefts_v;
    ImageView res_image_v;
    View test;
    RelativeLayout sample;
    String rest_name = null;
    String location = null;
    String img_url = null;
    String phone_num = null;
    String timing = null;
    String kinds = null;
    String dummy_name = null;
    int num_remain = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_info);

        Intent intent = getIntent();

        rest_name = intent.getExtras().getString("name");
        img_url = intent.getExtras().getString("img_large");
        location = intent.getExtras().getString("location");
        kinds = intent.getExtras().getString("cuisine");
        phone_num = intent.getExtras().getString("phone_num");
        timing = intent.getExtras().getString("timing");
        dummy_name = intent.getExtras().getString("dummy_name");
        num_remain = intent.getExtras().getInt("line_num");
        //Log.d("RESINFO","number line is " + num_remain);
        name_v = (TextView) findViewById(R.id.rest_name);
        cuisine_v = (TextView) findViewById(R.id.cuisine);
        loc_v = (TextView) findViewById(R.id.loc);
        phone_v = (TextView) findViewById(R.id.phone);
        timing_v = (TextView) findViewById(R.id.timing);
        num_lefts_v = (TextView) findViewById(R.id.num_lefts);
        sample = (RelativeLayout)findViewById(R.id.sample);
        res_image_v = (ImageView) findViewById(R.id.res_image);
        //Log.e("pass", dummy_name);
        name_v.setText(rest_name);
        loc_v.setText(location);
        cuisine_v.setText(kinds);
        phone_v.setText(phone_num);
        timing_v.setText(timing);
        num_lefts_v.setText(Integer.toString(num_remain));
        frame_back_btn_resinfo = (FrameLayout)findViewById(R.id.res_back_btn);
        real_back = (FrameLayout) findViewById(R.id.real_back);
        frame_back_btn_resinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        frame_back_btn_resinfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    real_back.setBackgroundResource(R.drawable.ic_navigate_before_black_24dp);
                }
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    real_back.setBackgroundResource(R.drawable.ic_navigate_before_white_24dp);
                }
                return false;
            }
        });


        btn_queue = (TextView)findViewById(R.id.btn_queue);
        btn_queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int marking;
                DBManager_reserv manager = new DBManager_reserv(getApplicationContext(), "list_test2.db", null, 1);
                Log.e("123", ":" + manager.returnName());
                if(manager.returnName().equals("nothing")){
                    marking = 0;
                }else{
                    marking = 1;
                }
                switch (marking){
                    case 0:
                        show_dialog();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(),"You already have queuing",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        height_image = sample.getHeight();
        width_image = sample.getWidth();
        Picasso.with(getApplicationContext()).load(img_url).resize(width_image, height_image).centerCrop().into(res_image_v);
    }


    @Override
    protected void onResume(){
        super.onResume();


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

            //btn_cancel.setOnClickListener(myOnClick);
            btn_confirm.setOnClickListener(myOnClick);
            btn_confirm.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        btn_confirm.setBackgroundColor(Color.parseColor("#00695C"));
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        btn_confirm.setBackgroundColor(Color.parseColor("#009688"));
                    }
                    return false;

                }
            });
            btn_cancel.setOnClickListener(myOnClick);
            btn_cancel.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        btn_cancel.setBackgroundColor(Color.parseColor("#26A69A"));
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        btn_cancel.setBackgroundColor(Color.parseColor("#B2DFDB"));
                    }
                    return false;

                }
            });
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
                    intent.putExtra("dummy_name",dummy_name);
                    intent.putExtra("reserve_num",numberPicker.getValue());
                    startActivity(intent);
                    dialog.dismiss();
                    finish();
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
