package com.squaresdevelopers.latestphonewallpapers.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by AttaUrRahman on 5/7/2018.
 */

public class LikedImagesDataBase extends SQLiteOpenHelper {
    private static String DB_NAME = "LIKED_IMAGE_DB";
    public static int DB_VERSION = 1;

    public LikedImagesDataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE LIKED_IMAGE_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT,URL)";
        sqLiteDatabase.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
