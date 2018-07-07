package eyresapps.com.crimes;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;

import eyresapps.com.data.Counter;
import eyresapps.com.data.Crimes;
import eyresapps.com.utils.CrimeCountList;
import eyresapps.com.utils.DateUtil;
import eyresapps.com.utils.HttpConnectUtil;
import eyresapps.com.utils.LatitudeAndLongitudeUtil;
import eyresapps.com.wct.MainActivity;

public class GetChicargoCrime extends AsyncTask<String, String, ArrayList<ArrayList<Crimes>>> {
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
    int nextMonth = dateUtil.getMonth();
    int nextYear = dateUtil.getYear();

    public GetChicargoCrime(Context context, boolean search, int attempts) {
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
            if(nextMonth > 12){
                nextMonth = 1;
                nextYear = dateUtil.getYear() - 1;
            }
            Log.i("GET CHICARGO Crime URL : ","https://data.cityofchicago.org/resource/6zsd-86xi.json?$where=within_circle(location, " + latLng.getLatLng().latitude + ", " + latLng.getLatLng().longitude + ", 1000) and date between '" + dateUtil.getYear() + "-" + dateUtil.getMonth() + "-01T00:00:00' and '" + nextYear + "-" + nextMonth + "-01T00:00:00'");

            // create new instance of the httpConnect class
            HttpConnectUtil jParser = new HttpConnectUtil();

            // get json string from service url
            String json = jParser.getJSONFromUrl(params[0]);

            // Get JSON object contains an object and an array
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {

                //crime / date / time / outcome / streetname / lat /lng / weapon / description

                crime = (new Crimes(jsonArray.getJSONObject(i).getString("primary_type"),
                        jsonArray.getJSONObject(i).getString("date"),
                        jsonArray.getJSONObject(i).getString("date"),
                        "Arrest made : " + jsonArray.getJSONObject(i).getString("arrest"),
                        jsonArray.getJSONObject(i).getString("block"),
                        jsonArray.getJSONObject(i).getDouble("latitude"),
                                jsonArray.getJSONObject(i).getDouble("longitude"),"",
                        jsonArray.getJSONObject(i).getString("description")));

                if (crimeList.isEmpty()) {
                    crimes.add(crime);
                    crimeList.add(crimes);
                } else {
                    for (int j = 0; j < crimeList.size(); j++) {
                        if (crimeList.get(j).get(0).getStreetName().toString().equals(jsonArray.getJSONObject(i).getString("block").replace("On or near", ""))) {
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
            ((MainActivity) context).updateMap(list,false);

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
            new GetChicargoCrime(context, bespokeSearch, attempts).execute("https://data.cityofchicago.org/resource/6zsd-86xi.json?$where=within_circle(location, " + latLng.getLatLng().latitude + ", " + latLng.getLatLng().longitude + ", 1000) and date between '" + dateUtil.getYearBehind() + "-" + dateUtil.getMonthBehind() + "-01T00:00:00' and '" + dateUtil.getYear() + "-" + dateUtil.getMonth() + "-01T00:00:00'");
        } else {
            ((MainActivity) context).dismissDialog("No crime Statistics for this date");
        }
    }
}