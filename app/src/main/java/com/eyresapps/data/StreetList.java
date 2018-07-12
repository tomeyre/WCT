package com.eyresapps.data;

import java.util.ArrayList;

public class StreetList {

    private ArrayList<Crimes> streetList;

    StreetList(ArrayList<Crimes> streetList){
        this.streetList = streetList;
    }

    public ArrayList<Crimes> getStreetList() {
        return streetList;
    }
}
