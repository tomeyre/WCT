package com.eyresapps.crimes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.eyresapps.crimetracker.MainActivity;
import com.eyresapps.data.Crimes;
import com.eyresapps.utils.CrimeCountList;
import com.eyresapps.utils.DateUtil;
import com.eyresapps.utils.FilterList;
import com.eyresapps.utils.HttpConnectUtil;
import com.eyresapps.utils.LatitudeAndLongitudeUtil;
import com.eyresapps.utils.UpdateMap;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by thomaseyre on 22/07/2018.
 */

public class GetSanFranCrime extends AsyncTask<String, Integer, ArrayList<ArrayList<Crimes>>> {
    private ArrayList<Crimes> crimes = new ArrayList<>();
    private ArrayList<ArrayList<Crimes>> crimeList = new ArrayList<>();
    private Crimes crime;
    private boolean firstOfItsKind = true;
    private Context context;
    private DateUtil dateUtil = DateUtil.getInstance();
    private LatitudeAndLongitudeUtil latLng = LatitudeAndLongitudeUtil.getInstance();
    private boolean filterByCrime;
    private int attempts;
    boolean finished = false;
    Integer finishedCounter = 0;
    private Integer maxCrimesPerThread = 250;
    private int totalCrimeCount = 0;
    private ProgressDialog progressDialog;
    private boolean filterByMonth = false;
    private FilterList filterList = FilterList.getInstance();
    private  Boolean firstLoad;

    public GetSanFranCrime(Context context, boolean filterByCrime, boolean filterByMonth, boolean firstLoad, int attempts) {
        this.context = context;
        this.filterByCrime = filterByCrime;
        this.filterByMonth = filterByMonth;
        this.attempts = attempts;
        this.firstLoad = firstLoad;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected ArrayList<ArrayList<Crimes>> doInBackground(String... params) {
        ((MainActivity)context).runOnUiThread(new Runnable() {
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
            Log.i("SAN FRAN ", "https://data.sfgov.org/resource/cuks-n6tp.json?$where=within_circle(location, " + latLng.getLatLng().latitude + ", " + latLng.getLatLng().longitude + ", 1000) and date between '" + dateUtil.getYear() + "-" + dateUtil.getMonth() + "-01T00:00:00,000' and '" + dateUtil.getYearAhead() + "-" + dateUtil.getMonthAhead() + "-01T00:00:00.000'");

            // create new instance of the httpConnect class
            HttpConnectUtil jParser = new HttpConnectUtil();

            // get json string from service url
            String json = jParser.getJSONFromUrl(params[0]);

            // Get JSON object contains an object and an array
            final JSONArray jsonArray = new JSONArray(json);

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
            ((MainActivity) context).dismissDialog("");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return crimeList;
    }


    private void getCrimes(JSONArray jsonArray, int start, int end) {
        try {
            for (int i = start; i < end; i++) {
                //crime / date / timeOccur / outcome / streetname / lat /lng / weapon / description

                crime = (new Crimes(jsonArray.getJSONObject(i).getString("category"),
                        jsonArray.getJSONObject(i).getString("date"),
                        "",
                        jsonArray.getJSONObject(i).getString("resolution"),
                        jsonArray.getJSONObject(i).getString("address"),
                        jsonArray.getJSONObject(i).getJSONObject("location").getJSONArray("coordinates").getDouble(1),
                        jsonArray.getJSONObject(i).getJSONObject("location").getJSONArray("coordinates").getDouble(0),
                        "",
                        jsonArray.getJSONObject(i).getString("descript"),
                        jsonArray.getJSONObject(i).getString("time"),
                        "",
                        ""));

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
                    if (crimeList.get(j).get(0).getLatitude() == jsonArray.getJSONObject(position).getJSONObject("location").getJSONArray("coordinates").getDouble(1) &&
                            crimeList.get(j).get(0).getLongitude() == jsonArray.getJSONObject(position).getJSONObject("location").getJSONArray("coordinates").getDouble(0)) {
                        crimes = new ArrayList<>();
                        crimes = crimeList.get(j);
                        crimes.add(crime);
                        crimeList.set(j, crimes);
                        firstOfItsKind = false;
                        break;
                    }
                }
                if (firstOfItsKind && jsonArray.getJSONObject(position).has("address")) {
                    crimes = new ArrayList<>();
                    crimes.add(crime);
                    crimeList.add(crimeList.size(), crimes);
                }

                totalCrimeCount++;
                firstOfItsKind = true;
                final int arraySize = jsonArray.length() - 1;
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.setMessage("loading " + totalCrimeCount * 100 / arraySize + "%");
//                        if((totalCrimeCount * 100 / arraySize) >= 98){
//                            progressDialog.setMessage("Updating map...");
//                        }
                    }
                });

            }
        }catch (Exception e){e.printStackTrace();
            progressDialog.dismiss();
            Log.i("ERROR ", "caught " + e.getMessage() + " / " + position);}
    }

    @Override
    protected void onPostExecute(ArrayList<ArrayList<Crimes>> list) {
        progressDialog.dismiss();
        if (list != null && !list.isEmpty()) {
            if(firstLoad){
                dateUtil.setMonthStatsReset(dateUtil.getMonth());
                dateUtil.setMonthStats(dateUtil.getMonth());
                dateUtil.setYearStatsReset(dateUtil.getYear());
                dateUtil.setYearStats(dateUtil.getYear());
            }
            if(filterByCrime){
                new UpdateMap(context, list).execute();
            }else {
                new CrimeCountList(context).sortCrimesCount(crimeList, true, false, context, true);
                new UpdateMap(context, list).execute();
            }
        } else if (latLng.getLatLng().latitude == 0 && latLng.getLatLng().longitude == 0) {
            ((MainActivity) context).dismissDialog("Gps unable to get location");
        } else if ((!filterByCrime && !filterByMonth) && attempts < 3 && (list == null || list.isEmpty())) {
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
            new GetSanFranCrime(context, filterByCrime, filterByMonth, firstLoad, attempts).execute("https://data.sfgov.org/resource/cuks-n6tp.json?$where=within_circle(location, " + latLng.getLatLng().latitude + ", " + latLng.getLatLng().longitude + ", 1000) and date between '" + dateUtil.getYear() + "-" + dateUtil.getMonth() + "-01T00:00:00,000' and '" + dateUtil.getYearAhead() + "-" + dateUtil.getMonthAhead() + "-01T00:00:00.000'");
        } else {
            ((MainActivity) context).dismissDialog("No crime Statistics for this date");
            ((MainActivity)context).setScreenEnabled();
        }
    }
}