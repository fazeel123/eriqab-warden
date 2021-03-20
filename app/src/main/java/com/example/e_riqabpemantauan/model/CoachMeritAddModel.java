package com.example.e_riqabpemantauan.model;

public class CoachMeritAddModel {

    private int id;
    private String name;
    private int totalMerit;
    private String details;
    private String merit;
    private String type;
    private String date;

    public CoachMeritAddModel() {

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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getMerit() {
        return merit;
    }

    public void setMerit(String merit) {
        this.merit = merit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
