package com.example.owner.queuing;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mintaewon on 2015. 7. 12..
 */
public class DBManager_reserv extends SQLiteOpenHelper {
    public DBManager_reserv(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE RESERV_LIST( _id INTEGER PRIMARY KEY AUTOINCREMENT, res_name TEXT, party TEXT);");
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


    public String returnData() {
        SQLiteDatabase db = getReadableDatabase();
        String str = "nothing";

        Cursor cursor = db.rawQuery("select _id from RESERV_LIST", null);
        while(cursor.moveToNext()) {
            str = cursor.getString(0);
        }

        return str;
    }

    public String returnParty() {
        SQLiteDatabase db = getReadableDatabase();
        String str = "nothing";

        Cursor cursor = db.rawQuery("select party from RESERV_LIST", null);
        while(cursor.moveToNext()) {
            str = cursor.getString(0);
        }

        return str;
    }
    public String returnName() {
        SQLiteDatabase db = getReadableDatabase();
        String str = "nothing";

        Cursor cursor = db.rawQuery("select res_name from RESERV_LIST", null);
        while(cursor.moveToNext()) {
            str = cursor.getString(0);
        }

        return str;
    }

}
