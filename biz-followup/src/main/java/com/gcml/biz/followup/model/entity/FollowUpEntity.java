package com.gcml.biz.followup.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.gzq.lib_resource.bean.ResidentBean;
import com.gzq.lib_resource.bean.UserEntity;

public class FollowUpEntity implements Parcelable {

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
    @SerializedName("resultTypeId")
    private int resultTypeId;
    @SerializedName("resultTitle")
    private String resultTitle;
    @SerializedName("resultContent")
    private String resultContent;
    @SerializedName("resultDate")
    private String resultDate;
    @SerializedName("resultDoctorId")
    private int resultDoctorId;
    @SerializedName("categoryName")
    private String categoryName;
    @SerializedName("categoryId")
    private int categoryId;
    @SerializedName("resultDoctor")
    private UserEntity resultDocter;
    @SerializedName("followStatus")
    private String followStatus;
    @SerializedName("planDocter")
    private UserEntity planDoctor;
    @SerializedName("createdOn")
    private String createdOn;
    @SerializedName("createDoctorName")
    private String createDoctorName;
    @SerializedName("tuser")
    private ResidentBean resident;

    protected FollowUpEntity(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        planDate = in.readString();
        planDoctorId = in.readInt();
        planTypeId = in.readInt();
        planTitle = in.readString();
        planContent = in.readString();
        resultTypeId = in.readInt();
        resultTitle = in.readString();
        resultContent = in.readString();
        resultDate = in.readString();
        resultDoctorId = in.readInt();
        categoryName = in.readString();
        categoryId = in.readInt();
        resultDocter = in.readParcelable(UserEntity.class.getClassLoader());
        followStatus = in.readString();
        planDoctor = in.readParcelable(UserEntity.class.getClassLoader());
        createdOn = in.readString();
        createDoctorName = in.readString();
        resident = in.readParcelable(ResidentBean.class.getClassLoader());
    }

    public static final Creator<FollowUpEntity> CREATOR = new Creator<FollowUpEntity>() {
        @Override
        public FollowUpEntity createFromParcel(Parcel in) {
            return new FollowUpEntity(in);
        }

        @Override
        public FollowUpEntity[] newArray(int size) {
            return new FollowUpEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeString(planDate);
        dest.writeInt(planDoctorId);
        dest.writeInt(planTypeId);
        dest.writeString(planTitle);
        dest.writeString(planContent);
        dest.writeInt(resultTypeId);
        dest.writeString(resultTitle);
        dest.writeString(resultContent);
        dest.writeString(resultDate);
        dest.writeInt(resultDoctorId);
        dest.writeString(categoryName);
        dest.writeInt(categoryId);
        dest.writeParcelable(resultDocter, flags);
        dest.writeString(followStatus);
        dest.writeParcelable(planDoctor, flags);
        dest.writeString(createdOn);
        dest.writeString(createDoctorName);
        dest.writeParcelable(resident, flags);
    }

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

    public int getResultTypeId() {
        return resultTypeId;
    }

    public void setResultTypeId(int resultTypeId) {
        this.resultTypeId = resultTypeId;
    }

    public String getResultTitle() {
        return resultTitle;
    }

    public void setResultTitle(String resultTitle) {
        this.resultTitle = resultTitle;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public UserEntity getResultDocter() {
        return resultDocter;
    }

    public void setResultDocter(UserEntity resultDocter) {
        this.resultDocter = resultDocter;
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

    public String getCreateDoctorName() {
        return createDoctorName;
    }

    public void setCreateDoctorName(String createDoctorName) {
        this.createDoctorName = createDoctorName;
    }

    public ResidentBean getResident() {
        return resident;
    }

    public void setResident(ResidentBean resident) {
        this.resident = resident;
    }
}
