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
    private String id;

    public Crimes(String crime, String date, String dateReport, String outcome, String streetName,
                  double latitude, double longitude, String weapon, String description, String time,
                  String timeReport, String id) {
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
        this.id = id;
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
    public String getId(){ return id; }

    public void setCrimeType(String crimeType) {
        this.crimeType = crimeType;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public void setDateOccur(String dateOccur) {
        this.dateOccur = dateOccur;
    }

    public void setDateReport(String dateReport) {
        this.dateReport = dateReport;
    }

    public void setTimeOccur(String timeOccur) {
        this.timeOccur = timeOccur;
    }

    public void setTimeReport(String timeReport) {
        this.timeReport = timeReport;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return "TYPE : " + crimeType + " DATE : " + dateOccur + " YEAR : " + dateReport;
    }
}