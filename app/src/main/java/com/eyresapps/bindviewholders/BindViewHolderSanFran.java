package com.eyresapps.bindviewholders;

import android.content.Context;

import com.eyresapps.data.Crimes;
import com.eyresapps.data.viewholders.CrimeViewHolderSanFran;
import com.eyresapps.utils.CapitalizeString;
import com.eyresapps.utils.DateParserUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by thomaseyre on 14/07/2018.
 */

public class BindViewHolderSanFran {
    public void bind(CrimeViewHolderSanFran crimeViewHolder, int i, ArrayList<Crimes> crimes, Context context) {
        crimeViewHolder.crime.setText(new CapitalizeString().getString(crimes.get(i).getCrimeType()));
        String outcomeText = crimes.get(i).getOutcome();
        crimeViewHolder.outcome.setText(new CapitalizeString().getString(outcomeText.toLowerCase()));
        try {
            Date date = new DateParserUtil().getParser().parse(crimes.get(i).getDateOccur());
            String formattedDate = new DateParserUtil().getDateTimeFormat().format(date);
            crimeViewHolder.dateOccur.setText(formattedDate.substring(0,formattedDate.length()-9));

        } catch (Exception e) {
            e.printStackTrace();
        }
        crimeViewHolder.time.setText(crimes.get(i).getTimeOccur());
        String description = new CapitalizeString().justCapitalize(crimes.get(i).getDescription());
        crimeViewHolder.description.setText(description);
        crimeViewHolder.mCardView.setTag(i);
    }
}
