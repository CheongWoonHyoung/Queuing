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
public class MypageActivity extends Activity{

    private TextView sign_out;
    FrameLayout back_btn;
    RelativeLayout go_account_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        sign_out = (TextView) findViewById(R.id.signout_btn);
        sign_out.setOnClickListener(myOnClick);
        back_btn = (FrameLayout)findViewById(R.id.mypage_back);
        back_btn.setOnClickListener(back);
        go_account_info = (RelativeLayout)findViewById(R.id.go_account_info);
        go_account_info.setOnClickListener(go_account);
    }


    private View.OnClickListener myOnClick=new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            switch(view.getId()){
                case R.id.signout_btn: {
                    Intent intent = new Intent(MypageActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }
    };

    private  View.OnClickListener back = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            finish();
        }
    };

    private  View.OnClickListener go_account = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            Intent intent2 = new Intent(MypageActivity.this, AccountInfoActivity.class);
            startActivity(intent2);
        }
    };

}
