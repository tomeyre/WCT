package com.eyresapps.utils;

import android.content.Context;

import com.eyresapps.crimetracker.R;

import java.util.ArrayList;

/**
 * Created by thomaseyre on 20/07/2018.
 */

public class Colors {
    private static final Colors ourInstance = new Colors();

    public static Colors getInstance() {
        return ourInstance;
    }

    ArrayList<Integer> colors = new ArrayList<Integer>();

    private Colors() {
    }

    public ArrayList<Integer> getColors(Context context) {
        colors.add(context.getResources().getColor(R.color.one));
        colors.add(context.getResources().getColor(R.color.two));
        colors.add(context.getResources().getColor(R.color.three));
        colors.add(context.getResources().getColor(R.color.four));
        colors.add(context.getResources().getColor(R.color.five));
        colors.add(context.getResources().getColor(R.color.six));
        colors.add(context.getResources().getColor(R.color.seven));
        colors.add(context.getResources().getColor(R.color.eight));
        colors.add(context.getResources().getColor(R.color.nine));
        colors.add(context.getResources().getColor(R.color.ten));
        colors.add(context.getResources().getColor(R.color.eleven));
        colors.add(context.getResources().getColor(R.color.twelve));
        colors.add(context.getResources().getColor(R.color.thirteen));
        colors.add(context.getResources().getColor(R.color.fourteen));
        colors.add(context.getResources().getColor(R.color.fifteen));
        colors.add(context.getResources().getColor(R.color.sixteen));
        colors.add(context.getResources().getColor(R.color.seventeen));
        colors.add(context.getResources().getColor(R.color.eighteen));
        colors.add(context.getResources().getColor(R.color.nineteen));
        colors.add(context.getResources().getColor(R.color.twenty));
        return colors;
    }
}
