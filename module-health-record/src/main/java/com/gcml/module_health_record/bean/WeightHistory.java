package com.gcml.module_health_record.bean;

/**
 * Created by gzq on 2018/1/18.
 */

public class WeightHistory extends BaseBean{
    public float temper_ature;


    public WeightHistory(float temper_ature, long time) {
        this.temper_ature = temper_ature;
        this.time = time;
    }
}
