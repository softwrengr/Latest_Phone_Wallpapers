package com.squaresdevelopers.latestphonewallpapers.networking;

import com.squaresdevelopers.latestphonewallpapers.dataModels.wallpaperDataModel.ItemReponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by eapple on 29/08/2018.
 */

public interface ApiInterface {

    @GET("categories_details/{version}/")
    Call<ItemReponseModel> categories(@Path("version") String version);

}
