package com.example.owner.queuing;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by mark_mac on 2015. 8. 3..
 */
public class FavoriteListAdapter extends ArrayAdapter<FavoriteListItem> {
    private Context context;
    private ArrayList<FavoriteListItem> items;
    int layoutResId;


    public FavoriteListAdapter(Context context, int textViewResourceId, ArrayList<FavoriteListItem> items){
        super(context,textViewResourceId,items);
        this.layoutResId = textViewResourceId;
        this.context=context;
        this.items = items;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent){
        View v = convertView;
        FavListHolder holder = null;
        if(v==null){
            LayoutInflater vi =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(layoutResId,parent,false);
            holder = new FavListHolder();
            holder.res_image = (ImageView) v.findViewById(R.id.img_favorites);
            holder.res_name = (TextView) v.findViewById(R.id.resname_favorites);
            holder.res_cuisine = (TextView) v.findViewById(R.id.cuisine_favorites);
            holder.res_number_line = (TextView) v.findViewById(R.id.waitings_favorites);

            v.setTag(holder);
        }
        else{
            holder = (FavListHolder)v.getTag();
        }


        View disappear_item =  v.findViewById(R.id.disappear_item);
        disappear_item.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) disappear_item.getLayoutParams();
        lp.bottomMargin = 0;
        v.requestLayout();
        if(v.getVisibility()==View.VISIBLE) Log.e("VISIBILITY","TRUE");
        else Log.e("VISIBILITY","FALSE");
        Log.e("MARGIN:"," "+items.size());

        RelativeLayout delete_btn = (RelativeLayout) v.findViewById(R.id.delete_favorites);

        int width_image = (int) context.getResources().getDimension(R.dimen.small_image_width);
        int height_image = (int) context.getResources().getDimension(R.dimen.small_image_height);
        FavoriteListItem fav_item = items.get(position);
        holder.res_name.setText(fav_item.res_name.toString());
        holder.res_cuisine.setText(fav_item.res_cuisine.toString());

        try {
            holder.res_number_line.setText(new get_linenum().execute(fav_item.res_name).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Picasso.with(this.context).load(fav_item.small_imgurl).resize(width_image, height_image).centerCrop().into(holder.res_image);
        Log.e("index", ":" + position);

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout disappear_item = (LinearLayout) parent.getChildAt(position).findViewById(R.id.disappear_item);
                ExpandAnimation ex_Ani = new ExpandAnimation(disappear_item, 500);
                ex_Ani.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        final DBManager_favorites dbManagerFavorites = new DBManager_favorites(context, "favorites.db", null, 1);
                        dbManagerFavorites.delete("delete from FAVORITES where res_name='"+items.get(position).res_name+"'");
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        items.remove(position);
                        notifyDataSetChanged();

                        Log.e("Position:", " " + position);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                disappear_item.startAnimation(ex_Ani);


            }
        });




        return v;
    }

    static class FavListHolder
    {
        ImageView res_image;
        TextView res_name;
        TextView res_cuisine;
        TextView res_number_line;
    }
    private class get_linenum extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... info) {
            String sResult = null;

            try {
                Log.d("INFO", "rest name is " + info[0]);
                URL url = new URL("http://52.69.163.43/get_linenum.php");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                String post_value = "title=" + info[0];

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(post_value);
                osw.flush();

                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str;

                while ((str = reader.readLine()) != null) {
                    builder.append(str);
                }
                sResult = builder.toString();

            } catch (Exception e) {
                Log.e("HTTPPOST","Error in Http POST REQUEST : " + e.toString());
            }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result){
            if(result.equals("CANNOT FIND RESTAURANT"))
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            else {
                Log.d("SEARCH",result);

                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
    }
}
