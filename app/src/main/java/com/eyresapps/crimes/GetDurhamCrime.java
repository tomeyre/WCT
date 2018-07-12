package com.eyresapps.crimes;


import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.eyresapps.data.Counter;
import com.eyresapps.data.Crimes;
import com.eyresapps.utils.CrimeCountList;
import com.eyresapps.utils.DateUtil;
import com.eyresapps.utils.HttpConnectUtil;
import com.eyresapps.utils.LatitudeAndLongitudeUtil;
import com.eyresapps.utils.UpdateMap;
import com.eyresapps.crimetracker.MainActivity;

import static com.google.android.gms.internal.zzahn.runOnUiThread;

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
    boolean finished = false;
    Integer finishedCounter = 0;
    private Integer maxCrimesPerThread = 250;
    private int totalCrimeCount = 0;
    private ProgressDialog progressDialog;

    public GetDurhamCrime(Context context, boolean search, int attempts) {
        this.context = context;
        this.bespokeSearch = search;
        this.attempts = attempts;
        progressDialog = new ProgressDialog(context);
    }


    @Override
    protected ArrayList<ArrayList<Crimes>> doInBackground(String... params) {
        runOnUiThread(new Runnable() {
            public void run() {
                progressDialog.setMessage("Looking for crimes in " + dateUtil.getMonthAsString());
                progressDialog.show();
            }
        });
        try {
            if (crimes != null || crimes.size() > 0) {
                crimes.clear();
            }
            if (crimeList != null || crimeList.size() > 0) {
                crimeList.clear();
            }
            Log.i("Get DURHAM Crime URL : ","https://opendurham.nc.gov/api/records/1.0/search/?dataset=durham-police-crime-reports&rows=100&facet=date_rept&facet=dow1&facet=reportedas&facet=chrgdesc&facet=big_zone&refine.date_rept=" + dateUtil.getYear() + "%2F" + dateUtil.getMonth() + "&geofilter.distance=" + latLng.getLatLng().latitude + "%2C+" + latLng.getLatLng().longitude + "%2C+1000");

            // create new instance of the httpConnect class
            HttpConnectUtil jParser = new HttpConnectUtil();

            // get json string from service url
            String json = jParser.getJSONFromUrl(params[0]);

            // Get JSON object contains an object and an array
            JSONObject jsonObject = new JSONObject(json);
            final JSONArray jsonArray = jsonObject.getJSONArray("records");

            int maxThreads = 0;

            if (null == jsonArray || jsonArray.length() == 0) {
                return crimeList;
            }

            if (jsonArray.length() < maxCrimesPerThread) {
                maxThreads = 1;
            } else {
                maxThreads = jsonArray.length() / maxCrimesPerThread;
                if(jsonArray.length() % maxCrimesPerThread > 0){
                    maxThreads++;
                }

            }

            for (int i = 0; i < maxThreads; i++) {
                final int position = i;
                final int finalMaxThreads = maxThreads;
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        if ((position + 1) == finalMaxThreads && finalMaxThreads == 1) {
                            getCrimes(jsonArray, 0, jsonArray.length());
                            finishedCounter++;
                        }
                        else if((position + 1) == finalMaxThreads){
                            getCrimes(jsonArray, (position * maxCrimesPerThread), jsonArray.length());
                            finishedCounter++;
                        } else {
                            getCrimes(jsonArray, (position * maxCrimesPerThread), (position + 1) * maxCrimesPerThread);
                            finishedCounter++;
                        }
                    }
                };
                thread.start();
            }

            while (!finished) {
                if (finishedCounter == maxThreads) {
                    finished = true;
                }
            }
            if (crimeList != null && !crimeList.isEmpty()) {
                for (int i = 0; i < crimeList.size(); i++) {
                    for (int j = 0; j < crimeList.get(i).size(); j++) {

                        if (counts.isEmpty()) {
                            counts.add(new Counter(crimeList.get(i).get(j).getCrimeType(), 1));
                            continue;
                        }
                        for (int k = 0; k < counts.size(); k++) {
                            if (counts.get(k).getName().equalsIgnoreCase(crimeList.get(i).get(j).getCrimeType())) {
                                int temp = counts.get(k).getCount();
                                counts.set(k, new Counter(crimeList.get(i).get(j).getCrimeType(), ++temp));
                                break;
                            }
                            if (k == counts.size() - 1) {
                                counts.add(new Counter(crimeList.get(i).get(j).getCrimeType(), 1));
                                break;
                            }
                        }
                    }
                }
            }
            ((MainActivity) context).dismissDialog("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return crimeList;
    }

    private void getCrimes(JSONArray jsonArray, int start, int end) {
        try {
            for (int i = start; i < end; i++) {
                //Geocoder geocoder;
                List<Address> addresses = null;
                //geocoder = new Geocoder(context, Locale.getDefault());

                //try {
                //addresses = geocoder.getFromLocation(jsonArray.getJSONObject(i).getJSONObject("fields").getJSONArray("geo_point_2d").getDouble(0),
                //jsonArray.getJSONObject(i).getJSONObject("fields").getJSONArray("geo_point_2d").getDouble(1), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                //}catch (Exception e){e.printStackTrace();}

                //crime / date / time / outcome / streetname / lat /lng / weapon / description
                if(jsonArray.getJSONObject(i).getJSONObject("fields").has("reportedas")) {

                    crime = (new Crimes(jsonArray.getJSONObject(i).getJSONObject("fields").getString("reportedas"),
                            jsonArray.getJSONObject(i).getJSONObject("fields").getString("strdate"),
                            jsonArray.getJSONObject(i).getJSONObject("fields").getString("hour_occu") + ":" +
                                    jsonArray.getJSONObject(i).getJSONObject("fields").getString("hour_fnd"),
                            jsonArray.getJSONObject(i).getJSONObject("fields").getString("chrgdesc"),
                            "Crime",
                            jsonArray.getJSONObject(i).getJSONObject("fields").getJSONArray("geo_point_2d").getDouble(0),
                            jsonArray.getJSONObject(i).getJSONObject("fields").getJSONArray("geo_point_2d").getDouble(1), "", ""));
                }
                else{
                    crime = (new Crimes(jsonArray.getJSONObject(i).getJSONObject("fields").getString("chrgdesc"),
                            jsonArray.getJSONObject(i).getJSONObject("fields").getString("strdate"),
                            jsonArray.getJSONObject(i).getJSONObject("fields").getString("hour_occu") + ":" +
                                    jsonArray.getJSONObject(i).getJSONObject("fields").getString("hour_fnd"),
                            jsonArray.getJSONObject(i).getJSONObject("fields").getString("chrgdesc"),
                            "Crime",
                            jsonArray.getJSONObject(i).getJSONObject("fields").getJSONArray("geo_point_2d").getDouble(0),
                            jsonArray.getJSONObject(i).getJSONObject("fields").getJSONArray("geo_point_2d").getDouble(1), "", ""));

                }
                addToList(jsonArray, i, crime);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void addToList(JSONArray jsonArray, int position, final Crimes crime){
        try {
            if (crimeList.isEmpty()) {
                crimes.add(crime);
                crimeList.add(crimes);
            } else {
                for (int j = 0; j < crimeList.size(); j++) {
                    if (crimeList.get(j).get(0).getLatitude() == jsonArray.getJSONObject(position).getJSONObject("fields").getJSONArray("geo_point_2d").getDouble(0) &&
                            crimeList.get(j).get(0).getLongitude() == jsonArray.getJSONObject(position).getJSONObject("fields").getJSONArray("geo_point_2d").getDouble(1)) {
                        crimes = new ArrayList<>();
                        crimes = crimeList.get(j);
                        crimes.add(crime);
                        crimeList.set(j, crimes);
                        firstOfItsKind = false;
                        break;
                    }
                }
                if (firstOfItsKind && jsonArray.getJSONObject(position).has("fields")) {
                    crimes = new ArrayList<>();
                    crimes.add(crime);
                    crimeList.add(crimeList.size(), crimes);
                }

                totalCrimeCount++;
                firstOfItsKind = true;
                final int arraySize = jsonArray.length() - 1;
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.setMessage("loading " + totalCrimeCount * 100 / arraySize + "%");
//                        if((totalCrimeCount * 100 / arraySize) >= 98){
//                            progressDialog.setMessage("Updating map...");
//                        }
                    }
                });

            }
        }catch (Exception e){e.printStackTrace();
            Log.i("ERROR ", "caught " + e.getMessage() + " / " + position);}
    }

    @Override
    protected void onPostExecute(ArrayList<ArrayList<Crimes>> list) {
        progressDialog.dismiss();
        if (list != null && !list.isEmpty()) {
            new CrimeCountList(context).sortCrimesCount(counts, true);
            new UpdateMap(context,list,false).execute();

        } else if (latLng.getLatLng().latitude == 0 && latLng.getLatLng().longitude == 0) {
            ((MainActivity) context).dismissDialog("Gps unable to get location");
        } else if (!bespokeSearch && attempts < 3 && (list == null || list.isEmpty())) {
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