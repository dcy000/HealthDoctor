package com.gzq.lib_resource.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ResidentBean implements Parcelable {

    private int age;
    private String allergy;
    private int bid;
    private String birthday;
    private String bloodType;
    private String bname;
    private int categoryid;
    private int doid;
    private String drink;
    private String dz;
    private String eatingHabits;
    private String eqid;
    private String exerciseHabits;
    private String fetation;
    private int height;
    private String hypertensionHand;
    private String hypertensionLevel;
    private String hypertensionPrimaryState;
    private String hypertensionTarget;
    private String mh;
    private int serverId;
    private String sex;
    private String sfz;
    private String smoke;
    private int state;
    private String tel;
    @SerializedName(value = "userPhoto", alternate = "user_photo")
    private String userPhoto;
    private String vipState;
    private int waist;
    private String watchBindTime;
    private String watchCode;
    private float weight;
    private String wyyxId;
    private String wyyxPwd;
    private String xfid;
    private String xfuserid;
    private int currentYearCount;
    private String recentResultData;
    private String userType;
    private String blood_type;

    public ResidentBean() {
    }



    protected ResidentBean(Parcel in) {
        age = in.readInt();
        allergy = in.readString();
        bid = in.readInt();
        birthday = in.readString();
        bloodType = in.readString();
        bname = in.readString();
        categoryid = in.readInt();
        doid = in.readInt();
        drink = in.readString();
        dz = in.readString();
        eatingHabits = in.readString();
        eqid = in.readString();
        exerciseHabits = in.readString();
        fetation = in.readString();
        height = in.readInt();
        hypertensionHand = in.readString();
        hypertensionLevel = in.readString();
        hypertensionPrimaryState = in.readString();
        hypertensionTarget = in.readString();
        mh = in.readString();
        serverId = in.readInt();
        sex = in.readString();
        sfz = in.readString();
        smoke = in.readString();
        state = in.readInt();
        tel = in.readString();
        userPhoto = in.readString();
        vipState = in.readString();
        waist = in.readInt();
        watchBindTime = in.readString();
        watchCode = in.readString();
        weight = in.readFloat();
        wyyxId = in.readString();
        wyyxPwd = in.readString();
        xfid = in.readString();
        xfuserid = in.readString();
        currentYearCount = in.readInt();
        recentResultData = in.readString();
        userType = in.readString();
        blood_type = in.readString();
    }

    public static final Creator<ResidentBean> CREATOR = new Creator<ResidentBean>() {
        @Override
        public ResidentBean createFromParcel(Parcel in) {
            return new ResidentBean(in);
        }

        @Override
        public ResidentBean[] newArray(int size) {
            return new ResidentBean[size];
        }
    };

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }
    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public int getDoid() {
        return doid;
    }

    public void setDoid(int doid) {
        this.doid = doid;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public String getDz() {
        return dz;
    }

    public void setDz(String dz) {
        this.dz = dz;
    }

    public String getEatingHabits() {
        return eatingHabits;
    }

    public void setEatingHabits(String eatingHabits) {
        this.eatingHabits = eatingHabits;
    }

    public String getEqid() {
        return eqid;
    }

    public void setEqid(String eqid) {
        this.eqid = eqid;
    }

    public String getExerciseHabits() {
        return exerciseHabits;
    }

    public void setExerciseHabits(String exerciseHabits) {
        this.exerciseHabits = exerciseHabits;
    }

    public String getFetation() {
        return fetation;
    }

    public void setFetation(String fetation) {
        this.fetation = fetation;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getHypertensionHand() {
        return hypertensionHand;
    }

    public void setHypertensionHand(String hypertensionHand) {
        this.hypertensionHand = hypertensionHand;
    }

    public String getHypertensionLevel() {
        return hypertensionLevel;
    }

    public void setHypertensionLevel(String hypertensionLevel) {
        this.hypertensionLevel = hypertensionLevel;
    }

    public String getHypertensionPrimaryState() {
        return hypertensionPrimaryState;
    }

    public void setHypertensionPrimaryState(String hypertensionPrimaryState) {
        this.hypertensionPrimaryState = hypertensionPrimaryState;
    }

    public String getHypertensionTarget() {
        return hypertensionTarget;
    }

    public void setHypertensionTarget(String hypertensionTarget) {
        this.hypertensionTarget = hypertensionTarget;
    }

    public String getMh() {
        return mh;
    }

    public void setMh(String mh) {
        this.mh = mh;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSfz() {
        return sfz;
    }

    public void setSfz(String sfz) {
        this.sfz = sfz;
    }

    public String getSmoke() {
        return smoke;
    }

    public void setSmoke(String smoke) {
        this.smoke = smoke;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getVipState() {
        return vipState;
    }

    public void setVipState(String vipState) {
        this.vipState = vipState;
    }

    public int getWaist() {
        return waist;
    }

    public void setWaist(int waist) {
        this.waist = waist;
    }

    public String getWatchBindTime() {
        return watchBindTime;
    }

    public void setWatchBindTime(String watchBindTime) {
        this.watchBindTime = watchBindTime;
    }

    public String getWatchCode() {
        return watchCode;
    }

    public void setWatchCode(String watchCode) {
        this.watchCode = watchCode;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getWyyxId() {
        return wyyxId;
    }

    public void setWyyxId(String wyyxId) {
        this.wyyxId = wyyxId;
    }

    public String getWyyxPwd() {
        return wyyxPwd;
    }

    public void setWyyxPwd(String wyyxPwd) {
        this.wyyxPwd = wyyxPwd;
    }

    public String getXfid() {
        return xfid;
    }

    public void setXfid(String xfid) {
        this.xfid = xfid;
    }

    public String getXfuserid() {
        return xfuserid;
    }

    public void setXfuserid(String xfuserid) {
        this.xfuserid = xfuserid;
    }

    public int getCurrentYearCount() {
        return currentYearCount;
    }

    public void setCurrentYearCount(int currentYearCount) {
        this.currentYearCount = currentYearCount;
    }

    public String getRecentResultData() {
        return recentResultData;
    }

    public void setRecentResultData(String recentResultData) {
        this.recentResultData = recentResultData;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(age);
        dest.writeString(allergy);
        dest.writeInt(bid);
        dest.writeString(birthday);
        dest.writeString(bloodType);
        dest.writeString(bname);
        dest.writeInt(categoryid);
        dest.writeInt(doid);
        dest.writeString(drink);
        dest.writeString(dz);
        dest.writeString(eatingHabits);
        dest.writeString(eqid);
        dest.writeString(exerciseHabits);
        dest.writeString(fetation);
        dest.writeInt(height);
        dest.writeString(hypertensionHand);
        dest.writeString(hypertensionLevel);
        dest.writeString(hypertensionPrimaryState);
        dest.writeString(hypertensionTarget);
        dest.writeString(mh);
        dest.writeInt(serverId);
        dest.writeString(sex);
        dest.writeString(sfz);
        dest.writeString(smoke);
        dest.writeInt(state);
        dest.writeString(tel);
        dest.writeString(userPhoto);
        dest.writeString(vipState);
        dest.writeInt(waist);
        dest.writeString(watchBindTime);
        dest.writeString(watchCode);
        dest.writeFloat(weight);
        dest.writeString(wyyxId);
        dest.writeString(wyyxPwd);
        dest.writeString(xfid);
        dest.writeString(xfuserid);
        dest.writeInt(currentYearCount);
        dest.writeString(recentResultData);
        dest.writeString(userType);
        dest.writeString(blood_type);
    }
}
