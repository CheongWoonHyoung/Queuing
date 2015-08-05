package com.example.owner.queuing;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mark_mac on 2015. 7. 24..
 */
public class MypageActivity extends FontActivity{

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
                    final DBManager_favorites dbManagerFavorites = new DBManager_favorites(getApplicationContext(), "favorites.db", null, 1);
                    String str = "";
                    for(int i=1; i<=dbManagerFavorites.getTableSize(); i++){
                        str += dbManagerFavorites.returnName(i) + "/";
                    }
                    str = str.substring(0, str.length()-1);
                    Log.d("STRING", "String : " + str);
                    //dbManagerFavorites.deleteAll();
                    Log.d("DB_SITUATION", dbManagerFavorites.showdatas());
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

    @Override
    protected void onResume(){
        super.onResume();
        final DBManager_login dbManagerLogin = new DBManager_login(getApplicationContext(), "test2.db", null, 1);
        account_name.setText(dbManagerLogin.returnUser());
    }
    private class HttpPostRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... info) {
            String sResult = "Error";

            try {
                URL url = new URL("http://52.69.163.43/line_test.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                String body = "in_out=" + info[0] +"&"
                        +"name=" + info[1] + "&"
                        +"number=" + info[2] + "&"
                        +"method=" + info[3] + "&"
                        +"resname=" + info[4];

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(body);
                osw.flush();

                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str;

                while ((str = reader.readLine()) != null) {
                    builder.append(str);
                }
                sResult     = builder.toString();
                Log.e("pass",sResult);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sResult;
        }


    }
}
