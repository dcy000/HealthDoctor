package com.gcml.biz.followup.model.entity;

import com.google.gson.annotations.SerializedName;

public class FollowUpUpdateBody {


    /**
     * id : 146
     * followStatus : 已随访
     * categoryId : 2
     * categoryName : 电话
     * resultContent : 2型糖尿病居民实际随访模板
     * resultDate : 2019-07-12 10:10:31
     * resultDoctorId : 10132
     * resultTitle : 2型糖尿病居民随访
     * resultTypeId : 4
     */

    @SerializedName("id")
    private int id;
    @SerializedName("followStatus")
    private String followStatus;
    @SerializedName("categoryId")
    private int categoryId;
    @SerializedName("categoryName")
    private String categoryName;
    @SerializedName("resultContent")
    private String resultContent;
    @SerializedName("resultDate")
    private String resultDate;
    @SerializedName("resultDoctorId")
    private int resultDoctorId;
    @SerializedName("resultTitle")
    private String resultTitle;
    @SerializedName("resultTypeId")
    private int resultTypeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(String followStatus) {
        this.followStatus = followStatus;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getResultContent() {
        return resultContent;
    }

    public void setResultContent(String resultContent) {
        this.resultContent = resultContent;
    }

    public String getResultDate() {
        return resultDate;
    }

    public void setResultDate(String resultDate) {
        this.resultDate = resultDate;
    }

    public int getResultDoctorId() {
        return resultDoctorId;
    }

    public void setResultDoctorId(int resultDoctorId) {
        this.resultDoctorId = resultDoctorId;
    }

    public String getResultTitle() {
        return resultTitle;
    }

    public void setResultTitle(String resultTitle) {
        this.resultTitle = resultTitle;
    }

    public int getResultTypeId() {
        return resultTypeId;
    }

    public void setResultTypeId(int resultTypeId) {
        this.resultTypeId = resultTypeId;
    }
}
