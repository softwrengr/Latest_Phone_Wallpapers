package com.squaresdevelopers.latestphonewallpapers.fragments;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squaresdevelopers.latestphonewallpapers.R;
import com.squaresdevelopers.latestphonewallpapers.dataBase.LikedImagesCurd;
import com.squaresdevelopers.latestphonewallpapers.dataModels.likeDataModel.LikeResponseModel;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiClient;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiInterface;
import com.squaresdevelopers.latestphonewallpapers.utils.AlertUtils;
import com.squaresdevelopers.latestphonewallpapers.utils.FileUtilitiy;
import com.squaresdevelopers.latestphonewallpapers.utils.GeneralUtils;
import com.squaresdevelopers.latestphonewallpapers.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Response;


public class WallPaperFragment extends Fragment {
    private ProgressDialog pDialog;
    AlertDialog alertDialog;
    @BindView(R.id.wallpaper)
    ImageView ivWallPaper;
    @BindView(R.id.apply_wallpaper)
    LinearLayout layoutApplyWallPaper;

    View view;
    String image, strModelNo, strUUID;
    Bitmap bitmap = null;
    private boolean valid = false;
    @BindView(R.id.ad_view)
    AdView adView;
    private ImageView ivFavorite;
    private boolean aBooleanLikeImage;

    private LikedImagesCurd likedImagesCurd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wall_paper, container, false);
        strModelNo = GeneralUtils.getModel(getActivity());
        pDialog = AlertUtils.createProgressBar(getActivity());
        customActionBar();
        onback(view);
        likedImagesCurd = new LikedImagesCurd(getActivity());

        aBooleanLikeImage = GeneralUtils.getSharedPreferences(getActivity()).getBoolean("like_image", false);
        if (!aBooleanLikeImage) {
            ivFavorite.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.like));
        }

        initUI();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        MobileAds.initialize(getActivity(),
                getActivity().getResources().getString(R.string.app_id));

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        return view;
    }


    private void initUI() {
        ButterKnife.bind(this, view);
        NetworkUtils.grantPermession(getActivity());

        image = GeneralUtils.getImage(getActivity());
        strUUID = GeneralUtils.getDeviceID(getActivity());

        if (image.equals("") || image == null) {
            ivWallPaper.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.transparent_background));
        } else {
            Picasso.with(getActivity()).load(image).into(ivWallPaper);
        }


        layoutApplyWallPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog = AlertUtils.createProgressDialog(getActivity());
                alertDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        boolean setWallpaper = FileUtilitiy.setWallPaper(getActivity(), image);
                        if (setWallpaper) {
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 300);


            }
        });

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

    private void onback(View view) {

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //  Log.i(tag, "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    //   Log.i(tag, "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    File filepath = Environment.getExternalStorageDirectory();
                    File dir = new File(filepath.getAbsolutePath() + "/.HD Wallpaper");
                    deleteDir(dir);
                    return true;
                }
                return false;
            }
        });

    }


    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
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
                        GeneralUtils.connectFragmentWithDrawer(getActivity(), new LikeFragment());
                        GeneralUtils.putStringValueInEditor(getActivity(), "liked_picture", image);
                    }


                } else {
                    Toast.makeText(getActivity(), "you got some error", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Image path not getting", Toast.LENGTH_SHORT).show();
            valid = false;
        } else if (strUUID.isEmpty()) {
            Toast.makeText(getActivity(), "you got some error please try again", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }


    public void customActionBar() {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView tvTitle = mCustomView.findViewById(R.id.title);
        ImageView share = mCustomView.findViewById(R.id.share);
        ivFavorite = mCustomView.findViewById(R.id.favorite);
        RelativeLayout layout_save = mCustomView.findViewById(R.id.layout_save);
        tvTitle.setText(strModelNo);
        share.setVisibility(View.VISIBLE);
        layout_save.setVisibility(View.VISIBLE);
        ivFavorite.setVisibility(View.VISIBLE);

        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivFavorite.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.like));
                if (validate()) {

                    if (image.equals(GeneralUtils.checkLikedPicture(getActivity()))) {
                        GeneralUtils.connectFragmentWithDrawer(getActivity(), new LikeFragment());
                    } else {
                        pDialog = AlertUtils.createProgressBar(getActivity());
                        pDialog.show();
                        apiCallLiked();
                    }

                }
            }
        });


        //saving wallpaper
        layout_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = AlertUtils.createProgressBar(getActivity());
                pDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initSaveWallpaper();
                    }
                }, 800);
            }
        });
        //sharing wallpaper
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new ShareTask(getActivity())).execute(image);
            }
        });


        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.show();
    }

    private void initSaveWallpaper() {

        try {
            URL url = new URL(image);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            boolean isSaveImage = FileUtilitiy.saveWallPaper(getActivity(), bitmap);
            if (isSaveImage) {
                pDialog.dismiss();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}


