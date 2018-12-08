package com.squaresdevelopers.latestphonewallpapers.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squaresdevelopers.latestphonewallpapers.R;
import com.squaresdevelopers.latestphonewallpapers.controllers.CategoriesAdapter;
import com.squaresdevelopers.latestphonewallpapers.dataModels.CategoryModel;
import com.squaresdevelopers.latestphonewallpapers.utils.AlertUtils;
import com.squaresdevelopers.latestphonewallpapers.utils.Config;
import com.squaresdevelopers.latestphonewallpapers.utils.GeneralUtils;
import com.squaresdevelopers.latestphonewallpapers.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {
    View view;
    android.support.v7.app.AlertDialog alertDialog;
    private AdView mAdView;
    private AdRequest adRequest;
    @BindView(R.id.rvCategory)
    RecyclerView rvCategories;
    CategoriesAdapter categoriesAdapter;
    ArrayList<CategoryModel> categoryModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Latest Phone Wallpapers");

        if(!NetworkUtils.isNetworkConnected(getActivity())){
            Toast.makeText(getActivity(), "you have lost your internet connection", Toast.LENGTH_SHORT).show();
        }

        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);

        RecyclerView.LayoutManager layoutManager =  new LinearLayoutManager(getActivity());
        rvCategories.setLayoutManager(layoutManager);
        categoryModelList = new ArrayList<>();
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        categoriesAdapter = new CategoriesAdapter(getActivity(),categoryModelList);
        rvCategories.setAdapter(categoriesAdapter);
        categoriesAdapter.notifyDataSetChanged();
        apiCall();
    }


    public void apiCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.CATEGORIES
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alertDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject object = dataArray.getJSONObject(i);
                        String id = object.getString("id");
                        String name = object.getString("category_name");
                        String image = object.getString("image");

                        CategoryModel model = new CategoryModel();
                        model.setId(id);
                        model.setName(name);
                        model.setImage(image);

                        categoryModelList.add(model);
                        categoriesAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }


    public void customActionBar() {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView tvTitle = mCustomView.findViewById(R.id.title);
        ImageView ivFeedback = mCustomView.findViewById(R.id.ivFeedback);
        tvTitle.setText("HD WallPapers");
        ivFeedback.setVisibility(View.VISIBLE);
        ivFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogFeedBack();
            }
        });
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.show();
    }

    private void showDialogFeedBack(){
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog_layout);
        dialog.show();

        GeneralUtils.connectFragment(getActivity(),new TabsFragment());
    }

}
