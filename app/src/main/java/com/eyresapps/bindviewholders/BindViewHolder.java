package com.eyresapps.bindviewholders;

import android.content.Context;

import com.eyresapps.data.Crimes;
import com.eyresapps.data.viewholders.CrimeViewHolder;
import com.eyresapps.utils.CapitalizeString;
import com.eyresapps.utils.DateParserUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by thomaseyre on 14/07/2018.
 */

public class BindViewHolder {
    public void bind(CrimeViewHolder crimeViewHolder, int i, ArrayList<Crimes> crimes, Context context) {
        crimeViewHolder.crime.setText(new CapitalizeString().getString(crimes.get(i).getCrimeType()));
        String outcomeText = crimes.get(i).getOutcome();
        if (outcomeText.contains(";")) {
            outcomeText = outcomeText.replace(";", "");
        }
        crimeViewHolder.outcome.setText(new CapitalizeString().getString(outcomeText.toLowerCase()));
        try {
            Date date = new DateParserUtil().getParser().parse(crimes.get(i).getDateOccur());
            String formattedDate = new DateParserUtil().getDateFormat().format(date);
            crimeViewHolder.dateOccur.setText(formattedDate.substring(2,formattedDate.length()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        crimeViewHolder.mCardView.setTag(i);
    }
}
