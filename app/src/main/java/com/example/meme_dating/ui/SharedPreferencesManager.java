package com.example.meme_dating.ui;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String APP_PREFS = "AppPrefsFile";
    private static final Boolean logged = false;
    private static final String username = "";

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

    public String getUsername() {
        String usernameValue = sp.getString("username", null);
        return usernameValue;
    }

    public void setStatus(Boolean logged) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("logged", logged);
        editor.apply();
    }

    public Boolean getStatus() {
        Boolean loggedValue = sp.getBoolean("logged", false);
        return loggedValue;
    }
}
