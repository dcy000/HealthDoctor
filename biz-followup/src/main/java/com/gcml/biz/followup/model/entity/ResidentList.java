package com.gcml.biz.followup.model.entity;

import com.google.gson.annotations.SerializedName;
import com.gzq.lib_resource.bean.ResidentBean;

import java.util.List;

public class ResidentList {
    @SerializedName("count")
    private int count;
    @SerializedName("data")
    private List<ResidentBean> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ResidentBean> getData() {
        return data;
    }

    public void setData(List<ResidentBean> data) {
        this.data = data;
    }

}
