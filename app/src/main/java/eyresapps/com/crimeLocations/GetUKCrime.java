package eyresapps.com.crimeLocations;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;

import eyresapps.com.utils.DateUtil;
import eyresapps.com.utils.HttpConnectUtil;
import eyresapps.com.utils.LatitudeAndLongitudeUtil;
import eyresapps.com.data.Crimes;
import eyresapps.com.wct.MainActivity;


public class GetUKCrime extends AsyncTask<String, String, ArrayList<ArrayList<Crimes>>> {
    ArrayList<Crimes> crimes = new ArrayList<>();
    ArrayList<ArrayList<Crimes>> crimeList = new ArrayList<>();
    Crimes crime;
    boolean firstOfItsKind = true;
    Context context;
    DateUtil dateUtil = DateUtil.getInstance();
    LatitudeAndLongitudeUtil latLng = LatitudeAndLongitudeUtil.getInstance();
    boolean bespokeSearch;

    public GetUKCrime(Context context, boolean search) {
        this.context = context;
        this.bespokeSearch = search;
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

            // create new instance of the httpConnect class
            HttpConnectUtil jParser = new HttpConnectUtil();

            // get json string from service url
            String json = jParser.getJSONFromUrl(params[0]);

            Log.d("JSON = ..............",json);

            // Get JSON object contains an object and an array
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).isNull("outcome_status")) {
                    crime = (new Crimes(jsonArray.getJSONObject(i).getString("category"),
                            jsonArray.getJSONObject(i).getString("month"),
                            "",
                            "No Outcome",
                            jsonArray.getJSONObject(i).getJSONObject("location").getJSONObject("street").getString("name"),
                            jsonArray.getJSONObject(i).getJSONObject("location").getDouble("latitude"),
                            jsonArray.getJSONObject(i).getJSONObject("location").getDouble("longitude"), "", ""));
                } else {
                    crime = (new Crimes(jsonArray.getJSONObject(i).getString("category"),
                            jsonArray.getJSONObject(i).getString("month"),
                            "",
                            jsonArray.getJSONObject(i).getJSONObject("outcome_status").getString("category"),
                            jsonArray.getJSONObject(i).getJSONObject("location").getJSONObject("street").getString("name"),
                            jsonArray.getJSONObject(i).getJSONObject("location").getDouble("latitude"),
                            jsonArray.getJSONObject(i).getJSONObject("location").getDouble("longitude"), "", ""));
                }
                if (crimeList.isEmpty()) {
                    crimes.add(crime);
                    crimeList.add(crimes);
                } else {
                    for (int j = 0; j < crimeList.size(); j++) {
                        if (crimeList.get(j).get(0).getStreetName().toString().equals(jsonArray.getJSONObject(i).getJSONObject("location").getJSONObject("street").getString("name").replace("On or near", ""))) {
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
            ((MainActivity) context).updateMap(list);
        } else if (latLng.getLatLng().latitude == 0 && latLng.getLatLng().longitude == 0) {
            ((MainActivity) context).dismissDialog();
        } else if(!bespokeSearch){
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
            new GetUKCrime(context, bespokeSearch).execute("https://data.police.uk/api/crimes-street/all-crime?date=" + dateUtil.getYear() + "-" + dateUtil.getMonth() + "&lat=" + latLng.getLatLng().latitude + "&lng=" + (latLng.getLatLng().longitude));
        } else {
            ((MainActivity) context).dismissDialog();
        }
    }
}