package com.example.meme_dating.ui;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String APP_PREFS = "AppPrefsFile";
    private static final Boolean logged = false;
    private static final String username = "";
    private static final int userID = 0;

    private SharedPreferences sp;
    private static SharedPreferencesManager instance;

    private SharedPreferencesManager(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferencesManager getInstance(Context context){
        if(instance == null)
            instance = new SharedPreferencesManager(context);

        return instance;
    }
    public void setUsername(String username) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", username);
        editor.apply();
    }
    public void setUserID(int id) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("userID", id);
        editor.apply();
    }
    public String getUsername() {
        return sp.getString("username", null);
    }
    public int getUserID() {
        return sp.getInt("userID", 0);
    }
    public void setStatus(Boolean logged) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("logged", logged);
        editor.apply();
    }
    public Boolean getStatus() {
        return sp.getBoolean("logged", false);
    }
    public void logOut(){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", "");
        editor.putInt("userID", 0);
        editor.putBoolean("logged", false);
        editor.apply();
    }
}
