package com.example.themusicshorizons.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.themusicshorizons.activities.LoginActivity;

import java.util.HashMap;

public class SessionManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    private static final String PREF_NAME = "MusicHorizonsUserPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USERNAME = "username";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(long userId, String username) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putLong(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_USER_ID, String.valueOf(pref.getLong(KEY_USER_ID, 0)));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public long getUserId() {
        return pref.getLong(KEY_USER_ID, 0); // Devuelve 0 si no hay usuario
    }
}
