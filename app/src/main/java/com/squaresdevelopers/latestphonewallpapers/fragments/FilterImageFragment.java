package com.squaresdevelopers.latestphonewallpapers.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FilterImageFragment extends Fragment {
    View view;
    android.support.v7.app.AlertDialog alertDialog;
    private AdView mAdView;
    private AdRequest adRequest;
    @BindView(R.id.rvSearchCategory)
    RecyclerView rvCategories;
    CategoriesAdapter categoriesAdapter;
    ArrayList<CategoryModel> categoryModelList;
    String strCategoryName;

    @BindView(R.id.layout_search)
    FrameLayout layout_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_filter_image, container, false);
        ButterKnife.bind(this, view);
        customActionBar();
        onback(view);

        if(!NetworkUtils.isNetworkConnected(getActivity())){
            Toast.makeText(getActivity(), "you have lost your internet connection", Toast.LENGTH_SHORT).show();
        }

        layout_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getActivity(), layout_search);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                          switch (item.getItemId()){
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
                                  initUI("Google Pixel");
                                  break;
                              case R.id.sony:
                                  initUI("Sony");
                                  break;

                          }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });

        return view;
    }

    private void initUI(String string) {
        strCategoryName = string;
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.CATEGORIES+strCategoryName
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
        tvTitle.setText("Search");
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.show();
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

                    GeneralUtils.connectFragment(getActivity(),new TabsFragment());
                    return true;
                }
                return false;
            }
        });

    }
}
