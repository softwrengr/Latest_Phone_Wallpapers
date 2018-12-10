package com.squaresdevelopers.latestphonewallpapers.dataModels.showLikeDataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by eapple on 10/12/2018.
 */

public class ShowLikeResponseModel {
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
    private List<ShowDetailModel> data = null;

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

    public List<ShowDetailModel> getData() {
        return data;
    }

    public void setData(List<ShowDetailModel> data) {
        this.data = data;
    }

}
