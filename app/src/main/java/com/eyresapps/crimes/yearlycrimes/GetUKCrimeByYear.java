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

public class GetUKCrimeByYear extends AsyncTask<String, Integer, ArrayList<Crimes>> {
    private ArrayList<Crimes> crimes = new ArrayList<>();
    private Crimes crime;
    private Context context;
    private DateUtil dateUtil = DateUtil.getInstance();
    private ProgressDialog progressDialog;
    private Boolean finished = false;
    private Integer finishedCounter = 0;
    private String id;

    public GetUKCrimeByYear(Context context, String id) {
        this.context = context;
        dateUtil.resetStatsDate();
        progressDialog = new ProgressDialog(context);
        this.id = id;
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
                final String url = "https://data.police.uk/api/crimes-at-location?date=" + dateUtil.getYearStats() + "-" + dateUtil.getMonthStats() + "&location_id=" + id;
                System.out.println(url);
                int year = dateUtil.getYearStats();
                int month = dateUtil.getMonthStats();
                final int innerMonth = month;
                final int innerYear = year;
                if (month == 1) {
                    year--;
                    month = 12;
                } else {
                    month--;
                }
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
                        }catch (Exception e){e.printStackTrace();}
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
            progressDialog.dismiss();
            e.printStackTrace();
        }

        return crimes;
    }


    private void getCrimes(JSONArray jsonArray, final int month, final int year) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).isNull("outcome_status")) {

                    //crime / date / timeOccur / outcome / streetname / lat /lng / weapon / description

                    crime = (new Crimes(jsonArray.getJSONObject(i).getString("category"),
                            jsonArray.getJSONObject(i).getString("month"),
                            "",
                            "No Outcome",
                            jsonArray.getJSONObject(i).getJSONObject("location").getJSONObject("street").getString("name"),
                            jsonArray.getJSONObject(i).getJSONObject("location").getDouble("latitude"),
                            jsonArray.getJSONObject(i).getJSONObject("location").getDouble("longitude"), "", "", "","",
                            jsonArray.getJSONObject(i).getJSONObject("location").getJSONObject("street").getString("id")));
                } else {
                    crime = (new Crimes(jsonArray.getJSONObject(i).getString("category"),
                            jsonArray.getJSONObject(i).getString("month"),
                            "",
                            jsonArray.getJSONObject(i).getJSONObject("outcome_status").getString("category"),
                            jsonArray.getJSONObject(i).getJSONObject("location").getJSONObject("street").getString("name"),
                            jsonArray.getJSONObject(i).getJSONObject("location").getDouble("latitude"),
                            jsonArray.getJSONObject(i).getJSONObject("location").getDouble("longitude"), "", "", "","",
                            jsonArray.getJSONObject(i).getJSONObject("location").getJSONObject("street").getString("id")));
                }
                addToList(crime);
            }
            if(jsonArray.length() == 0){
                addToList(new Crimes("none",Integer.toString(month),Integer.toString(year),"","",new Double(0),new Double(0),"","","","",""));
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