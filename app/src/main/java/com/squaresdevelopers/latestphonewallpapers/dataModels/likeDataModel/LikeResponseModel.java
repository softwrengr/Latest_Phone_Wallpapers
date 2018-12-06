package com.squaresdevelopers.latestphonewallpapers.dataModels.likeDataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by eapple on 06/12/2018.
 */

public class LikeResponseModel {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("response_code")
    @Expose
    private Integer responseCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private LikeDetailModel data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LikeDetailModel getData() {
        return data;
    }

    public void setData(LikeDetailModel data) {
        this.data = data;
    }
}
