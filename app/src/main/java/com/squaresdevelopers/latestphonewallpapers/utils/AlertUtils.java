package com.squaresdevelopers.latestphonewallpapers.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squaresdevelopers.latestphonewallpapers.R;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;


public class AlertUtils {

    public static AlertDialog createProgressDialog(Activity activity) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.progress_dialog
                , null);

        dialogBuilder.setView(dialogView);
        ImageView pd = dialogView.findViewById(R.id.indeterminateBar);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialoge_box);
        alertDialog.getWindow().setAttributes(lp);
        pd.setVisibility(View.VISIBLE);
        return alertDialog;

    }

    public static ProgressDialog createProgressBar(Activity activity){
        ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Please Wait ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        //pDialog.show();
        return pDialog;
    }

    public static void showFancyDialog(final Activity activity, String string){
        new LovelyInfoDialog(activity)
        .setTopColorRes(R.color.sky)
                .setIcon(R.drawable.cancel)
                .setTitle(string)
                .setMessage("you have lost your internet connection")
                .show();
    }


}
