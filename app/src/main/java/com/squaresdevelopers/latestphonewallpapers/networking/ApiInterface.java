package com.squaresdevelopers.latestphonewallpapers.networking;

import com.squaresdevelopers.latestphonewallpapers.dataModels.likeDataModel.LikeResponseModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.wallpaperDataModel.ItemReponseModel;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by eapple on 29/08/2018.
 */

public interface ApiInterface {

    @GET("categories_details/{version}/")
    Call<ItemReponseModel> categories(@Path("version") String version);

    @FormUrlEncoded
    @POST("like")
    Call<LikeResponseModel> like(  @Field("image_url") String photo,
                                 @Part("uuid") String uuid);

}
