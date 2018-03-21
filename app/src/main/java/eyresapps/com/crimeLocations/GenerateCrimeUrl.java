package eyresapps.com.crimeLocations;

import android.content.Context;

import eyresapps.com.crimes.GetChicargoCrime;
import eyresapps.com.crimes.GetDurhamCrime;
import eyresapps.com.crimes.GetLACrime;
import eyresapps.com.crimes.GetUKCrime;
import eyresapps.com.data.CrimeCount;
import eyresapps.com.utils.CurrentAddressUtil;
import eyresapps.com.utils.DateUtil;
import eyresapps.com.utils.LatitudeAndLongitudeUtil;
import eyresapps.com.wct.MainActivity;

public class GenerateCrimeUrl {

    DateUtil dateUtil = DateUtil.getInstance();
    LatitudeAndLongitudeUtil latLng = LatitudeAndLongitudeUtil.getInstance();
    CurrentAddressUtil currentAddress = CurrentAddressUtil.getInstance();
    CrimeCount crimeCount = CrimeCount.getInstance();

    public GenerateCrimeUrl(Context context, boolean bespokeSearch) {
        crimeCount.resetTotalsCount();
        if (currentAddress.getAddress().toLowerCase().contains("chicago")) {
            new GetChicargoCrime(context, bespokeSearch, 0).execute("https://data.cityofchicago.org/resource/6zsd-86xi.json?$where=within_circle(location, " + latLng.getLatLng().latitude + ", " + latLng.getLatLng().longitude + ", 1000) and date between '" + dateUtil.getYear() + "-" + dateUtil.getMonth() + "-01T00:00:00' and '2017-02-01T00:00:00'");
        } else if (currentAddress.getAddress().toLowerCase().contains("los angeles")) {
            new GetLACrime(context, bespokeSearch, 0).execute("https://data.lacity.org/resource/7fvc-faax.json?$where=within_circle(location_1, " + latLng.getLatLng().latitude + ", " + latLng.getLatLng().longitude + ", 1000) and date_occ between '" + dateUtil.getYear() + "-" + dateUtil.getMonth() + "-01T00:00:00' and '2017-02-01T00:00:00'");
        } else if (currentAddress.getAddress().toLowerCase().contains("uk")) {
            new GetUKCrime(context, bespokeSearch, 0).execute("https://data.police.uk/api/crimes-street/all-crime?date=" + dateUtil.getYear() + "-" + dateUtil.getMonth() + "&lat=" + latLng.getLatLng().latitude + "&lng=" + latLng.getLatLng().longitude);
        } else if (currentAddress.getAddress().toLowerCase().contains("durham, nc")) {
            new GetDurhamCrime(context, bespokeSearch, 0).execute("https://opendurham.nc.gov/api/records/1.0/search/?dataset=durham-police-crime-reports&rows=100&facet=date_rept&facet=dow1&facet=reportedas&facet=chrgdesc&facet=big_zone&refine.date_rept=" + dateUtil.getYear() + "%2F" + dateUtil.getMonth() + "&geofilter.distance=" + latLng.getLatLng().latitude + "%2C+" + latLng.getLatLng().longitude + "%2C+1000");
        }else {
            ((MainActivity)context).dismissDialog("Not available in this location sorry");
        }
    }
}