package com.example.owner.queuing;

/**
 * Created by mark_mac on 2015. 8. 3..
 */

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager_favorites extends SQLiteOpenHelper {
    public DBManager_favorites(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE FAVORITES( _id INTEGER PRIMARY KEY AUTOINCREMENT, res_name TEXT, cuisine TEXT, img_url TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insert(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void update(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void delete(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public String returnName(int cnt) {
        SQLiteDatabase db = getReadableDatabase();
        String str = "null";

        Cursor cursor = db.rawQuery("select res_name from FAVORITES", null);
        for(int i=0; i<cnt; i++){
            cursor.moveToNext();
            str = cursor.getString(0);
        }

        return str;
    }

    public int getTableSize(){
        SQLiteDatabase db = getReadableDatabase();
        String str = "null";
        int numRows = (int) DatabaseUtils.longForQuery(db, "select count(*) from FAVORITES", null);

        return numRows;
    }
    public String returnCusine(int cnt) {
        SQLiteDatabase db = getReadableDatabase();
        String str = "null";

        Cursor cursor = db.rawQuery("select cuisine from FAVORITES", null);
        for(int i=0; i<cnt; i++){
            cursor.moveToNext();
            str = cursor.getString(0);
        }

        return str;
    }

    public String returnImgurl(int cnt) {
        SQLiteDatabase db = getReadableDatabase();
        String str = "null";

        Cursor cursor = db.rawQuery("select img_url from FAVORITES", null);
        for(int i=0; i<cnt; i++){
            cursor.moveToNext();
            str = cursor.getString(0);
        }

        return str;
    }
    public Boolean isInDB_Favorites(String res_name){
        SQLiteDatabase db = getReadableDatabase();
        String str = "null";
        int numRows = (int) DatabaseUtils.longForQuery(db, "select count(*) from FAVORITES where res_name = '" + res_name +"'" , null);
        if(numRows ==0)
            return false;
        else
            return true;
    }

    public Boolean isEmpty(){
        SQLiteDatabase db = getReadableDatabase();
        String str = "null";
        int numRows = (int) DatabaseUtils.longForQuery(db, "select count(*) from FAVORITES", null);
        if(numRows ==0)
            return true;
        else
            return false;
    }

    public String showdatas(){
        SQLiteDatabase db = getReadableDatabase();
        String str = "null";

        Cursor cursor = db.rawQuery("select * from FAVORITES", null);
        int i=0;
        while(i<this.getTableSize()) {
            cursor.moveToNext();
            for(int j=0; j<4; j++)
                str += cursor.getString(j);
            i++;
        }

        return str;
    }

    public void deleteAll(){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("delete from FAVORITES");
    }
}
