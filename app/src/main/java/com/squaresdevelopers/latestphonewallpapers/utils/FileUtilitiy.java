package com.squaresdevelopers.latestphonewallpapers.utils;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

/**
 * Created by eapple on 03/12/2018.
 */

public class FileUtilitiy {


    public static void setWallPaper(Context context,String string){
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(context);
        try {
            URL url = new URL(string);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            myWallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
