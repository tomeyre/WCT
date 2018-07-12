package com.eyresapps.utils;

import android.widget.TextView;

import java.util.ArrayList;

public class CrimeNumbers {

    ArrayList<TextView> totalTextViews;
    ArrayList<TextView> streetTextViews;

    private static final CrimeNumbers ourInstance = new CrimeNumbers();

    public static CrimeNumbers getInstance() {
        return ourInstance;
    }

    private CrimeNumbers() {
    }

    public void setTotalTextViews(ArrayList<TextView> totalTextViews){
        this.totalTextViews = totalTextViews;
    }

    public void setStreetTextViews(ArrayList<TextView> streetTextViews){
        this.streetTextViews = streetTextViews;
    }

    public ArrayList<TextView> getTextViews(boolean totals) {
        if (totals) {
            return totalTextViews;
        } else {
            return streetTextViews;
        }
    }
}
