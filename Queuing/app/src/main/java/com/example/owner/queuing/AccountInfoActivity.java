package com.example.owner.queuing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by mark_mac on 2015. 7. 24..
 */
public class AccountInfoActivity extends Activity{
    FrameLayout back_btn;
    RelativeLayout change_pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        back_btn = (FrameLayout)findViewById(R.id.account_back);
        back_btn.setOnClickListener(back);
        change_pw = (RelativeLayout)findViewById(R.id.change_pw);
        change_pw.setOnClickListener(changePassword);

    }

    private  View.OnClickListener changePassword = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(AccountInfoActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        }
    };

    private  View.OnClickListener back = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            finish();
        }
    };


}
