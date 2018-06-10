package eyresapps.com.utils;

import java.util.Calendar;

/**
 * Created by Tom on 22/10/2017.
 */
public class DateUtil {
    private static DateUtil ourInstance = new DateUtil();

    int year = Calendar.getInstance().get(Calendar.YEAR);
    int month = Calendar.getInstance().get(Calendar.MONTH);
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

    int maxMonth;
    int maxYear;

    public static DateUtil getInstance() {
        return ourInstance;
    }

    private DateUtil() {
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

    public int getMonthBehind(){
        int monthBehind = this.month;
        if(monthBehind == 0){
            monthBehind = 12;
        }else{
            monthBehind--;
        }
        return monthBehind;
    }

    public int getYearBehind(){
        int yearBehind = this.year;
        if(this.month == 0){
            yearBehind--;
        }
        return yearBehind;
    }

    public String getMonthAsString(){
        String monthString;

        if(this.month == 0){
            monthString = "January";
        }else if(this.month == 1){
            monthString = "February";
        }else if(this.month == 2){
            monthString = "March";
        }else if(this.month == 3){
            monthString = "April";
        }else if(this.month == 4){
            monthString = "May";
        }else if(this.month == 5){
            monthString = "June";
        }else if(this.month == 6){
            monthString = "July";
        }else if(this.month == 7){
            monthString = "August";
        }else if(this.month == 8){
            monthString = "September";
        }else if(this.month == 9){
            monthString = "October";
        }else if(this.month == 10){
            monthString = "November";
        }else {
            monthString = "December";
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
