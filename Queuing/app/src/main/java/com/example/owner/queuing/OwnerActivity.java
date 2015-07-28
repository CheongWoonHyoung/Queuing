package com.example.owner.queuing;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class OwnerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_owner);


        final ArrayList<CusListItem> items = new ArrayList<CusListItem>();
        for(int i=0;i<15;i++) {
            items.add(new CusListItem(""+(i+1), "Mark Yoon", "using Queuing","3"));
        }
        for(int i=0;i<15;i++){
            items.add(new CusListItem(""+(i+16),"Mark Yoon","using Offline","2"));
        }
        final CusListAdpater adapter = new CusListAdpater(this,R.layout.cus_listview,items);
        final ListView cus_listview = (ListView) findViewById(R.id.cus_listview);
        cus_listview.setAdapter(adapter);


        final ReservDialog reservDialog = new ReservDialog(this);
        reservDialog.setTitle("Your Information");
        reservDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                adapter.add(new CusListItem("z", reservDialog._name, reservDialog._phone, reservDialog._number));
            }
        });
        reservDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
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
}
