package com.gzq.lib_resource.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserEntity implements Parcelable {

    private int docterid;
    private String doctername;
    private String tel;
    private String hosname;
    private String adds;
    private String duty;
    private String bill;
    private String department;
    private String documents;
    private String card;
    private int rankid;
    private String state;
    private int priority;
    private double amount;
    private String gat;
    private String pro;
    private int pend;
    private int evaluation;
    @SerializedName("apply_amount")
    private float applyAmount;
    @SerializedName("service_amount")
    private float serviceAmount;
    @SerializedName("docterPhoto")
    private String docterPhoto;
    private int onlineStatus;
    private String wyyxId;
    private String wyyxPwd;
    private String faceId;
    private String faceGroup;

    /**
     * {
     * "evaluation": 30,
     * "doctername": "吴正",
     * "service_amount": 0,
     * "docter_photo": null,
     * "duty": "院长",
     * "bill": "查看账单",
     * "state": "查看详情",
     * "hosname": "309医院",
     * "docterid": 10111,
     * "apply_amount": 0
     * }
     **/

    public UserEntity() {
    }

    protected UserEntity(Parcel in) {
        docterid = in.readInt();
        doctername = in.readString();
        tel = in.readString();
        hosname = in.readString();
        adds = in.readString();
        duty = in.readString();
        bill = in.readString();
        department = in.readString();
        documents = in.readString();
        card = in.readString();
        rankid = in.readInt();
        state = in.readString();
        priority = in.readInt();
        amount = in.readDouble();
        gat = in.readString();
        pro = in.readString();
        pend = in.readInt();
        evaluation = in.readInt();
        applyAmount = in.readInt();
        serviceAmount = in.readInt();
        docterPhoto = in.readString();
        onlineStatus = in.readInt();
        wyyxId = in.readString();
        wyyxPwd = in.readString();
        faceId = in.readString();
        faceGroup = in.readString();
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel in) {
            return new UserEntity(in);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };

    public int getDocterid() {
        return docterid;
    }

    public void setDocterid(int docterid) {
        this.docterid = docterid;
    }

    public String getDoctername() {
        return doctername;
    }

    public void setDoctername(String doctername) {
        this.doctername = doctername;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getHosname() {
        return hosname;
    }

    public void setHosname(String hosname) {
        this.hosname = hosname;
    }

    public String getAdds() {
        return adds;
    }

    public void setAdds(String adds) {
        this.adds = adds;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public int getRankid() {
        return rankid;
    }

    public void setRankid(int rankid) {
        this.rankid = rankid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getGat() {
        return gat;
    }

    public void setGat(String gat) {
        this.gat = gat;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public int getPend() {
        return pend;
    }

    public void setPend(int pend) {
        this.pend = pend;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public float getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(float applyAmount) {
        this.applyAmount = applyAmount;
    }

    public float getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(float serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public String getDocterPhoto() {
        return docterPhoto;
    }

    public void setDocterPhoto(String docterPhoto) {
        this.docterPhoto = docterPhoto;
    }

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
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

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getFaceGroup() {
        return faceGroup;
    }

    public void setFaceGroup(String faceGroup) {
        this.faceGroup = faceGroup;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(docterid);
        dest.writeString(doctername);
        dest.writeString(tel);
        dest.writeString(hosname);
        dest.writeString(adds);
        dest.writeString(duty);
        dest.writeString(bill);
        dest.writeString(department);
        dest.writeString(documents);
        dest.writeString(card);
        dest.writeInt(rankid);
        dest.writeString(state);
        dest.writeInt(priority);
        dest.writeDouble(amount);
        dest.writeString(gat);
        dest.writeString(pro);
        dest.writeInt(pend);
        dest.writeInt(evaluation);
        dest.writeFloat(applyAmount);
        dest.writeFloat(serviceAmount);
        dest.writeString(docterPhoto);
        dest.writeInt(onlineStatus);
        dest.writeString(wyyxId);
        dest.writeString(wyyxPwd);
        dest.writeString(faceId);
        dest.writeString(faceGroup);
    }
}
