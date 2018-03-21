package eyresapps.com.crimes;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import eyresapps.com.data.Counter;
import eyresapps.com.data.Crimes;
import eyresapps.com.utils.CrimeCountList;
import eyresapps.com.utils.DateUtil;
import eyresapps.com.utils.HttpConnectUtil;
import eyresapps.com.utils.LatitudeAndLongitudeUtil;
import eyresapps.com.wct.MainActivity;

public class GetDurhamCrime extends AsyncTask<String, String, ArrayList<ArrayList<Crimes>>> {
    ArrayList<Crimes> crimes = new ArrayList<>();
    ArrayList<ArrayList<Crimes>> crimeList = new ArrayList<>();
    Crimes crime;
    boolean firstOfItsKind = true;
    Context context;
    DateUtil dateUtil = DateUtil.getInstance();
    LatitudeAndLongitudeUtil latLng = LatitudeAndLongitudeUtil.getInstance();
    boolean bespokeSearch;
    private int attempts;
    ArrayList<Counter> counts = new ArrayList<>();

    public GetDurhamCrime(Context context, boolean search, int attempts) {
        this.context = context;
        this.bespokeSearch = search;
        this.attempts = attempts;
    }


    @Override
    protected ArrayList<ArrayList<Crimes>> doInBackground(String... params) {
        try {
            if (crimes != null || crimes.size() > 0) {
                crimes.clear();
            }
            if (crimeList != null || crimeList.size() > 0) {
                crimeList.clear();
            }
            Log.i("Get UK Crime URL : ","https://data.police.uk/api/crimes-street/all-crime?date=" + dateUtil.getYear() + "-" + dateUtil.getMonth() + "&lat=" + latLng.getLatLng().latitude + "&lng=" + latLng.getLatLng().longitude);

            // create new instance of the httpConnect class
            HttpConnectUtil jParser = new HttpConnectUtil();

            // get json string from service url
            String json = jParser.getJSONFromUrl(params[0]);

            // Get JSON object contains an object and an array
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("records");

            for (int i = 0; i < jsonArray.length(); i++) {
                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(context, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(jsonArray.getJSONObject(i).getJSONObject("fields").getJSONArray("geo_point_2d").getDouble(0),
                            jsonArray.getJSONObject(i).getJSONObject("fields").getJSONArray("geo_point_2d").getDouble(1), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                }catch (Exception e){e.printStackTrace();}

                //crime / date / time / outcome / streetname / lat /lng / weapon / description

                crime = (new Crimes(jsonArray.getJSONObject(i).getJSONObject("fields").getString("reportedas"),
                        jsonArray.getJSONObject(i).getJSONObject("fields").getString("strdate"),
                        jsonArray.getJSONObject(i).getJSONObject("fields").getString("hour_fnd").substring(0,1) + ":" +
                                jsonArray.getJSONObject(i).getJSONObject("fields").getString("hour_fnd").substring(2,3),
                        jsonArray.getJSONObject(i).getJSONObject("fields").getString("chrgdesc"),
                        addresses.get(0).getThoroughfare().toString(),
                        jsonArray.getJSONObject(i).getJSONObject("fields").getJSONArray("geo_point_2d").getDouble(0),
                                jsonArray.getJSONObject(i).getJSONObject("fields").getJSONArray("geo_point_2d").getDouble(1),"",""));

                if (crimeList.isEmpty()) {
                    crimes.add(crime);
                    crimeList.add(crimes);
                } else {
                    for (int j = 0; j < crimeList.size(); j++) {
                        if (crimeList.get(j).get(0).getStreetName().toString().equals(addresses.get(0).getThoroughfare().toString().replace("On or near", ""))) {
                            crimes = new ArrayList<>();
                            crimes = crimeList.get(j);
                            crimes.add(crime);
                            crimeList.set(j, crimes);
                            firstOfItsKind = false;
                            break;
                        }
                    }
                    if (firstOfItsKind) {
                        crimes = new ArrayList<>();
                        crimes.add(crime);
                        crimeList.add(crimeList.size(), crimes);
                    }
                    firstOfItsKind = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return crimeList;
    }

    @Override
    protected void onPostExecute(ArrayList<ArrayList<Crimes>> list) {
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).size(); j++) {

                    if (counts.isEmpty()) {
                        counts.add(new Counter(list.get(i).get(j).getCrimeType(), 1));
                        continue;
                    }
                    for (int k = 0; k < counts.size(); k++) {
                        if (counts.get(k).getName().equalsIgnoreCase(list.get(i).get(j).getCrimeType())) {
                            int temp = counts.get(k).getCount();
                            counts.set(k, new Counter(list.get(i).get(j).getCrimeType(), ++temp));
                            break;
                        }
                        if (k == counts.size() - 1) {
                            counts.add(new Counter(list.get(i).get(j).getCrimeType(), 1));
                            break;
                        }
                    }
                }
            }
            new CrimeCountList(context).sortCrimesCount(counts, true);
            ((MainActivity) context).updateMap(list);

        } else if (latLng.getLatLng().latitude == 0 && latLng.getLatLng().longitude == 0) {
            ((MainActivity) context).dismissDialog("Gps unable to get location");
        } else if (!bespokeSearch && attempts < 4 && (list == null || list.isEmpty())) {
            int year = dateUtil.getYear();
            int month = dateUtil.getMonth();
            if (month == 1) {
                year--;
                month = 12;
            } else {
                month--;
            }
            dateUtil.setYear(year);
            dateUtil.setMonth(month);
            attempts++;
            new GetDurhamCrime(context, bespokeSearch, attempts).execute("https://opendurham.nc.gov/api/records/1.0/search/?dataset=durham-police-crime-reports&rows=100&facet=date_rept&facet=dow1&facet=reportedas&facet=chrgdesc&facet=big_zone&refine.date_rept=" + dateUtil.getYear() + "%2F" + dateUtil.getMonth() + "&geofilter.distance=" + latLng.getLatLng().latitude + "%2C+" + latLng.getLatLng().longitude + "%2C+1000");
        } else {
            ((MainActivity) context).dismissDialog("No crime Statistics for this date");
        }
    }
}