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
    private TextView account_email;
    private TextView account_name;

    FrameLayout back_btn;
    RelativeLayout go_account_info;
    RelativeLayout change_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        final DBManager_login dbManagerLogin = new DBManager_login(getApplicationContext(), "test2.db", null, 1);

        change_name = (RelativeLayout)findViewById(R.id.change_name);
        change_name.setOnClickListener(changeName);
        sign_out = (TextView) findViewById(R.id.signout_btn);
        back_btn = (FrameLayout)findViewById(R.id.mypage_back);
        go_account_info = (RelativeLayout)findViewById(R.id.go_account_info);
        account_email = (TextView) findViewById(R.id.account_mail);
        account_name = (TextView) findViewById(R.id.account_name);

        account_email.setText(dbManagerLogin.returnEmail());
        account_name.setText(dbManagerLogin.returnUser());

        sign_out.setOnClickListener(myOnClick);
        back_btn.setOnClickListener(back);
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
            Intent intent = new Intent(MypageActivity.this, AccountInfoActivity.class);
            startActivity(intent);
        }
    };

    private  View.OnClickListener changeName = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            Intent intent3 = new Intent(MypageActivity.this, NameChangeActivity.class);
            startActivity(intent3);
        }
    };

}
