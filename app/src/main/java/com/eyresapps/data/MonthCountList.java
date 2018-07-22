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

    public void sortByMonth(){
        boolean notAddedNonEmpty = true;
        ArrayList<ArrayList<MonthCounter>> temp = new ArrayList<>();
        for(ArrayList<MonthCounter> monthCountLists : list){
            if(monthCountLists.isEmpty()){
                temp.add(new ArrayList<MonthCounter>());
            }else if(temp.isEmpty() || notAddedNonEmpty){
                temp.add(monthCountLists);
                notAddedNonEmpty = false;
            }else {
                for(int i = 0; i < temp.size(); i++){
                    if(!temp.get(i).isEmpty() && monthCountLists.get(0).getYear() < temp.get(i).get(0).getYear()){
                        temp.add(i,monthCountLists);
                        break;
                    } else if(!temp.get(i).isEmpty() && monthCountLists.get(0).getYear() <= temp.get(i).get(0).getYear() &&
                            monthCountLists.get(0).getMonth() < temp.get(i).get(0).getMonth()){
                        temp.add(i,monthCountLists);
                        break;
                    }else if(i == temp.size() - 1){
                        temp.add(monthCountLists);
                        break;
                    }
                }
            }
        }
        list = temp;
    }
}
