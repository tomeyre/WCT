package com.eyresapps.data;

import java.io.Serializable;

public class Crimes implements Serializable{

    private String crimeType;
    private String weapon;
    private String dateOccur;
    private String dateReport;
    private String timeOccur;
    private String timeReport;
    private String outcome;
    private String streetName;
    private double latitude;
    private double longitude;
    private String description;

    public Crimes(String crime, String date, String dateReport, String outcome, String streetName, double latitude, double longitude, String weapon, String description, String time, String timeReport) {
        this.crimeType = crime;
        this.weapon = weapon;
        this.dateOccur = date;
        this.dateReport = dateReport;
        this.timeOccur = time;
        this.timeReport = timeReport;
        this.outcome = outcome;
        this.streetName = streetName.replace("On or near","");
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public String getStreetName(){
        return streetName;
    }
    public String getWeapon() { return weapon; }
    public String getDateOccur() { return dateOccur; }
    public String getDateReport() { return dateReport; }
    public String getTimeOccur() { return timeOccur; }
    public String getTimeReport() { return timeReport; }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public String getCrimeType(){
        return crimeType;
    }
    public String getOutcome(){
        return outcome;
    }
    public String getDescription(){ return description; }

}