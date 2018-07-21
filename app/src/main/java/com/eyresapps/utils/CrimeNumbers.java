package com.eyresapps.utils;

import android.support.v7.widget.CardView;
import android.widget.TextView;

import java.util.ArrayList;

public class CrimeNumbers {

    ArrayList<TextView> totalTextViews;
    ArrayList<TextView> streetTextViews;

    ArrayList<CardView> streetColors;

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

    public void setStreetColors(ArrayList<CardView> streetColors) {
        this.streetColors = streetColors;
    }

    public ArrayList<TextView> getTextViews(boolean totals) {
        if (totals) {
            return totalTextViews;
        } else {
            return streetTextViews;
        }
    }

    public ArrayList<CardView> getStreetColors() {
        return streetColors;
    }
}
