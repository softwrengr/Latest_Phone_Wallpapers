package com.squaresdevelopers.latestphonewallpapers.fragments;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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

import com.squaresdevelopers.latestphonewallpapers.R;
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


public class WallPaperFragment extends Fragment {
    @BindView(R.id.wallpaper)
    ImageView ivWallPaper;
    @BindView(R.id.apply_wallpaper)
    LinearLayout layoutApplyWallPaper;

    View view;
    String image, strModelNo;
    Bitmap bitmap = null;
    File path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wall_paper, container, false);
        strModelNo = GeneralUtils.getModel(getActivity());
        customActionBar();

        initUI();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        onback(view);
        return view;
    }


    private void initUI() {
        NetworkUtils.grantPermession(getActivity());
        ButterKnife.bind(this, view);
        image = GeneralUtils.getImage(getActivity());

        ButterKnife.bind(this, view);
        Picasso.with(getActivity()).load(image).into(ivWallPaper);

        layoutApplyWallPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "WallPaper set Successfully", Toast.LENGTH_SHORT).show();
                FileUtilitiy.setWallPaper(getActivity(), image);
            }
        });

    }


    public class ShareTask extends AsyncTask<String, String, String> {
        private Context context;
        private ProgressDialog pDialog;
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

            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please Wait ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
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
                File filepath = Environment.getExternalStorageDirectory();
                //File dir = new File(filepath.getAbsolutePath() + "/.HD Wallpaper");
                File dir = new File(filepath.getParentFile() + "/.HD Wallpaper");
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

    public void customActionBar() {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView tvTitle = mCustomView.findViewById(R.id.title);
        ImageView share = mCustomView.findViewById(R.id.share);
        final ImageView favorite = mCustomView.findViewById(R.id.favorite);
        RelativeLayout layout_save = mCustomView.findViewById(R.id.layout_save);
        tvTitle.setText("Model " + strModelNo);
        share.setVisibility(View.VISIBLE);
        layout_save.setVisibility(View.VISIBLE);
        favorite.setVisibility(View.VISIBLE);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new ShareTask(getActivity())).execute(image);
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        layout_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    URL url = new URL(image);
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    FileUtilitiy.saveWallPaper(getActivity(),bitmap);
                } catch(IOException e) {
                    System.out.println(e);
                }
            }
        });
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.show();
    }

}

