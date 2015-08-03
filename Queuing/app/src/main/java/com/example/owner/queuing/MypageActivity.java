package com.example.owner.queuing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by mark_mac on 2015. 7. 24..
 */
public class MypageActivity extends Activity{

    private TextView sign_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        sign_out = (TextView) findViewById(R.id.signout_btn);
        sign_out.setOnClickListener(myOnClick);
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

}
