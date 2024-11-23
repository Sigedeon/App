package com.example.apps;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserSession(String userId, boolean isLoggedIn) {
        editor.putString("userId", userId);
        editor.putString("nom", userId);
        editor.putString("prenom", userId);
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    public String getUserId() {
        return sharedPreferences.getString("userId", null);
    }

    public String getPrenom() {
        return sharedPreferences.getString("prenom", null);
    }

    public String getNom() {
        return sharedPreferences.getString("nom", null);
    }


    public void logout() {
        editor.clear();
        editor.apply();
    }
}
