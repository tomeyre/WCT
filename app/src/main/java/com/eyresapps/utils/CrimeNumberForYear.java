package com.eyresapps.utils;

import android.support.v7.widget.CardView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by thomaseyre on 22/07/2018.
 */

public class CrimeNumberForYear {

    ArrayList<TextView> streetTextViews;

    ArrayList<CardView> streetColors;

    private static final CrimeNumberForYear ourInstance = new CrimeNumberForYear();

    public static CrimeNumberForYear getInstance() {
        return ourInstance;
    }

    private CrimeNumberForYear() {
    }

    public void setStreetTextViews(ArrayList<TextView> streetTextViews){
        this.streetTextViews = streetTextViews;
    }

    public void setStreetColors(ArrayList<CardView> streetColors) {
        this.streetColors = streetColors;
    }

    public ArrayList<TextView> getTextViews() {
        return streetTextViews;
    }

    public ArrayList<CardView> getStreetColors() {
        return streetColors;
    }
}
