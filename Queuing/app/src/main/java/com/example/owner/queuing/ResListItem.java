package com.example.owner.queuing;

import android.graphics.Bitmap;

/**
 * Created by mintaewon on 2015. 7. 24..
 */
public class ResListItem {
    public String small_imgurl;
    public String res_name;
    public String res_cuisine;
    public String res_distance;

    public ResListItem(String imgurl,String name, String cuisine, String distance){
        this.small_imgurl = imgurl;
        this.res_name = name;
        this.res_cuisine = cuisine;
        this.res_distance = distance;
    }
}
