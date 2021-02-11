package com.iks.jokesapplication.common;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorageController {
    private static final LocalStorageController ourInstance = new LocalStorageController();
    private Context applicationContext;
    private static final int PRIVATE_MODE = 0;
    private static final String PREF_FILE_NAME = "com.iks.attendanceapp";
    private LocalStorageController() {
    }
    public static LocalStorageController getInstance() {
        return ourInstance;
    }
    public Context getApplicationContext() {
        return applicationContext;
    }
    public void updateSharedPreference(String key, String value) {
        SharedPreferences pref = applicationContext.getSharedPreferences(PREF_FILE_NAME, PRIVATE_MODE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key,value);
        editor.apply(); // commit changes
    }
    public void setApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String getSharedPreferenceString(String key) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(PREF_FILE_NAME, PRIVATE_MODE);

//                PreferenceManager.getDefaultSharedPreferences(applicationContext);
        return sharedPreferences.getString(key, "");
    }
    public void putBooleanSharedPreferences(String key,boolean state){
        SharedPreferences pref = applicationContext.getSharedPreferences(PREF_FILE_NAME, PRIVATE_MODE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key,state);
        editor.apply();
    }
    public boolean getBooleanSharedPreferences(String key,Boolean defaultValue ) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(PREF_FILE_NAME, PRIVATE_MODE);
        return sharedPreferences.getBoolean(key,defaultValue);
    }
    /*private void updateSharedPreference(String key, ApplicationState value) {
        SharedPreferences pref = applicationContext.getSharedPreferences(PREF_FILE_NAME, PRIVATE_MODE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key,value.getApplicationState());
        editor.apply(); // commit changes

    }*/
    public void putInt(String key, int value) {
        SharedPreferences pref = applicationContext.getSharedPreferences(PREF_FILE_NAME, PRIVATE_MODE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key,value);
        editor.apply();
    }
    public int getInt(String key) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(PREF_FILE_NAME, PRIVATE_MODE);
        return sharedPreferences.getInt(key,0);
    }
    public void clearAllPreferences(){
        SharedPreferences preferences = applicationContext.getSharedPreferences(PREF_FILE_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
