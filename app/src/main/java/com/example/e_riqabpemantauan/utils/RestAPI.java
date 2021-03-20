package com.example.e_riqabpemantauan.utils;

public class RestAPI {

//    private static final String URL = "http://api-eriqab.cocalms.com/api/v1";
    private static final String URL = "https://eriqab-staging.mais.gov.my/api/v1";

    public static final String LOGIN = URL + "/login";
    public static final String GetWardenName = URL + "/wardenName";
    public static final String PostWardenSpecialCase = URL + "/kesKhas";
    public static final String DailyVerificationLog = URL + "/verificationList";
    public static final String DailyVerificationLogActiviyList = URL + "/showVerificationDetail/";
    public static final String DailyVerificationLogApprove = URL+ "/approveLog/";
    public static final String DailyVerificationLogDisapprove = URL + "/disapproveLog/";
    public static final String GetDailyLogDetails = URL + "/dailyLog";
    public static final String DailyDetailsId = URL + "/dailyDetail/";
    public static final String UpdateDailyDetails = URL + "/updateDailyDetail/";
    public static final String ViewCoachDetails = URL + "/clientMerit";
    public static final String ViewCoachDetailsId = URL + "/showMerit/";
    public static final String AddMeritDetails = URL + "/addMerit/";
    public static final String CreateActivity = URL + "/createActivity";
    public static final String CaseList = URL + "/kesKhasList";
    public static final String PreListActivities = URL + "/show_activity";
}
