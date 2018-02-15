package eyresapps.com.data;

import java.io.Serializable;

public class Crimes implements Serializable{

    private String crimeType;
    private String weapon;
    private String date;
    private String time;
    private String outcome;
    private String streetName;
    private double latitude;
    private double longitude;
    private String description;

    public Crimes(String crime, String date, String time, String outcome, String streetName, double latitude, double longitude, String weapon, String description) {
        this.crimeType = crime;
        this.weapon = weapon;
        this.date = date;
        this.time = time;
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
    public String getDate() { return date; }
    public String getTime() { return time; }
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

//    @Override
//    public String toString() {
//        return "Crime= " + crimeType + "\nStreetName= " + streetName + "\nOutcome= " + outcome;
//    }

}