package com.gcml.module_health_record.bean;

import java.io.Serializable;

public class DetectionData implements Serializable {
    private Float bloodOxygen;
    private Float bloodSugar;
    private Float cholesterol;
    private String createdBy;
    private String createdOn;
    private String deletionState;
    private String description;
    private String detectionType;
    private String ecg;
    //旧版字段
    private String eqid;
    //合版字段
    private String equipmentId;
    private Integer heartRate;
    private Integer highPressure;
    private Integer lowPressure;
    private Integer id;
    private String modifiedBy;
    private String modifiedOn;
    private Integer offset;
    //合版字段
    private Integer patientId;
    private Integer pulse;
    private Integer state;
    private Integer seq;
    private Integer sugarTime;
    private Float temperAture;
    //旧版字段
    private String time;
    private String dataTime;
    private Float uricAcid;
    private Integer userid;
    private Float weight;
    private Float height;
    private Boolean weightOver;
    private String yz;
    private Integer zid;
    private String result;
    private String resultUrl;
    private String breathHome;
    private byte[] ecgData;
    private String ecgDataString;
    private String ecgTips;
    private Integer ecgFlag;
    private Boolean isInit;

    public DetectionData() {

    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getDeletionState() {
        return deletionState;
    }

    public void setDeletionState(String deletionState) {
        this.deletionState = deletionState;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Float getBloodOxygen() {
        return bloodOxygen;
    }

    public void setBloodOxygen(Float bloodOxygen) {
        this.bloodOxygen = bloodOxygen;
    }

    public Float getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(Float bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public Float getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Float cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getDetectionType() {
        return detectionType;
    }

    public void setDetectionType(String detectionType) {
        this.detectionType = detectionType;
    }

    public String getEcg() {
        return ecg;
    }

    public void setEcg(String ecg) {
        this.ecg = ecg;
    }

    public String getEqid() {
        return eqid;
    }

    public void setEqid(String eqid) {
        this.eqid = eqid;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public Integer getHighPressure() {
        return highPressure;
    }

    public void setHighPressure(Integer highPressure) {
        this.highPressure = highPressure;
    }

    public Integer getLowPressure() {
        return lowPressure;
    }

    public void setLowPressure(Integer lowPressure) {
        this.lowPressure = lowPressure;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPulse() {
        return pulse;
    }

    public void setPulse(Integer pulse) {
        this.pulse = pulse;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getSugarTime() {
        return sugarTime;
    }

    public void setSugarTime(Integer sugarTime) {
        this.sugarTime = sugarTime;
    }

    public Float getTemperAture() {
        return temperAture;
    }

    public void setTemperAture(Float temperAture) {
        this.temperAture = temperAture;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Float getUricAcid() {
        return uricAcid;
    }

    public void setUricAcid(Float uricAcid) {
        this.uricAcid = uricAcid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Boolean isWeightOver() {
        return weightOver;
    }

    public void setWeightOver(boolean weightOver) {
        this.weightOver = weightOver;
    }

    public String getYz() {
        return yz;
    }

    public void setYz(String yz) {
        this.yz = yz;
    }

    public Integer getZid() {
        return zid;
    }

    public void setZid(Integer zid) {
        this.zid = zid;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }

    public String getBreathHome() {
        return breathHome;
    }

    public void setBreathHome(String breathHome) {
        this.breathHome = breathHome;
    }

    public byte[] getEcgData() {
        return ecgData;
    }

    public void setEcgData(byte[] ecgData) {
        this.ecgData = ecgData;
    }

    public String getEcgDataString() {
        return ecgDataString;
    }

    public void setEcgDataString(String ecgDataString) {
        this.ecgDataString = ecgDataString;
    }

    public String getEcgTips() {
        return ecgTips;
    }

    public void setEcgTips(String ecgTips) {
        this.ecgTips = ecgTips;
    }

    public int getEcgFlag() {
        return ecgFlag;
    }

    public void setEcgFlag(int ecgFlag) {
        this.ecgFlag = ecgFlag;
    }

    public Boolean isInit() {
        return isInit;
    }

    public void setInit(Boolean init) {
        isInit = init;
    }
}
