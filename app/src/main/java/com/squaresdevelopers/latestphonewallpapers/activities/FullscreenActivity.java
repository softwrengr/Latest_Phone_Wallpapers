package com.squaresdevelopers.latestphonewallpapers.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squaresdevelopers.latestphonewallpapers.R;
import com.squaresdevelopers.latestphonewallpapers.classes.BottomSheetClass;
import com.squaresdevelopers.latestphonewallpapers.dataBase.LikedImagesCurd;
import com.squaresdevelopers.latestphonewallpapers.dataModels.likeDataModel.LikeResponseModel;
import com.squaresdevelopers.latestphonewallpapers.fragments.CatogoryWallpaperFragment;
import com.squaresdevelopers.latestphonewallpapers.fragments.LikeFragment;
import com.squaresdevelopers.latestphonewallpapers.fragments.WallPaperFragment;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiClient;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiInterface;
import com.squaresdevelopers.latestphonewallpapers.utils.AlertUtils;
import com.squaresdevelopers.latestphonewallpapers.utils.FileUtilitiy;
import com.squaresdevelopers.latestphonewallpapers.utils.GeneralUtils;
import com.squaresdevelopers.latestphonewallpapers.utils.NetworkUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity implements BottomSheetClass.BottomSheetListener {
    public ProgressDialog pDialog;
    AlertDialog alertDialog;
    @BindView(R.id.wallpaper)
    ImageView ivWallPaper;
    @BindView(R.id.wallpaper_model)
    TextView tvWallPaperModel;
    String image, strModelNo, strUUID;
    Bitmap bitmap = null;
    private boolean valid = false;
    @BindView(R.id.ad_view)
    AdView adView;
    private LikedImagesCurd likedImagesCurd;

    @BindView(R.id.top)
    LinearLayout layoutTop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        this.getSupportActionBar().hide();
        ButterKnife.bind(this);

        layoutTop.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        strModelNo = GeneralUtils.getModel(FullscreenActivity.this);
        pDialog = AlertUtils.createProgressBar(FullscreenActivity.this);
        likedImagesCurd = new LikedImagesCurd(FullscreenActivity.this);
        tvWallPaperModel.setText(strModelNo);


        NetworkUtils.grantPermession(FullscreenActivity.this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initUI();

        MobileAds.initialize(FullscreenActivity.this,
                FullscreenActivity.this.getResources().getString(R.string.app_id));

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void initUI() {
        NetworkUtils.grantPermession(FullscreenActivity.this);

        image = GeneralUtils.getImage(FullscreenActivity.this);
        strUUID = GeneralUtils.getDeviceID(FullscreenActivity.this);

        if (image.equals("") || image == null) {
            ivWallPaper.setImageDrawable(FullscreenActivity.this.getResources().getDrawable(R.drawable.transparent_background));
        } else {
            Glide.with(FullscreenActivity.this).load(image).into(ivWallPaper);
        }


        //new BottomSheetClass().show(getSupportFragmentManager(), "bottomSheet");

    }


    public class ShareTask extends AsyncTask<String, String, String> {
        private Context context;
        URL myFileUrl;
        Bitmap bmImg = null;
        File file;

        public ShareTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            super.onPreExecute();
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub

            try {

                myFileUrl = new URL(args[0]);
                //myFileUrl1 = args[0];

                HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bmImg = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {


                String path = myFileUrl.getPath();
                String idStr = path.substring(path.lastIndexOf('/') + 1);
                File dir = new File(Environment.getExternalStorageDirectory(), "/.HD Wallpaper");

                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String fileName = idStr;

                file = new File(dir, fileName);


                FileOutputStream fos = new FileOutputStream(file);
                bmImg.compress(Bitmap.CompressFormat.PNG, 75, fos);
                fos.flush();
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String args) {
            // TODO Auto-generated method stub

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/png");
            Log.d("zma", String.valueOf(file.getAbsolutePath()));
            share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getAbsolutePath()));
            startActivity(Intent.createChooser(share, "Share Image"));
            pDialog.dismiss();
        }
    }

    private void apiCallLiked() {

        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);

        retrofit2.Call<LikeResponseModel> userLogin = services.like(image, strUUID, strModelNo);
        userLogin.enqueue(new Callback<LikeResponseModel>() {
            @Override
            public void onResponse(retrofit2.Call<LikeResponseModel> call, Response<LikeResponseModel> response) {
                pDialog.dismiss();
                if (response.body().getMessage().equals("Image Like successfully")) {


                    if (likedImagesCurd.checkImageUrl(image)) {


                        likedImagesCurd.insertData(image);
                        //GeneralUtils.connectFragmentWithDrawer(FullscreenActivity.this, new LikeFragment());
                        GeneralUtils.putStringValueInEditor(FullscreenActivity.this, "liked_picture", image);
                    }


                } else {
                    Toast.makeText(FullscreenActivity.this, "you got some error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<LikeResponseModel> call, Throwable t) {
                Log.d("error", t.getMessage());
                pDialog.dismiss();
            }
        });

    }


    private boolean validate() {
        valid = true;

        if (image.isEmpty()) {
            Toast.makeText(FullscreenActivity.this, "Image path not getting", Toast.LENGTH_SHORT).show();
            valid = false;
        } else if (strUUID.isEmpty()) {
            Toast.makeText(FullscreenActivity.this, "you got some error please try again", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }


    private void initSaveWallpaper() {

        try {
            URL url = new URL(image);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            boolean isSaveImage = FileUtilitiy.saveWallPaper(FullscreenActivity.this, bitmap);
            if (isSaveImage) {
                pDialog.dismiss();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    //interface method calling
    @Override
    public void onButtonClicked(String message) {
        switch (message) {
            case "like":
                if (validate()) {

                    if (image.equals(GeneralUtils.checkLikedPicture(FullscreenActivity.this))) {
                       // GeneralUtils.connectFragmentWithDrawer(FullscreenActivity.this, new LikeFragment());
                    } else {
                        pDialog = AlertUtils.createProgressBar(FullscreenActivity.this);
                        pDialog.show();
                        apiCallLiked();
                    }

                }
                break;
            case "share":
                (new ShareTask(FullscreenActivity.this)).execute(image);
                break;
            case "set":
                alertDialog = AlertUtils.createProgressDialog(FullscreenActivity.this);
                alertDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        boolean setWallpaper = FileUtilitiy.setWallPaper(FullscreenActivity.this, ivWallPaper);
                        if (setWallpaper) {
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(FullscreenActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 300);
                break;

            case "save":
                pDialog = AlertUtils.createProgressBar(FullscreenActivity.this);
                pDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initSaveWallpaper();
                    }
                }, 800);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GeneralUtils.connect(this, new CatogoryWallpaperFragment());
    }
}



