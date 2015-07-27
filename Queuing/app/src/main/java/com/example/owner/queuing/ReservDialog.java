package com.example.owner.queuing;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by mintaewon on 2015. 7. 27..
 */
public class ReservDialog extends Dialog implements View.OnTouchListener {
    public EditText name,phone,number;
    public TextView Ok,Cancel;
    public String _name,_phone,_number;
    public ReservDialog(Context context) {
        super(context);
    }

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reserve);

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
            _name=name.getText().toString();
            _phone=phone.getText().toString();
            _number=number.getText().toString();
            dismiss();
        }else if(view == Cancel){
            cancel();
        }
        return false;
    }
}
