package com.squaresdevelopers.latestphonewallpapers.utils;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by eapple on 03/12/2018.
 */

public class FileUtilitiy {

    public static boolean setWallPaper(Context context, ImageView string) {
        boolean setWallpaper = false;
        Bitmap bitmap;
        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context);
        try {

            bitmap = ((BitmapDrawable) string.getDrawable()).getBitmap();
            myWallpaperManager.setBitmap(bitmap);
            Toast.makeText(context, "WallPaper set Successfully", Toast.LENGTH_SHORT).show();
            setWallpaper = true;


        } catch (IOException e) {
            Log.d("zma", e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return setWallpaper;
    }

    public static boolean saveWallPaper(Context context, Bitmap bm) throws IOException {
        Toast.makeText(context, "WallPaper saved in folder Awalls", Toast.LENGTH_SHORT).show();

        boolean imageSave = false;

        Date currentTime = Calendar.getInstance().getTime();
        String dataTime = String.valueOf(currentTime);
       // Environment.DIRECTORY_PICTURES
                File path = Environment.getExternalStoragePublicDirectory("Awalls");

        if (!path.exists()) {
            path.mkdirs();
        }
        File imageFile = new File(path, dataTime + ".PNG");
        try {
            FileOutputStream out = new FileOutputStream(imageFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out); // Compress Image
            out.flush();
            out.close();

            imageSave = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }

        return imageSave;
    }
}
