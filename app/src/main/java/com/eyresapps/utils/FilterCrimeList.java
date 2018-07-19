package com.eyresapps.utils;

import com.eyresapps.data.Crimes;
import com.eyresapps.data.FilterItem;

import java.util.ArrayList;

public class FilterCrimeList {

    public ArrayList<ArrayList<Crimes>> filter(ArrayList<ArrayList<Crimes>> crimeList, ArrayList<FilterItem> filterList){
        ArrayList<ArrayList<Crimes>> filteredCrimes = new ArrayList<>();
        ArrayList<Crimes> innerList;

        for(int i = 0; i < crimeList.size(); i++){
            innerList = new ArrayList<>();
            for(int j = 0; j < crimeList.get(i).size(); j++){
                for(int k = 0; k < filterList.size(); k++){
                    if((crimeList.get(i).get(j).getCrimeType().equalsIgnoreCase(filterList.get(k).getNameString()) ||
                            crimeList.get(i).get(j).getCrimeType().equalsIgnoreCase(filterList.get(k).getName().getText().toString())) &&
                            filterList.get(k).getShow()){
                        System.out.println("dont filter  = " + filterList.get(k).getNameString() + " actual = " + crimeList.get(i).get(j).getCrimeType() + " / " + filterList.get(k).getShow());
                        innerList.add(crimeList.get(i).get(j));
                        break;
                    }else  if((crimeList.get(i).get(j).getCrimeType().equalsIgnoreCase(filterList.get(k).getNameString()) ||
                            crimeList.get(i).get(j).getCrimeType().equalsIgnoreCase(filterList.get(k).getName().getText().toString())) &&
                            !filterList.get(k).getShow()){
                        System.out.println("do filter  = " + filterList.get(k).getNameString() + " actual = " + crimeList.get(i).get(j).getCrimeType() + " / " + filterList.get(k).getShow());
                        break;
                    }
                }
            }
            if(!innerList.isEmpty()) {
                filteredCrimes.add(innerList);
            }
        }
        return filteredCrimes;
    }
}
