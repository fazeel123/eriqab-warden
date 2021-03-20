package com.example.e_riqabpemantauan.model;

public class CoachMeritDetailsModel {

    private int id;
    private String name;
    private int totalMerit;
    private int mId;
    private String date;
    private String note;
    private String type;
    private String merit;

    public CoachMeritDetailsModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalMerit() {
        return totalMerit;
    }

    public void setTotalMerit(int totalMerit) {
        this.totalMerit = totalMerit;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMerit() {
        return merit;
    }

    public void setMerit(String merit) {
        this.merit = merit;
    }
}
