package com.eyresapps.data.viewholders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.eyresapps.crimetracker.R;

public class CrimeViewHolder extends RecyclerView.ViewHolder {

    public CardView mCardView;
    public TextView crime;
    public TextView outcome;
    public TextView description;
    public TextView dateOccur;


    public CrimeViewHolder(View itemView) {
        super(itemView);
        crime = itemView.findViewById(R.id.crime);
        outcome = itemView.findViewById(R.id.outcome);
        description = itemView.findViewById(R.id.description);
        mCardView = itemView.findViewById(R.id.cardViewCrime);
        dateOccur = itemView.findViewById(R.id.dateOccur);
    }
}