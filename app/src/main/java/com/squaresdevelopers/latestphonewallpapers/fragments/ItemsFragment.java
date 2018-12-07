package com.squaresdevelopers.latestphonewallpapers.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squaresdevelopers.latestphonewallpapers.R;
import com.squaresdevelopers.latestphonewallpapers.controllers.CategoriesAdapter;
import com.squaresdevelopers.latestphonewallpapers.controllers.ItemAdapter;
import com.squaresdevelopers.latestphonewallpapers.dataModels.WallpaperItems;
import com.squaresdevelopers.latestphonewallpapers.dataModels.wallpaperDataModel.ItemDetailModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.wallpaperDataModel.ItemReponseModel;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiClient;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiInterface;
import com.squaresdevelopers.latestphonewallpapers.utils.AlertUtils;
import com.squaresdevelopers.latestphonewallpapers.utils.GeneralUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;


public class ItemsFragment extends Fragment {
    AlertDialog alertDialog;
    View view;
    @BindView(R.id.gv_categories_items)
    GridView gvCategoriesItems;
    ArrayList<ItemDetailModel> itemReponseModelArrayList;
    ItemAdapter categoriesAdapter;
    String strID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_items, container, false);
        customActionBar();
        initUI();
        onback(view);
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this,view);
        strID = GeneralUtils.getID(getActivity());
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        itemReponseModelArrayList = new ArrayList<>();
        categoriesAdapter = new ItemAdapter(getActivity(), itemReponseModelArrayList);
        gvCategoriesItems.setAdapter(categoriesAdapter);
        apiCall();

    }

    private void apiCall(){
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ItemReponseModel> categoriesResponseModelCall = services.categories(strID);
        categoriesResponseModelCall.enqueue(new Callback<ItemReponseModel>() {
            @Override
            public void onResponse(Call<ItemReponseModel> call, retrofit2.Response<ItemReponseModel> response) {

                alertDialog.dismiss();
                itemReponseModelArrayList.addAll(response.body().getData());
                categoriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ItemReponseModel> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
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
        tvTitle.setText("WallPapers");
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
