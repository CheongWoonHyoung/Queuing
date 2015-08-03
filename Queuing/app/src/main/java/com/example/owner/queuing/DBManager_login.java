package com.example.owner.queuing;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mintaewon on 2015. 7. 12..
 */
public class DBManager_login extends SQLiteOpenHelper {
    public DBManager_login(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IS_LOGIN( _id INTEGER PRIMARY KEY AUTOINCREMENT, is_login TEXT, _auth TEXT, _user TEXT, _email TEXT);");
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

    public String returnUser() {
        SQLiteDatabase db = getReadableDatabase();
        String str = "null";

        Cursor cursor = db.rawQuery("select _user from IS_LOGIN where _id=1", null);
        while(cursor.moveToNext()) {
            str = cursor.getString(0);
        }

        return str;
    }

    public String returnEmail() {
        SQLiteDatabase db = getReadableDatabase();
        String str = "null";

        Cursor cursor = db.rawQuery("select _email from IS_LOGIN where _id=1", null);
        while(cursor.moveToNext()) {
            str = cursor.getString(0);
        }

        return str;
    }

    public String returnData() {
        SQLiteDatabase db = getReadableDatabase();
        String str = "first";

        Cursor cursor = db.rawQuery("select is_login from IS_LOGIN where _id=1", null);
        while(cursor.moveToNext()) {
            str = cursor.getString(0);
        }

        return str;
    }
    public String returnAuth() {
        SQLiteDatabase db = getReadableDatabase();
        String str = "first";

        Cursor cursor = db.rawQuery("select _auth from IS_LOGIN where _id=1", null);
        while(cursor.moveToNext()) {
            str = cursor.getString(0);
        }

        return str;
    }
}
