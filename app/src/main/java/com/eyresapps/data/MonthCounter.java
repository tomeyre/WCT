package com.eyresapps.data;

/**
 * Created by thomaseyre on 21/07/2018.
 */

public class MonthCounter {

    private String name;
    private Integer count;
    private Integer month;
    private Integer year;

    public MonthCounter (String name, Integer count, Integer month, Integer year){
        this.name = name;
        this.count = count;
        this.month = month;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getYear() {
        return year;
    }
}
