package com.emrood.e_contact.Utils;

import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Noel Emmanuel Roodly on 11/21/2018.
 */
public class Utils {


    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

}
