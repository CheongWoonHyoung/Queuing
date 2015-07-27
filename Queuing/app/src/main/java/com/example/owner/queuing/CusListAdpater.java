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
 * Created by mintaewon on 2015. 7. 27..
 */
public class CusListAdpater extends ArrayAdapter<CusListItem> {
    private Context context;
    private ArrayList<CusListItem> items;
    public CusListAdpater(Context context, int textViewResourceId, ArrayList<CusListItem> items){
        super(context,textViewResourceId,items);
        this.context=context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if(v==null){
            LayoutInflater vi =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.cus_listview, null);
        }

        CusListItem cus_item = items.get(position);

        TextView cus_priority = (TextView) v.findViewById(R.id.cus_priority);
        TextView cus_name = (TextView) v.findViewById(R.id.cus_name);
        TextView cus_number = (TextView) v.findViewById(R.id.cus_number);
        TextView cus_method = (TextView) v.findViewById(R.id.cus_method);

        cus_priority.setText(cus_item.cus_priority);
        cus_name.setText(cus_item.cus_name);
        cus_number.setText(cus_item.cus_number);
        cus_method.setText(cus_item.cus_method);

        return v;
    }
}
