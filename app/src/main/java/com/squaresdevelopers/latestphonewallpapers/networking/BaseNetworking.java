package com.squaresdevelopers.latestphonewallpapers.networking;

import android.util.Log;
import android.widget.Toast;

import com.squaresdevelopers.latestphonewallpapers.controllers.CategoriesAdapter;
import com.squaresdevelopers.latestphonewallpapers.dataModels.categoryListDataModel.CategorResponseModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.categoryListDataModel.CategoryDetailModel;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;

public class BaseNetworking {
    public static ArrayList<CategoryDetailModel> categoryModelList;

    public static void getList(String search) {
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        Call<CategorResponseModel> allUsers = services.showList(search);
        allUsers.enqueue(new Callback<CategorResponseModel>() {
            @Override
            public void onResponse(Call<CategorResponseModel> call, retrofit2.Response<CategorResponseModel> response) {
                if (response.body().getSuccess()) {
                    categoryModelList = new ArrayList<>();
                    categoryModelList.addAll(response.body().getData());
                    Collections.reverse(categoryModelList);


                } else {
                }

            }

            @Override
            public void onFailure(Call<CategorResponseModel> call, Throwable t) {
                Log.d("fail", t.getMessage());
            }
        });

    }
}
