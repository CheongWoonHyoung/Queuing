package com.example.owner.queuing;

/**
 * Created by mintaewon on 2015. 7. 27..
 */
public class CusListItem {
    public String cus_priority;
    public String cus_name;
    public String cus_number;
    public String cus_method;

    public CusListItem(String priority, String name,String method, String number){
        this.cus_priority = priority;
        this.cus_name = name;
        this.cus_number = number;
        this.cus_method = method;
    }

}
