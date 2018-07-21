package com.eyresapps.data;

/**
 * Created by thomaseyre on 21/07/2018.
 */

public class MonthCounter {

    private String name;
    private Integer count;
    private Integer month;

    public MonthCounter (String name, Integer count, Integer month){
        this.name = name;
        this.count = count;
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
}
