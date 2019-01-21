package com.squaresdevelopers.latestphonewallpapers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import com.squaresdevelopers.latestphonewallpapers.dataModels.categoryListDataModel.CategorResponseModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.categoryListDataModel.CategoryDetailModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.showLikeDataModel.ShowLikeResponseModel;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiClient;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiInterface;
import com.squaresdevelopers.latestphonewallpapers.networking.NetworkingCall;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;


public class HomeFragment extends Fragment {
    View view;
    android.support.v7.app.AlertDialog alertDialog;
    @BindView(R.id.rvCategory)
    RecyclerView rvCategories;
    CategoriesAdapter categoriesAdapter;
    ArrayList<CategoryDetailModel> categoryModelList;
    public static String strFiltercategory = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        customActionBar();

        if (!NetworkUtils.isNetworkConnected(getActivity())) {
            AlertUtils.showFancyDialog(getActivity(), "Internet Connection Problem");
        } else {
            initUI(strFiltercategory);
        }

        return view;
    }

    public void initUI(String s) {
        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvCategories.setLayoutManager(layoutManager);
        categoryModelList = new ArrayList<>();
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        getList(s);

    }

    //cutomAction Bar
    public void customActionBar() {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView tvTitle = mCustomView.findViewById(R.id.title);
        final TextView tvFilter = mCustomView.findViewById(R.id.ivFilter);
        tvTitle.setText(getResources().getString(R.string.app_name));
        tvFilter.setVisibility(View.VISIBLE);
        tvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDropDownMenu(tvFilter);

            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.show();

    }


    private void showDropDownMenu(TextView layout) {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(getActivity(), layout);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.all:
                        initUI("");
                        break;
                    case R.id.samsung:
                        initUI("Samsung");
                        break;
                    case R.id.apple:
                        initUI("Apple");
                        break;
                    case R.id.nokia:
                        initUI("Nokia");
                        break;
                    case R.id.huawei:
                        initUI("Huawei");
                        break;
                    case R.id.blackberry:
                        initUI("Blackberry");
                        break;
                    case R.id.LG:
                        initUI("LG");
                        break;
                    case R.id.xiaomi:
                        initUI("Xiaomi");
                        break;
                    case R.id.pixel:
                        initUI("Google");
                        break;
                    case R.id.sony:
                        initUI("Sony");
                        break;
                    case R.id.one_plaus:
                        initUI("OnePlus");
                        break;

                }
                return true;
            }
        });

        popup.show();
    }

    private void getList(String search) {
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        Call<CategorResponseModel> allUsers = services.showList(search);
        allUsers.enqueue(new Callback<CategorResponseModel>() {
            @Override
            public void onResponse(Call<CategorResponseModel> call, retrofit2.Response<CategorResponseModel> response) {
                if (response.body().getSuccess()) {

                    if (alertDialog != null)
                        alertDialog.dismiss();

                    categoryModelList.addAll(response.body().getData());
                    Collections.reverse(categoryModelList);

                    categoriesAdapter = new CategoriesAdapter(getActivity(), categoryModelList);
                    rvCategories.setAdapter(categoriesAdapter);
                    categoriesAdapter.notifyDataSetChanged();

                } else {
                    if (alertDialog != null)
                        alertDialog.dismiss();
                    Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CategorResponseModel> call, Throwable t) {
                Log.d("fail",t.getMessage());
            }
        });
    }

}
