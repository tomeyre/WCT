package eyresapps.com.utils;

import java.util.ArrayList;

import eyresapps.com.data.Crimes;
import eyresapps.com.data.FilterItem;

public class FilterCrimeList {

    public ArrayList<ArrayList<Crimes>> filter(ArrayList<ArrayList<Crimes>> crimeList, ArrayList<FilterItem> filterList){
        ArrayList<ArrayList<Crimes>> filteredCrimes = new ArrayList<>();
        ArrayList<Crimes> innerList;

        for(int i = 0; i < crimeList.size(); i++){
            innerList = new ArrayList<>();
            for(int j = 0; j < crimeList.get(i).size(); j++){
                for(int k = 0; k < filterList.size(); k++){
                    if(crimeList.get(i).get(j).getCrimeType().equalsIgnoreCase(filterList.get(k).getNameString()) && filterList.get(k).getShow()){
                        System.out.println("add crime");
                        System.out.println("filter name string  = " + filterList.get(k).getNameString() + " actual = " + crimeList.get(i).get(j).getCrimeType() + " / " + filterList.get(k).getShow());
                        innerList.add(crimeList.get(i).get(j));
                    }else if(crimeList.get(i).get(j).getCrimeType().equalsIgnoreCase(filterList.get(k).getName().getText().toString()) && !filterList.get(k).getShow()){
                        System.out.println("filter name  = " + filterList.get(k).getNameString() + " actual = " + crimeList.get(i).get(j).getCrimeType() + " / " + filterList.get(k).getShow());
                    }else{
                        System.out.println("add crime");
                        innerList.add(crimeList.get(i).get(j));
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
