package com.iiit.amaresh.demotrack.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;

import com.iiit.amaresh.demotrack.Pojo.Oflinedata;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RN on 11/5/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "PRConnect.db";
    public static final int VERSON = 1;
    private String DROP_TABLE="drop table if exists data";
    private String ALLUSER_TABLE = "Alluser";
    private String USER_COLUMN_ID="userid";
    private String USER_LATITUDE="latitude";
    private String USER_LONGITUDE="longitude";
    private String USER_TITLE="usertitle";
    private String USER_ADDRESS="ownaddress";
    private String USER_VIDEO="video";
    private String BYTE_ARRAY="bytearray";
    private String USER_FILE="file";

    public DBHelper(Context context) {

        super(context, DBNAME, null, VERSON);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ALLUSER_TABLE +
                "(" + USER_COLUMN_ID + " text, "
                + USER_LATITUDE + " text, "
                + USER_LONGITUDE + " text, "
                + USER_TITLE +" text, "
                + USER_ADDRESS + " text, "
                + USER_VIDEO +" text, "
                + USER_FILE + " text) "
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ALLUSER_TABLE);
        onCreate(sqLiteDatabase);
    }


    public void insertasset(Oflinedata data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_COLUMN_ID, data.getUser_id());
        contentValues.put(USER_LATITUDE, data.getLatitude());
        contentValues.put(USER_LONGITUDE, data.getLongitude());
        contentValues.put(USER_TITLE, data.getTitle());
        contentValues.put(USER_ADDRESS, data.getAddress());
        contentValues.put(USER_VIDEO, data.getVideo());
        contentValues.put(USER_FILE, data.getImage());
       db.insertWithOnConflict(ALLUSER_TABLE, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        db.close(); // Closing database connection

    }

    public List<Oflinedata> getOflinedata() {
        ArrayList<Oflinedata> assetlist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT  * FROM " + ALLUSER_TABLE,null);
            if (res.moveToFirst()) {
                do {
                    Oflinedata user = new Oflinedata();
                    user.setUser_id(res.getInt(0));
                    user.setlatitude(res.getString(1));
                    user.setlongitude(res.getString(2));
                    user.setaddress(res.getString(3));
                    user.settitle(res.getString(4));
                    user.setvideo(res.getString(5));
                    user.setimage(res.getString(6));
                    assetlist.add(user);
                } while (res.moveToNext());
            }
        //res.close();
        return assetlist;
    }


}