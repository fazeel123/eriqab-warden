package com.example.e_riqabpemantauan.model;

public class RecordDailyLogDetailsDataModel {

    private int id;
    private String activity;
    private String time;

    private String add_time;
    private String dateActivity;
    private String shiftActivity;
    private String timeActivity;
    private boolean checkActivity;
    private String activityName;

    public RecordDailyLogDetailsDataModel(int id, String time, String activity) {
        this.id = id;
        this.activity = activity;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
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
