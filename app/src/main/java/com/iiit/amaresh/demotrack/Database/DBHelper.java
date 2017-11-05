package com.iiit.amaresh.demotrack.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by RN on 11/5/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "PRConnect.db";
    public static final int VERSON = 1;
    private String DROP_TABLE="drop table if exists data";
    private String ALLUSER_TABLE = "alluser";
    private String USER_COLUMN_ID="userid";
    private String USER_LATITUDE="latitude";
    private String USER_LONGITUDE="longitude";
    private String USER_TITLE="usertitle";
    private String USER_ADDRESS="address";
    private String USER_VIDEO="video";
    private String USER_FILE="file";

    public DBHelper(Context context) {

        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ALLUSER_TABLE +
                "(" + USER_COLUMN_ID + " text ,"
                + USER_LATITUDE + " text ,"
                + USER_LONGITUDE + " text ,"
                + USER_TITLE +" text ," +
                USER_ADDRESS + "text ," +
                USER_VIDEO+"text ," +USER_FILE+"text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public boolean insertasset(int user_id, String latitude, String longitude, String s_title,
                               String saddress, File video, File file) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_COLUMN_ID, user_id);
        contentValues.put(USER_LATITUDE, latitude);
        contentValues.put(USER_LONGITUDE, longitude);
        contentValues.put(USER_TITLE, s_title);
        contentValues.put(USER_ADDRESS, saddress);
        contentValues.put(USER_VIDEO, String.valueOf(video));
        contentValues.put(USER_FILE, String.valueOf(file));

        db.insert(ALLUSER_TABLE, null, contentValues);
        db.close();
        return true;
    }
}