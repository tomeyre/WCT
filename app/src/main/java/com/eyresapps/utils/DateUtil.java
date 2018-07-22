package com.eyresapps.utils;

import java.util.Calendar;

/**
 * Created by Tom on 22/10/2017.
 */
public class DateUtil {
    private static DateUtil ourInstance = new DateUtil();

    int year = Calendar.getInstance().get(Calendar.YEAR);
    int month = Calendar.getInstance().get(Calendar.MONTH);
    int yearStats = Calendar.getInstance().get(Calendar.YEAR);
    int monthStats = Calendar.getInstance().get(Calendar.MONTH);
    int yearStatsReset;
    int monthStatsReset;
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

    int maxMonth;
    int maxYear;

    public static DateUtil getInstance() {
        return ourInstance;
    }

    private DateUtil() {
    }

    public void setYearStatsReset(int yearStatsReset) {
        this.yearStatsReset = yearStatsReset;
    }

    public void setMonthStatsReset(int monthStatsReset) {
        this.monthStatsReset = monthStatsReset;
    }

    public int getYearStats() {
        return yearStats;
    }

    public void setYearStats(int yearStats) {
        this.yearStats = yearStats;
    }

    public int getMonthStats() {
        return monthStats;
    }

    public void setMonthStats(int monthStats) {
        this.monthStats = monthStats;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void resetDate(){
        this.year = Calendar.getInstance().get(Calendar.YEAR);
        this.month = Calendar.getInstance().get(Calendar.MONTH);
    }

    public void resetStatsDate(){
        this.yearStats = yearStatsReset;
        this.monthStats = monthStatsReset;
    }

    public int getMonthAhead(){
        int monthAhead = this.month;
        if(monthAhead == 12){
            monthAhead = 1;
        }else{
            monthAhead++;
        }
        return monthAhead;
    }

    public int getYearAhead(){
        int yearAhead = this.year;
        if(this.month == 12){
            yearAhead++;
        }
        return yearAhead;
    }

    public String getMonthAsString(){
        String monthString;

        if(this.month == 1){
            monthString = "January";
        }else if(this.month == 2){
            monthString = "February";
        }else if(this.month == 3){
            monthString = "March";
        }else if(this.month == 4){
            monthString = "April";
        }else if(this.month == 5){
            monthString = "May";
        }else if(this.month == 6){
            monthString = "June";
        }else if(this.month == 7){
            monthString = "July";
        }else if(this.month == 8){
            monthString = "August";
        }else if(this.month == 9){
            monthString = "September";
        }else if(this.month == 10){
            monthString = "October";
        }else if(this.month == 11){
            monthString = "November";
        }else {
            monthString = "December";
        }

        return monthString;
    }

    public String getShortMonthAsString(Integer month){
        String monthString;

        if(month == 0){
            monthString = "JAN";
        }else if(month == 1){
            monthString = "FEB";
        }else if(month == 2){
            monthString = "MAR";
        }else if(month == 3){
            monthString = "APR";
        }else if(month == 4){
            monthString = "MAY";
        }else if(month == 5){
            monthString = "JUN";
        }else if(month == 6){
            monthString = "JUL";
        }else if(month == 7){
            monthString = "AUG";
        }else if(month == 8){
            monthString = "SEP";
        }else if(month == 9){
            monthString = "OCT";
        }else if(month == 10){
            monthString = "NOV";
        }else {
            monthString = "DEC";
        }

        return monthString;
    }

    public int getMaxMonth() {
        return maxMonth;
    }

    public void setMaxMonth(int maxMonth) {
        this.maxMonth = maxMonth;
    }

    public int getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }

    public int getUKMinYearValue(){
        return 2015;
    }

    public int getChicargoMinYearValue(){
        return 2001;
    }

    public int getDurhamMinYearValue(){
        return 2013;
    }

    public int getLAMinYearValue(){
        return 2010;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }
}
