package com.example.owner.queuing;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark_mac on 2015. 8. 3..
 */
public class FavoriteList extends FontActivity{

    ListView fav_listview;
    FrameLayout back_btn;
    ArrayList<FavoriteListItem> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        final DBManager_favorites dbManagerFavorites = new DBManager_favorites(getApplicationContext(), "favorites.db", null, 1);
        back_btn = (FrameLayout) findViewById(R.id.back_favorites);
        items = new ArrayList<>();
        Log.d("ISEMPTY","ISEMPTY " + dbManagerFavorites.isEmpty());
        Log.d("HOWMANY", "HOWMANY " + dbManagerFavorites.getTableSize());
        if(!dbManagerFavorites.isEmpty()) {
            for (int i = 1; i <= dbManagerFavorites.getTableSize(); i++) { //except first null, start from i=1
                Log.d("LIST", dbManagerFavorites.returnName(i) + " " + dbManagerFavorites.returnImgurl(i) + " " + dbManagerFavorites.returnCusine(i));
                items.add(new FavoriteListItem(dbManagerFavorites.returnName(i), dbManagerFavorites.returnImgurl(i), dbManagerFavorites.returnCusine(i), null));
            }
        }
        FavoriteListAdapter adapter = new FavoriteListAdapter(this,R.layout.activity_favorites_item,items);
        fav_listview = (ListView) findViewById(R.id.list_favorites);
        fav_listview.setAdapter(adapter);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


}
