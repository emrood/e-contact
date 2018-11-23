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

        Locale haitian = new Locale("ht", "HT");

        switch (lang) {
            case "Anglais":
                config.setLocale(Locale.ENGLISH);
                config.locale = Locale.ENGLISH;
                break;
            case "Français":
                config.setLocale(Locale.FRENCH);
                config.locale = Locale.FRENCH;
                break;
            case "Créole":
                config.setLocale(Locale.JAPANESE);
                config.locale = Locale.JAPANESE;
                break;
            default:
                config.setLocale(haitian);
                config.locale = haitian;
                break;
        }

        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}