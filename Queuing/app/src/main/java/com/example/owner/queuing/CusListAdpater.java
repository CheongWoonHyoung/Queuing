package com.example.owner.queuing;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mintaewon on 2015. 7. 27..
 */
public class CusListAdpater extends ArrayAdapter<CusListItem> {
    private Context context;
    private Typeface mTypeface;
    private ArrayList<CusListItem> items;
    public CusListAdpater(Context context, int textViewResourceId, ArrayList<CusListItem> items){
        super(context,textViewResourceId,items);
        this.context=context;
        this.items = items;
        mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Questrial_Regular.otf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        CusListHolder holder = new CusListHolder();
        if(v==null){
            LayoutInflater vi =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.cus_listview, null);
            holder.cus_priority = (TextView) v.findViewById(R.id.cus_priority);
            holder.cus_name = (TextView) v.findViewById(R.id.cus_name);
            holder.cus_number = (TextView) v.findViewById(R.id.cus_number);
            holder.cus_method = (TextView) v.findViewById(R.id.cus_method);
            holder.isOpen = true;
            v.setTag(holder);
        }else{
            holder = (CusListHolder) v.getTag();
        }

        CusListItem cus_item = items.get(position);

        View list_layout = v.findViewById(R.id.cus_item);
        list_layout.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) list_layout.getLayoutParams();
        lp.bottomMargin = 0;
        v.requestLayout();


        if(cus_item != null){
            holder.cus_priority.setText(cus_item.cus_priority);
            holder.cus_priority.setTypeface(mTypeface);
            holder.cus_name.setText(cus_item.cus_name);
            holder.cus_name.setTypeface(mTypeface);
            holder.cus_number.setText(cus_item.cus_number);
            holder.cus_number.setTypeface(mTypeface);
            holder.cus_method.setText(cus_item.cus_method);
            holder.cus_method.setTypeface(mTypeface);
            holder.isOpen = cus_item.isOpen;
        }

        return v;
    }
    public class CusListHolder{
        public TextView cus_priority;
        public TextView cus_name;
        public TextView cus_number;
        public TextView cus_method;
        boolean isOpen;
    }
}
