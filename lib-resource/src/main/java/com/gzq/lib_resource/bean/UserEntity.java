package com.gzq.lib_resource.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class UserEntity implements Parcelable {

    private int docterid;
    private String doctername;
    private String tel;
    private String adds;
    private String duty;
    private String department;
    private int state;
    private int priority;
    private String documents;
    private String card;
    private double amount;
    private String gat;
    private String pro;
    private int pend;
    private int number;
    private int evaluation;
    private int apply_amount;
    private int service_amount;
    private String docter_photo;
    private RBean r;
    private String hosname;
    private int online_status;

    public UserEntity() {
    }

    protected UserEntity(Parcel in) {
        docterid = in.readInt();
        doctername = in.readString();
        tel = in.readString();
        adds = in.readString();
        duty = in.readString();
        department = in.readString();
        state = in.readInt();
        priority = in.readInt();
        documents = in.readString();
        card = in.readString();
        amount = in.readDouble();
        gat = in.readString();
        pro = in.readString();
        pend = in.readInt();
        number = in.readInt();
        evaluation = in.readInt();
        apply_amount = in.readInt();
        service_amount = in.readInt();
        docter_photo = in.readString();
        hosname = in.readString();
        online_status = in.readInt();
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public int getApply_amount() {
        return apply_amount;
    }

    public void setApply_amount(int apply_amount) {
        this.apply_amount = apply_amount;
    }

    public int getService_amount() {
        return service_amount;
    }

    public void setService_amount(int service_amount) {
        this.service_amount = service_amount;
    }

    public String getDocter_photo() {
        return docter_photo;
    }

    public void setDocter_photo(String docter_photo) {
        this.docter_photo = docter_photo;
    }

    public RBean getR() {
        return r;
    }

    public void setR(RBean r) {
        this.r = r;
    }

    public String getHosname() {
        return hosname;
    }

    public void setHosname(String hosname) {
        this.hosname = hosname;
    }

    public int getOnline_status() {
        return online_status;
    }

    public void setOnline_status(int online_status) {
        this.online_status = online_status;
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
        dest.writeString(adds);
        dest.writeString(duty);
        dest.writeString(department);
        dest.writeInt(state);
        dest.writeInt(priority);
        dest.writeString(documents);
        dest.writeString(card);
        dest.writeDouble(amount);
        dest.writeString(gat);
        dest.writeString(pro);
        dest.writeInt(pend);
        dest.writeInt(number);
        dest.writeInt(evaluation);
        dest.writeInt(apply_amount);
        dest.writeInt(service_amount);
        dest.writeString(docter_photo);
        dest.writeString(hosname);
        dest.writeInt(online_status);
    }

    public static class RBean {
        /**
         * rankid : 10001
         * rankname : 主治医师
         */

        private int rankid;
        private String rankname;

        public int getRankid() {
            return rankid;
        }

        public void setRankid(int rankid) {
            this.rankid = rankid;
        }

        public String getRankname() {
            return rankname;
        }

        public void setRankname(String rankname) {
            this.rankname = rankname;
        }
    }
}
