package com.gcml.biz.followup.model.entity;

import com.google.gson.annotations.SerializedName;
import com.gzq.lib_resource.bean.ResidentBean;
import com.gzq.lib_resource.bean.UserEntity;

public class FollowUpEntity {
    @SerializedName("id")
    private int id;
    @SerializedName("userId")
    private int userId;
    @SerializedName("planDate")
    private String planDate;
    @SerializedName("planDoctorId")
    private int planDoctorId;
    @SerializedName("planTypeId")
    private int planTypeId;
    @SerializedName("planTitle")
    private String planTitle;
    @SerializedName("planContent")
    private String planContent;
    @SerializedName("resultContent")
    private String resultContent;
    @SerializedName("followStatus")
    private String followStatus;
    @SerializedName("planDocter")
    private UserEntity planDoctor;
    @SerializedName("createdOn")
    private String createdOn;
    @SerializedName("tuser")
    private ResidentBean resident;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getPlanTypeId() {
        return planTypeId;
    }

    public void setPlanTypeId(int planTypeId) {
        this.planTypeId = planTypeId;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public String getPlanContent() {
        return planContent;
    }

    public void setPlanContent(String planContent) {
        this.planContent = planContent;
    }

    public String getResultContent() {
        return resultContent;
    }

    public void setResultContent(String resultContent) {
        this.resultContent = resultContent;
    }

    public String getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(String followStatus) {
        this.followStatus = followStatus;
    }

    public UserEntity getPlanDoctor() {
        return planDoctor;
    }

    public void setPlanDoctor(UserEntity planDoctor) {
        this.planDoctor = planDoctor;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public ResidentBean getResident() {
        return resident;
    }

    public void setResident(ResidentBean resident) {
        this.resident = resident;
    }

}
