package com.eyresapps.crimeLocations;

import android.content.Context;

import com.eyresapps.crimes.GetChicargoCrime;
import com.eyresapps.crimes.GetDurhamCrime;
import com.eyresapps.crimes.GetLACrime;
import com.eyresapps.crimes.GetNewOrleansCrime;
import com.eyresapps.crimes.GetSanFranCrime;
import com.eyresapps.crimes.GetSeattleCrime;
import com.eyresapps.crimes.GetUKCrime;
import com.eyresapps.crimetracker.MainActivity;
import com.eyresapps.data.CrimeCount;
import com.eyresapps.utils.CurrentAddressUtil;
import com.eyresapps.utils.DateUtil;
import com.eyresapps.utils.LatitudeAndLongitudeUtil;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class GenerateCrimeUrl {

    DateUtil dateUtil = DateUtil.getInstance();
    LatitudeAndLongitudeUtil latLng = LatitudeAndLongitudeUtil.getInstance();
    CurrentAddressUtil currentAddress = CurrentAddressUtil.getInstance();
    CrimeCount crimeCount = CrimeCount.getInstance();

    public GenerateCrimeUrl(Context context, boolean filterByCrime, boolean filterByMonth, boolean firstLoad) {
        crimeCount.resetTotalsCount();
        System.out.println("ADDRESS : " + currentAddress.getAddress());
        if (currentAddress.getAddress().toLowerCase().contains("chicago")) {
            ((MainActivity)context).setYearStatsVisibility(GONE);
            new GetChicargoCrime(context, filterByCrime,filterByMonth, firstLoad, 0).execute("https://data.cityofchicago.org/resource/6zsd-86xi.json?$where=within_circle(location, " + latLng.getLatLng().latitude + ", " + latLng.getLatLng().longitude + ", 1000) and date between '" + dateUtil.getYear() + "-" + dateUtil.getMonth() + "-01T00:00:00' and '" + dateUtil.getYearAhead() + "-" + dateUtil.getMonthAhead() + "-01T00:00:00'");
        } else if (currentAddress.getAddress().contains("Los Angeles") && currentAddress.getAddress().contains("USA")) {
            ((MainActivity)context).setYearStatsVisibility(GONE);
            new GetLACrime(context, filterByCrime,filterByMonth, firstLoad, 0).execute("https://data.lacity.org/resource/7fvc-faax.json?$where=within_circle(location_1, " + latLng.getLatLng().latitude + ", " + latLng.getLatLng().longitude + ", 1000) and date_occ between '" + dateUtil.getYear() + "-" + dateUtil.getMonth() + "-01T00:00:00' and '" + dateUtil.getYearAhead() + "-" + dateUtil.getMonthAhead() + "-01T00:00:00'");
        } else if (currentAddress.getAddress().toLowerCase().contains("uk")) {
            ((MainActivity)context).setYearStatsVisibility(VISIBLE);
            new GetUKCrime(context, filterByCrime,filterByMonth, firstLoad, 0).execute("https://data.police.uk/api/crimes-street/all-crime?date=" + dateUtil.getYear() + "-" + dateUtil.getMonth() + "&lat=" + latLng.getLatLng().latitude + "&lng=" + latLng.getLatLng().longitude);
        } else if (currentAddress.getAddress().toLowerCase().contains("durham, nc")) {
            ((MainActivity)context).setYearStatsVisibility(GONE);
            new GetDurhamCrime(context, filterByCrime,filterByMonth, firstLoad, 0).execute("https://opendurham.nc.gov/api/records/1.0/search/?dataset=durham-police-crime-reports&rows=100&facet=date_rept&facet=dow1&facet=reportedas&facet=chrgdesc&facet=big_zone&refine.date_rept=" + dateUtil.getYear() + "%2F" + dateUtil.getMonth() + "&geofilter.distance=" + latLng.getLatLng().latitude + "%2C+" + latLng.getLatLng().longitude + "%2C+1000");
        } else if (currentAddress.getAddress().toLowerCase().contains("new orleans")) {
            ((MainActivity)context).setYearStatsVisibility(GONE);
            new GetNewOrleansCrime(context, filterByCrime,filterByMonth, firstLoad, 0).execute("https://data.nola.gov/resource/j5wk-jv3p.json?$where=within_circle(location, " + latLng.getLatLng().latitude + ", " + latLng.getLatLng().longitude + ", 1000) and timecreate between '" + dateUtil.getYear() + "-" + dateUtil.getMonth() + "-01T00:00:00,000' and '" + dateUtil.getYearAhead() + "-" + dateUtil.getMonthAhead() + "-01T00:00:00.000'");
        } else if (currentAddress.getAddress().toLowerCase().contains("san francisco")) {
            ((MainActivity)context).setYearStatsVisibility(GONE);
            new GetSanFranCrime(context, filterByCrime,filterByMonth, firstLoad, 0).execute("https://data.sfgov.org/resource/cuks-n6tp.json?$where=within_circle(location, " + latLng.getLatLng().latitude + ", " + latLng.getLatLng().longitude + ", 1000) and date between '" + dateUtil.getYear() + "-" + dateUtil.getMonth() + "-01T00:00:00,000' and '" + dateUtil.getYearAhead() + "-" + dateUtil.getMonthAhead() + "-01T00:00:00.000'");
        } else if (currentAddress.getAddress().toLowerCase().contains("seattle")) {
            ((MainActivity)context).setYearStatsVisibility(GONE);
            new GetSeattleCrime(context, filterByCrime,filterByMonth, firstLoad, 0).execute("https://data.seattle.gov/resource/pu5n-trf4.json?$where=within_circle(incident_location, " + latLng.getLatLng().latitude + ", " + latLng.getLatLng().longitude + ", 1000) and at_scene_time between '" + dateUtil.getYear() + "-" + dateUtil.getMonth() + "-01T00:00:00,000' and '" + dateUtil.getYearAhead() + "-" + dateUtil.getMonthAhead() + "-01T00:00:00.000'");
        } else {
            ((MainActivity)context).dismissDialog("Not available in this location sorry");
        }
    }
}