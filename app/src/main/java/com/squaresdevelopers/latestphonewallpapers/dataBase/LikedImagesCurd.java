package com.squaresdevelopers.latestphonewallpapers.dataBase;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.renderscript.Script;
import android.widget.Toast;

import com.squaresdevelopers.latestphonewallpapers.dataModels.LikedImagesmodel;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by AttaUrRahman on 5/7/2018.
 */

public class LikedImagesCurd {

    private static SQLiteDatabase sqLiteDatabase;

    private Context context;

    public LikedImagesCurd(Context context) {
        LikedImagesDataBase likedImagesDataBase = new LikedImagesDataBase(context);
        sqLiteDatabase = likedImagesDataBase.getWritableDatabase();
        this.context = context;
    }

    public void insertData(String strImageUrl) {

        ContentValues values = new ContentValues();
        values.put("URL", strImageUrl);

        sqLiteDatabase.insert("LIKED_IMAGE_TABLE", null, values);
        Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();


    }

    public void DeleteLikeImage(String strLikeImage) {

        if (checkImageUrl(strLikeImage)) {
            Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM LIKED_IMAGE_TABLE WHERE URL = '" + strLikeImage + "' ", null);
            if (cursor.moveToFirst()) {

                this.sqLiteDatabase.delete("ORDER_NAME_TABLE", "URL = '" + strLikeImage + "'", null);
                Toast.makeText(context, "unlike successful", Toast.LENGTH_SHORT).show();


            }
        }


    }

    public boolean checkImageUrl(String strUrl) {

        Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM LIKED_IMAGE_TABLE WHERE URL = '" + strUrl + "' ", null);
        boolean isCheckUrl = true;
        if (cursor.moveToFirst()) {
            isCheckUrl = false;
        }
        return isCheckUrl;
    }


}
