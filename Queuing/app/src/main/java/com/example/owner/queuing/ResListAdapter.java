package com.example.owner.queuing;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mintaewon on 2015. 7. 24..
 */
class ResListAdapter extends ArrayAdapter<ResListItem> {
    private Context context;
    private ArrayList<ResListItem> items;
    public ResListAdapter(Context context, int textViewResourceId, ArrayList<ResListItem> items){
        super(context,textViewResourceId,items);
        this.context=context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if(v==null){
            LayoutInflater vi =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.res_listview, null);
        }

        ResListItem res_item = items.get(position);
        Log.e("index", ":" + position);
        //ImageView res_image = (ImageView) v.findViewById(R.id.res_image);
        //TextView  res_name  = (TextView) v.findViewById(R.id.res_name);
        //TextView  res_distance = (TextView) v.findViewById(R.id.res_distance);

        //res_image.setImageBitmap(res_item.res_img);
        //res_name.setText(res_item.res_name);
        //res_distance.setText(res_item.res_distance);

        return v;
    }

}
