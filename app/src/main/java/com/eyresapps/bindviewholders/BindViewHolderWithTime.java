package com.eyresapps.bindviewholders;

import android.content.Context;

import com.eyresapps.data.Crimes;
import com.eyresapps.data.viewholders.CrimeViewHolderWithTime;
import com.eyresapps.utils.CapitalizeString;
import com.eyresapps.utils.DateParserUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by thomaseyre on 14/07/2018.
 */

public class BindViewHolderWithTime {
    public void bind(CrimeViewHolderWithTime crimeViewHolder, int i, ArrayList<Crimes> crimes, Context context) {
        crimeViewHolder.crime.setText(new CapitalizeString().getString(crimes.get(i).getCrimeType()));
        try {
            Date date = new DateParserUtil().getParser().parse(crimes.get(i).getDateOccur());
            String formattedDate = new DateParserUtil().getDateFormat().format(date);
            crimeViewHolder.dateOccur.setText(formattedDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        crimeViewHolder.time.setText(crimes.get(i).getTimeOccur().substring(0,2) + ":" + crimes.get(i).getTimeOccur().substring(2,crimes.get(i).getTimeOccur().length()));
        String description = new CapitalizeString().justCapitalize(crimes.get(i).getDescription());
        crimeViewHolder.description.setText(description);
        crimeViewHolder.mCardView.setTag(i);
    }
}
