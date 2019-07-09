package com.gcml.biz.followup.model.entity;

import com.google.gson.annotations.SerializedName;

public class HealthTagEntity {


    /**
     * id : 60
     * text : 糖尿病
     * value : 糖尿病
     * typeId : 16
     */

    @SerializedName("id")
    private int id;
    @SerializedName("text")
    private String text;
    @SerializedName("value")
    private String value;
    @SerializedName("typeId")
    private int typeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
