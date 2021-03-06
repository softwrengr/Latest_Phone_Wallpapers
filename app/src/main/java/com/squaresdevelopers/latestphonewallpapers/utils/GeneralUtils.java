package com.squaresdevelopers.latestphonewallpapers.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.squaresdevelopers.latestphonewallpapers.R;


public class GeneralUtils {
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;


    public static Fragment connect(Context context, Fragment fragment) {
        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.zma, fragment).commit();
        return fragment;
    }

    public static Fragment connectFragment(Context context, Fragment fragment) {
        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.drawer_container, fragment).commit();
        return fragment;
    }

    public static Fragment connectFragmentWithDrawer(Context context, Fragment fragment) {
        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.drawer_container, fragment).addToBackStack("abc").commit();
        return fragment;
    }


    public static SharedPreferences.Editor putStringValueInEditor(Context context, String key, String value) {
        sharedPreferences = getSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putString(key, value).commit();
        return editor;
    }

    public static SharedPreferences.Editor putIntegerValueInEditor(Context context, String key, int value) {
        sharedPreferences = getSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putInt(key, value).commit();
        return editor;
    }

    public static SharedPreferences.Editor putBooleanValueInEditor(Context context, String key, boolean value) {
        sharedPreferences = getSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putBoolean(key, value).commit();
        return editor;
    }



    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Config.MY_PREF, 0);
    }


    public static String getID(Context context){
        return getSharedPreferences(context).getString("id","");
    }

    public static String getImage(Context context){
        return getSharedPreferences(context).getString("image","");
    }
    public static String getImageID(Context context){
        return getSharedPreferences(context).getString("image_id","");
    }
    public static String getModel(Context context){
        return getSharedPreferences(context).getString("model_no","");
    }

    public static String getName(Context context){
        return getSharedPreferences(context).getString("name","");
    }
    public static String checkLikedPicture(Context context){
        return getSharedPreferences(context).getString("liked_picture","");
    }

    public static int getImagePosition(Context context){
        return getSharedPreferences(context).getInt("position",1);
    }

    public static String getDeviceID(Context context){
       return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

}
