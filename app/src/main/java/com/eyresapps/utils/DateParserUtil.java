package com.eyresapps.utils;

import com.eyresapps.data.CurrentLocale;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by thomaseyre on 15/03/2018.
 */

public class DateParserUtil {

    CurrentAddressUtil currentAddress = CurrentAddressUtil.getInstance();
    Locale locale = CurrentLocale.getInstance().getLocale();

    public SimpleDateFormat getParser(){
        if(currentAddress.getAddress().toLowerCase().contains("uk")) {
            return new SimpleDateFormat("yyyy-MM", locale);
        }
        if(currentAddress.getAddress().toLowerCase().contains("chicago")) {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", locale);
        }
        if(currentAddress.getAddress().toLowerCase().contains("los angeles")) {
            return new SimpleDateFormat("yyyy-MM-dd", locale);
        }
        if(currentAddress.getAddress().toLowerCase().contains("durham, nc")) {
            return new SimpleDateFormat("yyyy-MM-dd", locale);
        }
        if(currentAddress.getAddress().toLowerCase().contains("new orleans")) {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", locale);
        }
        return new SimpleDateFormat("yyyy-MM-dd", locale);
    }

    public DateFormat getDateFormat(){
        int style = DateFormat.LONG;
        return DateFormat.getDateInstance(style, locale);
    }

    public DateFormat getDateTimeFormat(){
        int style = DateFormat.LONG;
        return DateFormat.getDateTimeInstance(style, DateFormat.DEFAULT, locale);
    }

}
