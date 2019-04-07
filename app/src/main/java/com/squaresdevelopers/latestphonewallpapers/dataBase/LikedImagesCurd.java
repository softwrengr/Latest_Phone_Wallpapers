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
        LikedImagesDataBase database = new LikedImagesDataBase(context);
        sqLiteDatabase = database.getWritableDatabase();
        this.context = context;
    }

    //inserting single products to cart
    public void insertSingleProduct(String image_id,String image_url) {

        if (!checkProduct(image_id)) {
            ContentValues values = new ContentValues();
            values.put("IMAGE_ID", image_id);
            values.put("IMAGE_URL",image_url);
            sqLiteDatabase.insert("LIKE_IMAGES", null, values);
            Toast.makeText(context, "Successful liked", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "already liked", Toast.LENGTH_SHORT).show();
        }
    }



    //check for single products
    public boolean checkProduct(String image_id) {
        Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM LIKE_IMAGES WHERE IMAGE_ID = '" + image_id + "' ", null);
        boolean isItemAddChart = false;
        if (cursor.moveToFirst()) {
            isItemAddChart = true;
        }
        return isItemAddChart;

    }

    //fetching all products
    public Cursor getProducts() {
        String query = "SELECT * FROM LIKE_IMAGES";

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        return cursor;
    }

    //deleting item from cart table
    public void delete(String imageID) {
        this.sqLiteDatabase.delete("LIKE_IMAGES", "IMAGE_ID = '" + imageID + "'", null);
        Toast.makeText(context, "delete item successful", Toast.LENGTH_SHORT).show();
    }

    public void clearData(){
        String clearData = "DELETE  FROM CART_TABLE";
        sqLiteDatabase.execSQL(clearData);
    }


}
