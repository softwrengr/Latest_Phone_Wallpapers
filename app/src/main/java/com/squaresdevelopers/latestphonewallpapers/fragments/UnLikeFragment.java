package com.squaresdevelopers.latestphonewallpapers.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squaresdevelopers.latestphonewallpapers.R;
import com.squaresdevelopers.latestphonewallpapers.dataBase.LikedImagesCurd;
import com.squaresdevelopers.latestphonewallpapers.dataModels.CategoryModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.likeDataModel.LikeResponseModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.unLikeDataModel.UnLikeModel;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiClient;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiInterface;
import com.squaresdevelopers.latestphonewallpapers.utils.AlertUtils;
import com.squaresdevelopers.latestphonewallpapers.utils.Config;
import com.squaresdevelopers.latestphonewallpapers.utils.FileUtilitiy;
import com.squaresdevelopers.latestphonewallpapers.utils.GeneralUtils;
import com.squaresdevelopers.latestphonewallpapers.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;

public class UnLikeFragment extends Fragment {
    AlertDialog alertDialog;
    @BindView(R.id.like_wallpaper)
    ImageView ivWallPaper;

    View view;
    String image, strImageUrl;
    Bitmap bitmap = null;
    private boolean valid = false;
    int imageId;

    private LikedImagesCurd likedImagesCurd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_un_like, container, false);
        customActionBar();
        initUI();
        likedImagesCurd = new LikedImagesCurd(getActivity());

        return view;
    }


    private void initUI() {
        ButterKnife.bind(this, view);
        NetworkUtils.grantPermession(getActivity());
        strictMode();

        image = GeneralUtils.getImage(getActivity());
        Bundle bundle = this.getArguments();
        imageId = bundle.getInt("delete");
        strImageUrl = bundle.getString("image_url");


        if (image.equals("") || image == null) {
            ivWallPaper.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.transparent_background));
        } else {
            Glide.with(getActivity()).load(image).into(ivWallPaper);
        }

    }


    private boolean validate() {
        valid = true;
        if (image.isEmpty()) {
            Toast.makeText(getActivity(), "Image path not getting", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
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
            super.onPreExecute();
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
            alertDialog.dismiss();
        }
    }

    private void apiCallUnLiked() {
        likedImagesCurd.delete(String.valueOf(imageId));
        GeneralUtils.connectFragmentWithDrawer(getActivity(), new LikeFragment());
    }

    public void customActionBar() {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));
        mActionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.custom_wallp_layout, null);
        LinearLayout layoutUnlike, layoutShare, layoutSave, layoutSet;
        TextView tvUnlike = mCustomView.findViewById(R.id.like_wall);
        layoutUnlike = mCustomView.findViewById(R.id.layout_like);
        layoutSet = mCustomView.findViewById(R.id.layout_set);
        layoutSave = mCustomView.findViewById(R.id.layout_save);
        layoutShare = mCustomView.findViewById(R.id.layout_share);
        tvUnlike.setText("Unlike");


        layoutUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {
                    apiCallUnLiked();

                }
            }
        });


        //saving wallpaper
        layoutSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = AlertUtils.createProgressDialog(getActivity());
                alertDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        saveWallpaper();
                    }
                }, 800);

            }
        });
        //sharing wallpaper
        layoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = AlertUtils.createProgressDialog(getActivity());
                alertDialog.show();
                (new ShareTask(getActivity())).execute(image);
            }
        });

        layoutSet.setOnClickListener(new View.OnClickListener() {
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


        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.show();
    }

    private void saveWallpaper(){
        try {
            URL url = new URL(image);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            boolean isSaveImage = FileUtilitiy.saveWallPaper(getActivity(), bitmap);
            if(isSaveImage){
                alertDialog.dismiss();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void strictMode(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

}
