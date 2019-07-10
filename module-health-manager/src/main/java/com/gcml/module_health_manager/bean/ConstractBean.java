package com.gcml.module_health_manager.bean;

import com.gzq.lib_resource.bean.ResidentBean;

import java.io.Serializable;
import java.util.List;

public class ConstractBean implements Serializable {


    private int count;
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
