package com.squaresdevelopers.latestphonewallpapers.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    private ProgressDialog pDialog;
    AlertDialog alertDialog;
    @BindView(R.id.like_wallpaper)
    ImageView ivWallPaper;
    @BindView(R.id.like_apply_wallpaper)
    LinearLayout layoutApplyWallPaper;

    View view;
    String image, strModelNo, strImageUrl;
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
        likedImagesCurd  =new LikedImagesCurd(getActivity());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        return view;
    }


    private void initUI() {
        ButterKnife.bind(this, view);
        NetworkUtils.grantPermession(getActivity());

        image = GeneralUtils.getImage(getActivity());
        Bundle bundle = this.getArguments();
        imageId = bundle.getInt("delete");
        strImageUrl = bundle.getString("image_url");


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
                        if (setWallpaper){
                            alertDialog.dismiss();
                        }else {
                            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },300);

            }
        });

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
        RelativeLayout layout_save = mCustomView.findViewById(R.id.layout_save);
        TextView tvUnlike = mCustomView.findViewById(R.id.tvUnlike);
        tvTitle.setText("Liked");
        share.setVisibility(View.VISIBLE);
        layout_save.setVisibility(View.VISIBLE);
        tvUnlike.setVisibility(View.VISIBLE);


        tvUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {
                    pDialog = AlertUtils.createProgressBar(getActivity());
                    pDialog.show();
                    apiCallUnLiked();

                }
            }
        });


        //saving wallpaper
        layout_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Saving please wait a while!", Toast.LENGTH_SHORT).show();
                try {
                    URL url = new URL(image);
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    FileUtilitiy.saveWallPaper(getActivity(), bitmap);
                } catch (IOException e) {
                    System.out.println(e);
                }
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
            // TODO Auto-generated method stub

            super.onPreExecute();
            pDialog = AlertUtils.createProgressBar(getActivity());

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

    private void apiCallUnLiked() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DELETE + String.valueOf(imageId)
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("Liked Wallpaper successfully deleted")) {

                        likedImagesCurd.deleteLikeImage(strImageUrl);
                        GeneralUtils.connectFragmentWithDrawer(getActivity(), new LikeFragment());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("_method","DELETE");

                return params;
            }
        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }


}
