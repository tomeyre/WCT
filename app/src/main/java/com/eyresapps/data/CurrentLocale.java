package com.eyresapps.data;

import java.util.Locale;

/**
 * Created by thomaseyre on 14/07/2018.
 */

public class CurrentLocale {
    private static final CurrentLocale ourInstance = new CurrentLocale();
    private Locale locale;

    public static CurrentLocale getInstance() {
        return ourInstance;
    }

    private CurrentLocale() {
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
