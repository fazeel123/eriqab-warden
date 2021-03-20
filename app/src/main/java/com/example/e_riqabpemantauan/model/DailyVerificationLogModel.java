package com.example.e_riqabpemantauan.model;

public class DailyVerificationLogModel {

    private int id;
    private String date;
    private String shift;
    private String wardenOneId;
    private String getWardenTwoId;
    private String hourseRulerId;
    private String executiveId;
    private String comment;

    public DailyVerificationLogModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getWardenOneId() {
        return wardenOneId;
    }

    public void setWardenOneId(String wardenOneId) {
        this.wardenOneId = wardenOneId;
    }

    public String getGetWardenTwoId() {
        return getWardenTwoId;
    }

    public void setGetWardenTwoId(String getWardenTwoId) {
        this.getWardenTwoId = getWardenTwoId;
    }

    public String getHourseRulerId() {
        return hourseRulerId;
    }

    public void setHourseRulerId(String hourseRulerId) {
        this.hourseRulerId = hourseRulerId;
    }

    public String getExecutiveId() {
        return executiveId;
    }

    public void setExecutiveId(String executiveId) {
        this.executiveId = executiveId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
