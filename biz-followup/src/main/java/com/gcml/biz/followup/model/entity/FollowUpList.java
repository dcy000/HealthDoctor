package com.gcml.biz.followup.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FollowUpList {

    @SerializedName("count")
    private int count;
    @SerializedName("data")
    private List<FollowUpEntity> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<FollowUpEntity> getData() {
        return data;
    }

    public void setData(List<FollowUpEntity> data) {
        this.data = data;
    }
}
