package com.example.owner.queuing;

/**
 * Created by mark_mac on 2015. 8. 3..
 */
public class FavoriteListItem{
    public String res_name;
    public String res_cuisine;
    public String small_imgurl;
    public String line_num;

    public FavoriteListItem(String name, String imgurl, String cuisine, String line_num){
        this.res_name = name;
        this.small_imgurl = imgurl;
        this.res_cuisine = cuisine;
        this.line_num = line_num;
    }
}
