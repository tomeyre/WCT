package com.eyresapps.crimes.yearlycrimes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.eyresapps.crimetracker.YearStats;
import com.eyresapps.data.Crimes;
import com.eyresapps.utils.DateUtil;
import com.eyresapps.utils.HttpConnectUtil;

import org.json.JSONArray;

import java.util.ArrayList;

public class GetNewYorkCrimeByYear extends AsyncTask<String, Integer, ArrayList<Crimes>> {
    private ArrayList<Crimes> crimes = new ArrayList<>();
    private Crimes crime;
    private Context context;
    private DateUtil dateUtil = DateUtil.getInstance();
    private ProgressDialog progressDialog;
    private Boolean finished = false;
    private Integer finishedCounter = 0;
    private Crimes id;

    public GetNewYorkCrimeByYear(Context context, Crimes id) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        this.id = id;
        dateUtil.resetStatsDate();
        int month = dateUtil.getMonthStats();
        int newYear = dateUtil.getYearStats() + 1;
        int newMonth = dateUtil.getMonthStats() + 1;
        if (month == 12) {
            dateUtil.setYearStats(newYear);
            dateUtil.setMonthStats(1);
        } else {
            dateUtil.setMonthStats(newMonth);
        }
    }

    @Override
    protected ArrayList<Crimes> doInBackground(String... params) {
        ((YearStats)context).runOnUiThread(new Runnable() {
            public void run() {
                progressDialog.setMessage("Getting stats...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        });

        try {
            int maxThreads = 12;

            for (int i = 0; i < maxThreads; i++) {
                int year = dateUtil.getYearStats();
                int month = dateUtil.getMonthStats();
                if (month == 1) {
                    year--;
                    month = 12;
                } else {
                    month--;
                }
                String urlCheck = "";
                if (year >= dateUtil.getCurrentYear()) {
                    urlCheck = "https://data.cityofnewyork.us/resource/7x9x-zpz6.json?$where=latitude=" + id.getLatitude() + " and longitude=" + id.getLongitude() +" and cmplnt_fr_dt between '"+year+"-"+month+"-01T00:00:00' and '"+dateUtil.getYearStats()+"-"+dateUtil.getMonthStats()+"-01T00:00:00'";
                } else if(year < dateUtil.getCurrentYear()){
                    urlCheck = "https://data.cityofnewyork.us/resource/9s4h-37hy.json?$where=latitude=" + id.getLatitude() + " and longitude=" + id.getLongitude() +" and cmplnt_fr_dt between '"+year+"-"+month+"-01T00:00:00' and '"+dateUtil.getYearStats()+"-"+dateUtil.getMonthStats()+"-01T00:00:00'";
                } else {
                    System.out.println("ERROR year greater than current year");
                }
                final int innerMonth = month;
                final int innerYear = year;
                final String url = urlCheck;
                System.out.println(url);
                dateUtil.setYearStats(year);
                dateUtil.setMonthStats(month);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            // create new instance of the httpConnect class
                            HttpConnectUtil jParser = new HttpConnectUtil();

                            // get json string from service url
                            String json = jParser.getJSONFromUrl(url);

                            // Get JSON object contains an object and an array
                            final JSONArray jsonArray = new JSONArray(json);
                            getCrimes(jsonArray, innerMonth, innerYear);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }

            while (!finished) {
                if (finishedCounter >= maxThreads) {
                    finished = true;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

        return crimes;
    }


    private void getCrimes(JSONArray jsonArray, final int month, final int year) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {

                //crime / date / timeOccur / outcome / streetname / lat /lng / weapon / description

                crime = (new Crimes(jsonArray.getJSONObject(i).getString("ofns_desc").replaceAll("\\d", ""),
                        jsonArray.getJSONObject(i).getString("cmplnt_fr_dt"),
                        "",
                        "",
                        "",
                        jsonArray.getJSONObject(i).getDouble("latitude"),
                        jsonArray.getJSONObject(i).getDouble("longitude"),
                        "",
                        jsonArray.getJSONObject(i).getString("pd_desc"),
                        jsonArray.getJSONObject(i).getString("cmplnt_fr_tm"),
                        "",
                        jsonArray.getJSONObject(i).getString("cmplnt_num")));

                if (jsonArray.getJSONObject(i).has("cmplnt_to_dt")) {
                    crime.setDateReport(jsonArray.getJSONObject(i).getString("cmplnt_to_dt"));
                }
                if (jsonArray.getJSONObject(i).has("loc_of_occur_desc")) {
                    crime.setDescription(crime.getDescription() + " \u002D " + jsonArray.getJSONObject(i).getString("loc_of_occur_desc"));
                }
                if (jsonArray.getJSONObject(i).has("boro_nm")) {
                    crime.setStreetName(jsonArray.getJSONObject(i).getString("boro_nm"));
                } else {
                    crime.setStreetName("unknown");
                }
                if (jsonArray.getJSONObject(i).has("prem_typ_desc")) {
                    crime.setStreetName(jsonArray.getJSONObject(i).getString("prem_typ_desc"));
                    crime.setDescription(crime.getDescription() + " \u002D " + jsonArray.getJSONObject(i).getString("prem_typ_desc"));
                }
                if (jsonArray.getJSONObject(i).has("cmplnt_to_tm")) {
                    crime.setTimeReport(jsonArray.getJSONObject(i).getString("cmplnt_to_tm"));
                }
                System.out.println(crime.toString());
                addToList(crime);
            }
            if (null == jsonArray || jsonArray.length() == 0) {
                System.out.println("NONE " + month + " : " + year);
                addToList(new Crimes("none", Integer.toString(month), Integer.toString(year), "", "", new Double(0), new Double(0), "", "", "", "", ""));
            }
            finishedCounter++;
        } catch (Exception e) {
            finishedCounter++;
            e.printStackTrace();
        }
    }
    private synchronized void addToList(Crimes crime) {
        crimes.add(crime);
    }

    @Override
    protected void onPostExecute(ArrayList<Crimes> list) {
        progressDialog.dismiss();
        ((YearStats)context).setCrimes(list);
    }
}