package com.squaresdevelopers.latestphonewallpapers.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.squaresdevelopers.latestphonewallpapers.R;
import com.squaresdevelopers.latestphonewallpapers.dataModels.categoryListDataModel.CategorResponseModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.categoryListDataModel.CategoryDetailModel;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiClient;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiInterface;
import com.squaresdevelopers.latestphonewallpapers.networking.NetworkingCall;
import com.squaresdevelopers.latestphonewallpapers.controllers.CategoriesAdapter;
import com.squaresdevelopers.latestphonewallpapers.dataModels.CategoryModel;
import com.squaresdevelopers.latestphonewallpapers.utils.AlertUtils;
import com.squaresdevelopers.latestphonewallpapers.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Collections;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;


public class HomeFragment extends Fragment {
    View view;
    android.support.v7.app.AlertDialog alertDialog;
    @BindView(R.id.et_search_phone)
    EditText etSearch;
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

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);


        return view;
    }

    public void initUI(String s) {
        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvCategories.setLayoutManager(layoutManager);
        categoryModelList = new ArrayList<>();
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        categoriesAdapter = new CategoriesAdapter(getActivity(), categoryModelList);
        rvCategories.setAdapter(categoriesAdapter);
        getList(s);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                categoriesAdapter.getFilter().filter( etSearch.getText().toString());
            }
        });

    }

    //cutomAction Bar
    public void customActionBar() {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#002080")));
        mActionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#002699")));
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView tvTitle = mCustomView.findViewById(R.id.title);
        tvTitle.setText(getResources().getString(R.string.app_name));
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.show();

    }


    private void showDropDownMenu(ImageView filter) {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(getActivity(), filter);
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
                    categoriesAdapter.notifyDataSetChanged();

                } else {
                    if (alertDialog != null)
                        alertDialog.dismiss();
                    Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CategorResponseModel> call, Throwable t) {
                Log.d("fail", t.getMessage());
            }
        });

    }
}
