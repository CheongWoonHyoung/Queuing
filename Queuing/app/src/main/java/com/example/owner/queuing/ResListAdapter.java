package com.example.owner.queuing;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by mintaewon on 2015. 7. 24..
 */
public class ResListAdapter extends ArrayAdapter<ResListItem> {
    private Context context;
    private ArrayList<ResListItem> items;
    int layoutResId;
    private Typeface mTypeface;
    private Typeface mBoldTypeFace;

    public ResListAdapter(Context context, int textViewResourceId, ArrayList<ResListItem> items){
        super(context,textViewResourceId,items);
        this.layoutResId = textViewResourceId;
        this.context=context;
        this.items = items;
        mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Questrial_Regular.otf");
        mBoldTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/Quicksand_Bold.otf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;

        ViewGroup vg = (ViewGroup) convertView;
        ViewGroup root = (ViewGroup) parent.findViewById(android.R.id.content);
        ResListHolder holder = null;
        if(v==null){
            LayoutInflater vi =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(layoutResId,parent,false);
            holder = new ResListHolder();
            holder.res_image = (ImageView) v.findViewById(R.id.r_simage);
            holder.res_name = (TextView) v.findViewById(R.id.r_name);
            holder.res_cuisine = (TextView) v.findViewById(R.id.r_cuisine);
            holder.res_distance = (TextView) v.findViewById(R.id.r_distance);

            v.setTag(holder);

        }
        else{
            holder = (ResListHolder)v.getTag();
        }
        Log.e("GETVIEW",": ");
        int width_image = (int) context.getResources().getDimension(R.dimen.small_image_width);
        int height_image = (int) context.getResources().getDimension(R.dimen.small_image_height);
        ResListItem res_item = items.get(position);
        holder.res_name.setText(res_item.res_name);
        holder.res_name.setTypeface(mTypeface);
        holder.res_cuisine.setText(res_item.res_cuisine);
        holder.res_cuisine.setTypeface(mTypeface);
        holder.res_distance.setText(res_item.res_distance);
        holder.res_distance.setTypeface(mTypeface);
        //setGlobalFont(parent);
        Picasso.with(this.context).load(res_item.small_imgurl).resize(width_image, height_image).centerCrop().into(holder.res_image);
        Log.e("index", ":" + position);

        return v;
    }

    void setGlobalFont(ViewGroup root) {
        Log.e("CHILD",": "+root.getChildCount());
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof TextView)
                ((TextView)child).setTypeface(mTypeface);
            else if (child instanceof ViewGroup)
                setGlobalFont((ViewGroup)child);
        }
    }

    static class ResListHolder
    {
        ImageView res_image;
        TextView res_name;
        TextView res_cuisine;
        TextView res_distance;
    }
}
