package com.example.e_riqabpemantauan.model;

public class RecordDailyLogDetailsModel {

    private String gtime;
    private String title;
    private String time;

    private String add_time;
    private String dateActivity;
    private String shiftActivity;
    private String timeActivity;
    private boolean checkActivity;
    private String activityName;

    public RecordDailyLogDetailsModel() {

    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getGtime() {
        return gtime;
    }

    public void setGtime(String gtime) {
        this.gtime = gtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDateActivity() {
        return dateActivity;
    }

    public void setDateActivity(String dateActivity) {
        this.dateActivity = dateActivity;
    }

    public String getShiftActivity() {
        return shiftActivity;
    }

    public void setShiftActivity(String shiftActivity) {
        this.shiftActivity = shiftActivity;
    }

    public String getTimeActivity() {
        return timeActivity;
    }

    public void setTimeActivity(String timeActivity) {
        this.timeActivity = timeActivity;
    }

    public boolean isCheckActivity() {
        return checkActivity;
    }

    public void setActivity(boolean activity) {
        this.checkActivity = activity;
    }

    public void setCheckActivity(boolean checkActivity) {
        this.checkActivity = checkActivity;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
