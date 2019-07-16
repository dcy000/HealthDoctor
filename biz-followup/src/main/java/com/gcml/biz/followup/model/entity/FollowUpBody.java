package com.gcml.biz.followup.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FollowUpBody {


    /**
     * planContent : 仅供测试
     * planDate : 2019-07-11 14:24:00
     * planDoctorId : 10130
     * planTitle : 测试创建模板
     * planTypeId : 3  模版id
     * userId : 130244
     * createDoctorId : 130244
     * createDoctorName :
     */

    @SerializedName("planContent")
    private String planContent;
    @SerializedName("planDate")
    private String planDate;
    @SerializedName("planDoctorId")
    private int planDoctorId;
    @SerializedName("id")
    private int id;
    @SerializedName("planTitle")
    private String planTitle;
    @SerializedName("planTypeId")
    private int planTypeId;
    @SerializedName("userId")
    private int userId;
    @SerializedName("createDoctorId")
    private int createDoctorId;
    @SerializedName("createDoctorName")
    private String createDoctorName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlanContent() {
        return planContent;
    }

    public void setPlanContent(String planContent) {
        this.planContent = planContent;
    }

    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }

    public int getPlanDoctorId() {
        return planDoctorId;
    }

    public void setPlanDoctorId(int planDoctorId) {
        this.planDoctorId = planDoctorId;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public int getPlanTypeId() {
        return planTypeId;
    }

    public void setPlanTypeId(int planTypeId) {
        this.planTypeId = planTypeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCreateDoctorId() {
        return createDoctorId;
    }

    public void setCreateDoctorId(int createDoctorId) {
        this.createDoctorId = createDoctorId;
    }

    public String getCreateDoctorName() {
        return createDoctorName;
    }

    public void setCreateDoctorName(String createDoctorName) {
        this.createDoctorName = createDoctorName;
    }
}
