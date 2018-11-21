package com.emrood.e_contact.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Noel Emmanuel Roodly on 11/18/2018.
 */
public class PreferenceManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "intro_slider-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_PHONEBOOK_SYNCED = "IsPhoneBookSynced";
    private static final String IS_USER_INFO_SAVED = "IsUserInfoSaved";
    private static final String LANG_SELECTED = "language";

    public PreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setPhoneBookSynced(boolean isPhoneBookSynced) {
        editor.putBoolean(IS_PHONEBOOK_SYNCED, isPhoneBookSynced);
        editor.commit();
    }

    public boolean isPhoneBookSynced() {
        return pref.getBoolean(IS_PHONEBOOK_SYNCED, false);
    }

    public void setUserInfoSaved(boolean isUsersave) {
        editor.putBoolean(IS_USER_INFO_SAVED, isUsersave);
        editor.commit();
    }

    public boolean isUserInfoSaved() {
        return pref.getBoolean(IS_USER_INFO_SAVED, false);
    }

    public void setLangSelected(String lang) {
        editor.putString(LANG_SELECTED, lang);
        editor.commit();
    }

    public String getLangSelected() {
        return pref.getString(LANG_SELECTED, "Français");
    }
}