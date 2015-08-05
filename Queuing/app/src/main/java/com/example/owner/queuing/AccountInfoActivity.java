package com.example.owner.queuing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.gcm.GcmReceiver;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.w3c.dom.Text;

/**
 * Created by mark_mac on 2015. 7. 24..
 */
public class AccountInfoActivity extends FontActivity{
    FrameLayout back_btn;
    RelativeLayout change_pw;
    TextView email_account;
    Switch switch_notification;
    public static SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        email_account = (TextView) findViewById(R.id.mail_accountsetting);
        final DBManager_login dbManagerLogin = new DBManager_login(getApplicationContext(), "test2.db", null, 1);


        pref = getApplicationContext().getSharedPreferences("Notification_ONOFF", MODE_PRIVATE);
        editor = pref.edit();
        pref.getBoolean("Notification", true);
        back_btn = (FrameLayout)findViewById(R.id.account_back);
        change_pw = (RelativeLayout)findViewById(R.id.change_pw);

        switch_notification = (Switch) findViewById(R.id.switch_notification);

        switch_notification.setChecked(pref.getBoolean("Notification",true));
        switch_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editor.putBoolean("Notification",true);
                    editor.commit();
                    Log.d("NOTIFY","TF : " + pref.getBoolean("Notification",true));
                }
                else {
                    editor.putBoolean("Notification", false);
                    editor.commit();
                    Log.d("NOTIFY", "TF : " + pref.getBoolean("Notification", true));

                }
            }
        });
        email_account.setText(dbManagerLogin.returnEmail());
        back_btn.setOnClickListener(back);
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

    public static Boolean getNotification_ONOFF(Context context){
        pref = context.getSharedPreferences("Notification_ONOFF", MODE_PRIVATE);
        return pref.getBoolean("Notification", true);
    }


}
