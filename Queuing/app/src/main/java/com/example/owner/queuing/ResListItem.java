package com.example.owner.queuing;

import android.graphics.Bitmap;

/**
 * Created by mintaewon on 2015. 7. 24..
 */
class ResListItem {
    public Bitmap res_img;
    public String res_name;
    public String res_cuisine;
    public String res_distance;

    public ResListItem(Bitmap img,String name, String cuisine, String distance){
        this.res_img = img;
        this.res_name = name;
        this.res_cuisine = cuisine;
        this.res_distance = distance;
    }
}
