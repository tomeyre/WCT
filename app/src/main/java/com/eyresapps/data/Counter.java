package com.eyresapps.data;

/**
 * Created by thomaseyre on 22/02/2018.
 */

public class Counter {

    private String name;
    private Integer count;

    public Counter (String name, Integer count){
        this.name = name;
        this.count = count;
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
}
