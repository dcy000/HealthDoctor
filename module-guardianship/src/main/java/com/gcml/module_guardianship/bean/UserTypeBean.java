package com.gcml.module_guardianship.bean;

public class UserTypeBean {

    /**
     * id : 60
     * text : 糖尿病
     * value : 糖尿病
     * typeId : 16
     */

    private int id;
    private String text;
    private String value;
    private int typeId;
    public boolean select=false;

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
