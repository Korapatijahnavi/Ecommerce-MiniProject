package com.example.ecommerceminiproject;
import android.content.Context;
import android.content.SharedPreferences;
public class SessionManager {
    private final SharedPreferences sharedPreferences;
    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(
                Constants.PREFS_NAME,
                Context.MODE_PRIVATE);
    }
    public void saveLogin(String phone) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(
                Constants.KEY_IS_LOGGED_IN,
                true);

        editor.putString(
                Constants.KEY_MOBILE,
                phone);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(
                Constants.KEY_IS_LOGGED_IN, false);
    }
    public String getPhone() {
        return sharedPreferences.getString(
                Constants.KEY_MOBILE,
                "");
    }
    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}