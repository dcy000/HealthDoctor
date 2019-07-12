package com.gcml.biz.followup.model.entity;

import com.google.gson.annotations.SerializedName;
import com.gzq.lib_resource.bean.UserEntity;

import java.util.List;

public class DoctorList {
    @SerializedName("count")
    private int count;
    @SerializedName("data")
    private List<UserEntity> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<UserEntity> getData() {
        return data;
    }

    public void setData(List<UserEntity> data) {
        this.data = data;
    }

}
