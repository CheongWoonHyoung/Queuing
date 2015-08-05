package com.example.owner.queuing;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Type;

/**
 * Created by mintaewon on 2015. 7. 27..
 */
public class ReservDialog extends Dialog implements View.OnTouchListener {
    public EditText name,phone,number;
    public TextView Ok,Cancel;
    private Typeface mTypeface;
    public String _name,_phone,_number;
    public ReservDialog(Context context) {
        super(context);
    }

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reserve);


        mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Questrial_Regular.otf");
        name = (EditText) findViewById(R.id.input_name);
        phone = (EditText) findViewById(R.id.input_phoneno);
        number = (EditText) findViewById(R.id.input_number);

        Ok = (TextView) findViewById(R.id.Ok);
        Cancel = (TextView) findViewById(R.id.Cancel);

        Ok.setOnTouchListener(this);
        Cancel.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view==Ok){
            Log.e("ok", "z");
            _name=name.getText().toString();
            _phone=phone.getText().toString();
            _number=number.getText().toString();
            name.setText(null);
            phone.setText(null);
            number.setText(null);
            name.setHint("Input your name");
            phone.setHint("Input your Phone number");
            number.setHint("Input your Company number");
            name.setTypeface(mTypeface);
            number.setTypeface(mTypeface);
            phone.setTypeface(mTypeface);
            name.requestFocus();
            cancel();
        }
        if(view == Cancel){
            Log.e("cacel","z");
            dismiss();
        }
        return false;
    }
}
