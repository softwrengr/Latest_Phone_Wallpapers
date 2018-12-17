package com.squaresdevelopers.latestphonewallpapers.utils;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
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


    public static boolean setWallPaper(Context context, String string) {
        boolean setWallpaper = false;
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(context);
        try {
            URL url = new URL(string);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            myWallpaperManager.setBitmap(bitmap);
            Toast.makeText(context, "WallPaper set Successfully", Toast.LENGTH_SHORT).show();
            setWallpaper = true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return setWallpaper;
    }

    public static boolean saveWallPaper(Context context, Bitmap bm) throws IOException {
        Toast.makeText(context, "WallPaper saved in folder Latest WallPaper", Toast.LENGTH_SHORT).show();

        boolean imageSave = false;

        Date currentTime = Calendar.getInstance().getTime();
        String dataTime = String.valueOf(currentTime);
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "Latest WallPaper");

        if (!path.exists()) {
            path.mkdirs();
        }
        File imageFile = new File(path, dataTime + ".PNG");
        try {
            FileOutputStream out = new FileOutputStream(imageFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out); // Compress Image
            out.flush();
            out.close();

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
//            MediaScannerConnection.scanFile(context, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
//                public void onScanCompleted(String path, Uri uri) {
//                    Log.i("ExternalStorage", "Scanned " + path + ":");
//                    Log.i("ExternalStorage", "-> uri=" + uri);
//
//
//                }
//            });
            imageSave = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }

        return imageSave;
    }
}
