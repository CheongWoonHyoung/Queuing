package com.example.owner.queuing;

/**
 * Created by mark_mac on 2015. 8. 3..
 */

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public String returnName(int index) {
        SQLiteDatabase db = getReadableDatabase();
        String str = "null";

        Cursor cursor = db.rawQuery("select res_name from FAVORITES where _id=" + Integer.toString(index), null);
        while(cursor.moveToNext()) {
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
    public String returnCusine(int index) {
        SQLiteDatabase db = getReadableDatabase();
        String str = "null";

        Cursor cursor = db.rawQuery("select cuisine from FAVORITES where _id=" + Integer.toString(index) , null);
        while(cursor.moveToNext()) {
            str = cursor.getString(0);
        }

        return str;
    }

    public String returnImgurl(int index) {
        SQLiteDatabase db = getReadableDatabase();
        String str = "first";

        Cursor cursor = db.rawQuery("select img_url from FAVORITES where _id=" + Integer.toString(index), null);
        while(cursor.moveToNext()) {
            str = cursor.getString(0);
        }

        return str;
    }
}
