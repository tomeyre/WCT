package com.eyresapps.data;

import java.util.ArrayList;

/**
 * Created by thomaseyre on 22/02/2018.
 */

public class CrimeCount {

    private static CrimeCount ourInstance = new CrimeCount();

    public static CrimeCount getInstance() {
        return ourInstance;
    }

    private CrimeCount() {
    }

    private ArrayList<Counter> streetCountList = new ArrayList<>();
    private ArrayList<Counter> totalCountList = new ArrayList<>();



    public void resetStreetCount(){
        streetCountList.clear();
    }

    public void resetTotalsCount(){
        totalCountList.clear();
    }

    public ArrayList<Counter> getStreetCountList() {
        return streetCountList;
    }

    public ArrayList<Counter> getTotalCountList() {
        return totalCountList;
    }

    public void setStreetCountList(ArrayList<Counter> streetCountList) {
        this.streetCountList = streetCountList;
    }

    public void setTotalCountList(ArrayList<Counter> totalCountList) {
        this.totalCountList = totalCountList;
    }
}
