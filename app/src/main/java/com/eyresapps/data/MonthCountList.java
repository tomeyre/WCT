package com.eyresapps.data;

import java.util.ArrayList;

/**
 * Created by thomaseyre on 21/07/2018.
 */

public class MonthCountList {

    ArrayList<ArrayList<MonthCounter>> list;

    public MonthCountList() {
        list = new ArrayList<>();
        list.add(new ArrayList<MonthCounter>());
        list.add(new ArrayList<MonthCounter>());
        list.add(new ArrayList<MonthCounter>());
        list.add(new ArrayList<MonthCounter>());
        list.add(new ArrayList<MonthCounter>());
        list.add(new ArrayList<MonthCounter>());
        list.add(new ArrayList<MonthCounter>());
        list.add(new ArrayList<MonthCounter>());
        list.add(new ArrayList<MonthCounter>());
        list.add(new ArrayList<MonthCounter>());
        list.add(new ArrayList<MonthCounter>());
        list.add(new ArrayList<MonthCounter>());
    }

    public void updateMonth(ArrayList<MonthCounter> monthCount, int month){
        list.set(month, monthCount);
    }

    public ArrayList<MonthCounter> getMonth(int month){
        return list.get(month);
    }
}
