package com.example.owner.queuing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
    RelativeLayout btn_queue;
    LinearLayout btn_confirm;
    LinearLayout btn_cancel;
    NumberPicker numberPicker;
    Dialog dialog;

    TextView name_v;
    TextView loc_v;
    TextView phone_v;
    TextView timing_v;
    TextView cuisine_v;
    TextView num_lefts_v;
    ImageView res_image_v;

    String rest_name = null;
    String location = null;
    String img_url = null;
    String phone_num = null;
    String timing = null;
    String kinds = null;
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
        num_remain = intent.getExtras().getInt("line_num");
        Log.d("RESINFO","number line is " + num_remain);
        name_v = (TextView) findViewById(R.id.rest_name);
        cuisine_v = (TextView) findViewById(R.id.cuisine);
        loc_v = (TextView) findViewById(R.id.loc);
        phone_v = (TextView) findViewById(R.id.phone);
        timing_v = (TextView) findViewById(R.id.timing);
        num_lefts_v = (TextView) findViewById(R.id.num_lefts);
        res_image_v = (ImageView) findViewById(R.id.res_image);

        name_v.setText(rest_name);
        loc_v.setText(location);
        cuisine_v.setText(kinds);
        phone_v.setText(phone_num);
        timing_v.setText(timing);
        num_lefts_v.setText(Integer.toString(num_remain));

        try {
            res_image_v.setImageBitmap(new loadBitmap().execute(img_url).get());
        } catch (Exception e) {

            Log.e("SETBITMAP", "ERROR in setting image bitmap : " + e.toString());
        }

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

    private class loadBitmap extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... url) {
            Bitmap bitmap = null;
            InputStream in = null;
            BufferedOutputStream out = null;

            final int IO_BUFFER_SIZE = 4*1024;
            try {
                Log.d("INFO","img url is " + url[0]);

                in = new BufferedInputStream(new URL(url[0]).openStream(), IO_BUFFER_SIZE);
                final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
                int bytesRead;
                byte[] buffer = new byte[IO_BUFFER_SIZE];
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();

                final byte[] data = dataStream.toByteArray();
                BitmapFactory.Options options = new BitmapFactory.Options();
                //options.inSampleSize = 1;

                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            } catch (Exception e) {
                Log.e("BITMAP","Error in loading Bitmap : " + e.toString());
            }
            return bitmap;
        }
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
