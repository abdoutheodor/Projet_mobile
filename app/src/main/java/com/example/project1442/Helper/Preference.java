package com.example.project1442.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference{
    private static final String PREF_NAME = "app";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    //private static final String IS_LOGGED_IN = "IsLoggedIn";

    //private static final String USER_ID = "UserID";
    private static final String LANGAGE = "langage";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public Preference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /*public void login(int userID) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putInt(USER_ID, userID);
        editor.apply();
    }

    public void logout() {
        editor.putBoolean(IS_LOGGED_IN, false);
        editor.putInt(USER_ID, -1);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public int getUserID() {
        return sharedPreferences.getInt(USER_ID, -1);
    }*/

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setLangage(String langage) {
        editor.putString(LANGAGE, langage);
        editor.apply();
    }

    public String getLangage() {
        return sharedPreferences.getString(LANGAGE, "fr");
    }
}
