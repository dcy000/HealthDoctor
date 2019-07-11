package com.gcml.module_health_manager.bean;

import java.io.Serializable;

public class DetectionBean implements Serializable {

    public String dataAnomalyId;
    public int userId;
    public int doctorId;
    public String userName;
    public String tel;
    public int highPressure;
    public int lowPressure;
    public long detectionTime;
    public String msg;
    public String anomalyStatus;
    public String reviewStatus;
    public String verifyStatus;
    public long verifyModify;
    public int sugarTime;
    public double bloodSugar;
}
