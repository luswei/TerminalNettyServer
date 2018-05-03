package com.dmdh.YunMenJinSuoWeb.model;

public class RecordMessage {

    private String userNumber;// 3byte
    private String cardID;//12byte
    private String name;//10byte
    private String type;//1byte
    private String time;//6byte
    private String timesCard;//2byte
    private String remark;//8byte

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimesCard() {
        return timesCard;
    }

    public void setTimesCard(String timesCard) {
        this.timesCard = timesCard;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "RecordMessage{" +
                "userNumber='" + userNumber + '\'' +
                ", cardID='" + cardID + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", time='" + time + '\'' +
                ", timesCard='" + timesCard + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
