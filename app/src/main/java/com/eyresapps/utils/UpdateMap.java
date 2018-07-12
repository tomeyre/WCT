package com.eyresapps.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import com.eyresapps.data.Crimes;
import com.eyresapps.crimetracker.MainActivity;

public class UpdateMap extends AsyncTask<Integer, Integer, Integer> {

    private ProgressDialog updatingMapDialog;
    private Context context;
    ArrayList<ArrayList<Crimes>> list;
    boolean filter;

    public UpdateMap(Context context, ArrayList<ArrayList<Crimes>> list, boolean filter){
        this.context = context;
        this.list = list;
        this.filter = filter;
    }


    @Override
    protected void onPreExecute() {
        updatingMapDialog = new ProgressDialog(context);
        updatingMapDialog.setMessage("Updating map...");
        updatingMapDialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Thread.sleep(100l);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((MainActivity) context).updateMap(list, filter);

        return null;
    }

    @Override
    protected void onPostExecute(Integer result) {
        System.out.println("stop showing map updating dialog");
        updatingMapDialog.dismiss();
    }
}