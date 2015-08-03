package com.example.owner.queuing;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class OwnerActivity extends Activity {
    ReservDialog reservDialog;
    EditText name;
    EditText phone;
    EditText company;
    CusListAdpater adapter;
    ExpandAnimation ex_Ani;
    ArrayList<CusListItem> items;
    ListView cus_listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_owner);
        final DBManager_login manager = new DBManager_login(getApplicationContext(), "test2.db", null, 1);
        final String res_name = manager.returnUser();
        Log.d("MANAGER","Res name : " + res_name);
        items = new ArrayList<CusListItem>();
        adapter = new CusListAdpater(this,R.layout.cus_listview,items);

        String jsonall = null;
        String url = "http://52.69.163.43/line_parse.php?name="+"Taylors";
        try {
            jsonall =new JsonParser_toString().execute(url).get();
            String jsonString = jsonall;
            JSONArray jArray = null;
            jArray = new JSONArray(jsonString);
            JSONObject json_data = null;

            for(int i=0; i<jArray.length(); i++){
                json_data = jArray.getJSONObject(i);
                items.add(new CusListItem(String.valueOf(json_data.getInt("pid")),json_data.getString("name"),json_data.getString("method"),json_data.getString("number")));
            }
        } catch (Exception e){
            Log.e("JSON", "Error in JSONPARSER : " + e.toString());
        }
        Log.e("JSON", "whole json result : " + jsonall);





        cus_listview = (ListView) findViewById(R.id.cus_listview);
        cus_listview.setAdapter(adapter);
        cus_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                new HttpPostRequest().execute("out", items.get(i).cus_name, items.get(i).cus_number, "OFFLINE", "Taylors");
                LinearLayout cus_item = (LinearLayout) view.findViewById(R.id.cus_item);

                ex_Ani = new ExpandAnimation(cus_item, 500);
                ex_Ani.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        items.remove(i);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                cus_item.startAnimation(ex_Ani);
            }
        });


        reservDialog = new ReservDialog(this);
        reservDialog.setTitle("Your Information");
        reservDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });

        reservDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                adapter.add(new CusListItem("z", reservDialog._name, "OFFLINE", reservDialog._number));
                Log.d("NULL","null : " + name + " "  + phone + " " + company);


                new HttpPostRequest().execute("in", reservDialog._name, reservDialog._number, "OFFLINE", res_name);

            }
        });

        TextView AddCustomer = (TextView) findViewById(R.id.reservation);
        AddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reservDialog.show();


            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        getApplicationContext().registerReceiver(mReceiver,new IntentFilter("key"));

    }

    @Override
    public void onPause(){
        super.onPause();

        getApplicationContext().unregisterReceiver(mReceiver);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getStringExtra("name");
            String num = intent.getStringExtra("num");
            int find_index = 0;
            Log.d("MSG_RECEIVED", "message : " + name + " " + num);
            if(num.length()==4) {
                for (int i = 0; i < items.size(); i++) {
                    if (name.equals(items.get(i).getCus_name())) {
                        find_index = i;
                        break;
                    }
                }
                if (cus_listview.getChildAt(find_index) == null) {
                    items.remove(find_index);
                    adapter.notifyDataSetChanged();
                } else {
                    LinearLayout cus_item = (LinearLayout) cus_listview.getChildAt(find_index).findViewById(R.id.cus_item);
                    ex_Ani = new ExpandAnimation(cus_item, 500);
                    final int i = find_index;
                    ex_Ani.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            items.remove(i);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    cus_item.startAnimation(ex_Ani);
                }
            }
            else{
                Log.d("MSG",num);
                adapter.add(new CusListItem("z", name,"ADDING BY APP", num));
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_owner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                Log.e("NPC:",sResult);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result){

            Log.d("RESULT_ECHOS", result);
        }
    }

}
