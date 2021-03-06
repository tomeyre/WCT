package com.eyresapps.utils;

import java.util.ArrayList;

import com.eyresapps.data.FilterItem;

public class FilterList {

    private ArrayList<FilterItem> filterList;

    private static final FilterList ourInstance = new FilterList();

    public static FilterList getInstance() {
        return ourInstance;
    }

    private FilterList() {
    }

    public ArrayList<FilterItem> getFilterList() {
        return filterList;
    }

    public void setFilterList(ArrayList<FilterItem> filterList) {
        this.filterList = filterList;
    }
}
