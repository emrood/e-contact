package com.emrood.e_contact.Utils;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Locale;
/**
 * Created by Noel Emmanuel Roodly on 11/18/2018.
 */
public class LanguageHelper {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void changeLocal(Resources res, String lang) {

        Configuration config;
        config = new Configuration(res.getConfiguration());

        switch (lang) {
            case "en":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    config.setLocale(Locale.ENGLISH);
                    config.locale = Locale.ENGLISH;
                }else{
                    config.locale = Locale.ENGLISH;
                }
                break;
            case "fr":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    config.setLocale(Locale.FRENCH);
                    config.locale = Locale.FRENCH;


                }else{
                    config.locale = Locale.FRENCH;
                }
                break;
            case "HT":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    config.setLocale(Locale.JAPANESE);
                    config.locale = Locale.JAPANESE;
                }else{
                    config.locale = Locale.JAPANESE;
                }
                break;
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    config.setLocale(Locale.ENGLISH);
                    config.locale = Locale.ENGLISH;
                }else{
                    config.locale = Locale.ENGLISH;
                }
                break;
        }

        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}