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
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squaresdevelopers.latestphonewallpapers.R;
import com.squaresdevelopers.latestphonewallpapers.controllers.ImageAdapter;
import com.squaresdevelopers.latestphonewallpapers.dataBase.LikedImagesCurd;
import com.squaresdevelopers.latestphonewallpapers.dataModels.wallpaperDataModel.ItemDetailModel;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WallPaperFragment extends Fragment {
    private ProgressDialog pDialog;
    AlertDialog alertDialog;
    @BindView(R.id.model_name)
    TextView tvModelName;
    @BindView(R.id.apply_wallpaper)
    LinearLayout layoutApplyWallPaper;

    View view;
    String image, strModelNo,strImageID;
    Bitmap bitmap = null;
    private boolean valid = false;
    @BindView(R.id.ad_view)
    AdView adView;
    private LikedImagesCurd likedImagesCurd;
    public static List<ItemDetailModel> itemsArrayList;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    ImageAdapter adapter;

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


        NetworkUtils.grantPermession(getActivity());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initUI();

        MobileAds.initialize(getActivity(),
                getActivity().getResources().getString(R.string.app_id));

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        onback(view);
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        NetworkUtils.grantPermession(getActivity());
        tvModelName.setText(strModelNo);

        image = GeneralUtils.getImage(getActivity());
        strImageID = GeneralUtils.getImageID(getActivity());

        adapter = new ImageAdapter(getActivity(), itemsArrayList);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(GeneralUtils.getImagePosition(getActivity()));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                GeneralUtils.putIntegerValueInEditor(getActivity(), "pager_item", i);
                image = itemsArrayList.get(i).getImage();
                strImageID = String.valueOf(itemsArrayList.get(i).getId());

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

//        if (image.equals("") || image == null) {
//            ivWallPaper.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.transparent_background));
//        } else {
//            Glide.with(getActivity()).load(image).into(ivWallPaper);
//        }

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
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub

            try {
                myFileUrl = new URL(args[0]);

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

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    File filepath = Environment.getExternalStorageDirectory();
                    File dir = new File(filepath.getAbsolutePath() + "/.HD Wallpaper");
                    deleteDir(dir);
                    GeneralUtils.connectFragment(getActivity(), new WallpaperItemsFragment());
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




    public void customActionBar() {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));
        mActionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.custom_wallp_layout, null);
        LinearLayout layoutLike,layoutShare,layoutSave,layoutSet;
        layoutLike = mCustomView.findViewById(R.id.layout_like);
        layoutSet = mCustomView.findViewById(R.id.layout_set);
        layoutSave = mCustomView.findViewById(R.id.layout_save);
        layoutShare = mCustomView.findViewById(R.id.layout_share);

        layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    likeWallpaper();

                }
            }
        });


        //saving wallpaper
        layoutSave.setOnClickListener(new View.OnClickListener() {
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
        layoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void likeWallpaper() {
        likedImagesCurd.insertSingleProduct(strImageID, image);
        GeneralUtils.connectFragmentWithDrawer(getActivity(), new LikeFragment());
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

    private boolean validate() {
        valid = true;
        if (image.isEmpty()) {
            Toast.makeText(getActivity(), "Image path not getting", Toast.LENGTH_SHORT).show();
            valid = false;
        } else if (strImageID.isEmpty()) {
            Toast.makeText(getActivity(), "you got some error please try again", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }


}