package com.gcml.biz.followup.model.entity;

import com.google.gson.annotations.SerializedName;

public class ResidentListBody {

    /**
     * doctorId : 0
     * existUserType : string
     * followCountType : 0
     * limit : 0
     * page : 0
     * patientName : string
     */

    @SerializedName("doctorId")
    private int doctorId;
    @SerializedName("existUserType")
    private String existUserType;
    @SerializedName("followCountType")
    private int followCountType;
    @SerializedName("limit")
    private int limit;
    @SerializedName("page")
    private int page;
    @SerializedName("patientName")
    private String patientName;

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getExistUserType() {
        return existUserType;
    }

    public void setExistUserType(String existUserType) {
        this.existUserType = existUserType;
    }

    public int getFollowCountType() {
        return followCountType;
    }

    public void setFollowCountType(int followCountType) {
        this.followCountType = followCountType;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
