package com.gcml.biz.followup.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class HealthTagEntity implements Parcelable {


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

    public HealthTagEntity() {
    }

    protected HealthTagEntity(Parcel in) {
        id = in.readInt();
        text = in.readString();
        value = in.readString();
        typeId = in.readInt();
    }

    public static final Creator<HealthTagEntity> CREATOR = new Creator<HealthTagEntity>() {
        @Override
        public HealthTagEntity createFromParcel(Parcel in) {
            return new HealthTagEntity(in);
        }

        @Override
        public HealthTagEntity[] newArray(int size) {
            return new HealthTagEntity[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(text);
        dest.writeString(value);
        dest.writeInt(typeId);
    }
}
