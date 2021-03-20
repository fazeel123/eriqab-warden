package com.example.e_riqabpemantauan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.e_riqabpemantauan.model.UserModel;

public class SharedPrefsManager {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String ACCESS_TOKEN  = "access_token";

    public SharedPrefsManager() {
    }

    public SharedPrefsManager(Context context)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;

    }

    public SharedPreferences getPreferences()
    {
        if(context != null)
            return preferences;
        else
            return null;
    }

    public void signIn(UserModel info) {
        editor = preferences.edit();
        editor.putString(ACCESS_TOKEN, info.getAccess_token());
        editor.apply();
    }

    public String getToken()
    {
        return preferences.getString(ACCESS_TOKEN, null);
    }
}
