package com.eyresapps.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eyresapps.bindviewholders.BindViewHolder;
import com.eyresapps.bindviewholders.BindViewHolderChicargo;
import com.eyresapps.bindviewholders.BindViewHolderDurham;
import com.eyresapps.bindviewholders.BindViewHolderLa;
import com.eyresapps.bindviewholders.BindViewHolderNewOrleans;
import com.eyresapps.bindviewholders.BindViewHolderSanFran;
import com.eyresapps.bindviewholders.BindViewHolderSeattle;
import com.eyresapps.crimetracker.R;
import com.eyresapps.data.Crimes;
import com.eyresapps.data.viewholders.CrimeViewHolder;
import com.eyresapps.data.viewholders.CrimeViewHolderDurham;
import com.eyresapps.data.viewholders.CrimeViewHolderLa;
import com.eyresapps.data.viewholders.CrimeViewHolderNewOrleans;
import com.eyresapps.data.viewholders.CrimeViewHolderSanFran;
import com.eyresapps.data.viewholders.CrimeViewHolderSeattle;
import com.eyresapps.utils.CurrentAddressUtil;

import java.util.ArrayList;

public class RVAdapterCrimes extends RecyclerView.Adapter<CrimeViewHolder> {

    ArrayList<Crimes> crimes;
    Context context;
    CurrentAddressUtil currentAddressUtil = CurrentAddressUtil.getInstance();
    String address = currentAddressUtil.getAddress().toLowerCase();

    public RVAdapterCrimes(ArrayList<Crimes> crimes, Context context)
    {
        this.crimes = crimes;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CrimeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        if(address.contains("uk")){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.crime, viewGroup, false);
            return new CrimeViewHolder(v);
        }
        else if(address.contains("chicago")){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.crime_chicago, viewGroup, false);
            return new CrimeViewHolder(v);
        }
        else if(address.toLowerCase().contains("los angeles") && address.toUpperCase().contains("USA")){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.crime_la, viewGroup, false);
            return new CrimeViewHolderLa(v);
        }
        else if (address.toLowerCase().contains("durham, nc")) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.crime_durham, viewGroup, false);
            return new CrimeViewHolderDurham(v);
        }
        else if (address.toLowerCase().contains("new orleans")) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.crime_new_orleans, viewGroup, false);
            return new CrimeViewHolderNewOrleans(v);
        }
        else if (address.toLowerCase().contains("san francisco")) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.crime_san_fran, viewGroup, false);
            return new CrimeViewHolderSanFran(v);
        }
        else if (address.toLowerCase().contains("seattle")) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.crime_seattle, viewGroup, false);
            return new CrimeViewHolderSeattle(v);
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.crime, viewGroup, false);
        return new CrimeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CrimeViewHolder crimeViewHolder, int i) {
        if(address.contains("uk")){
            new BindViewHolder().bind(crimeViewHolder, i, crimes, context);
        }
        else if(address.contains("chicago")){
            new BindViewHolderChicargo().bind(crimeViewHolder, i, crimes, context);
        }
        else if(address.toLowerCase().contains("los angeles") && address.toUpperCase().contains("USA")){
            new BindViewHolderLa().bind((CrimeViewHolderLa) crimeViewHolder, i, crimes, context);
        }
        else if (address.toLowerCase().contains("durham, nc")) {
            new BindViewHolderDurham().bind((CrimeViewHolderDurham) crimeViewHolder, i, crimes, context);
        }
        else if (address.toLowerCase().contains("new orleans")) {
            new BindViewHolderNewOrleans().bind((CrimeViewHolderNewOrleans) crimeViewHolder, i, crimes, context);
        }
        else if (address.toLowerCase().contains("san francisco")) {
            new BindViewHolderSanFran().bind((CrimeViewHolderSanFran) crimeViewHolder, i, crimes, context);
        }
        else if (address.toLowerCase().contains("seattle")) {
            new BindViewHolderSeattle().bind((CrimeViewHolderSeattle) crimeViewHolder, i, crimes, context);
        }
    }

    @Override
    public int getItemCount() {
        if(crimes != null) {
            return crimes.size();
        }
        else return 0;
    }
}