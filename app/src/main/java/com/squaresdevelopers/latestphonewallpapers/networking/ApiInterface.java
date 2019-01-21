package com.squaresdevelopers.latestphonewallpapers.networking;

import com.squaresdevelopers.latestphonewallpapers.dataModels.categoryListDataModel.CategorResponseModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.likeDataModel.LikeResponseModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.showLikeDataModel.ShowLikeResponseModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.unLikeDataModel.UnLikeModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.wallpaperDataModel.ItemReponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by eapple on 29/08/2018.
 */

public interface ApiInterface {

    @GET("categories/{version}/")
    Call<CategorResponseModel> showList(@Path("version") String search);

    @GET("categories_details/{version}/")
    Call<ItemReponseModel> categories(@Path("version") String version);

    @FormUrlEncoded
    @POST("like")
    Call<LikeResponseModel> like(@Field("image_url") String photo,
                                 @Field("uuid") String uuid,
                                 @Field("model_number") String model_no);

    @GET("like/{version}/")
    Call<ShowLikeResponseModel> showLikeItems(@Path("version") String deviceId);

    @FormUrlEncoded
    @POST("like/{version}/")
    Call<UnLikeModel> unLikeItems(@Path("version") String id,
                                  @Field("_method") String method);

}
