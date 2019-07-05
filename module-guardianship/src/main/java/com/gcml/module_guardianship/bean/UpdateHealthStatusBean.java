package com.gcml.module_guardianship.bean;

import java.io.Serializable;

public class UpdateHealthStatusBean implements Serializable {

    /**
     * bid : 130434
     * userType : 尘肺
     */

    private int bid;
    private String userType;

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
