package com.eyresapps.bindviewholders;

import android.content.Context;

import com.eyresapps.data.Crimes;
import com.eyresapps.data.viewholders.CrimeViewHolderNewYork;
import com.eyresapps.utils.CapitalizeString;
import com.eyresapps.utils.DateParserUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by thomaseyre on 14/07/2018.
 */

public class BindViewHolderNewYork {
    public void bind(CrimeViewHolderNewYork crimeViewHolder, int i, ArrayList<Crimes> crimes, Context context) {
        crimeViewHolder.crime.setText(new CapitalizeString().getString(crimes.get(i).getCrimeType()));
        try {
            Date date = new DateParserUtil().getParser().parse(crimes.get(i).getDateOccur());
            String formattedDate = new DateParserUtil().getDateTimeFormat().format(date);
            crimeViewHolder.dateOccur.setText(formattedDate.substring(0,formattedDate.length()-9));
            if(!crimes.get(i).getDateReport().equalsIgnoreCase("")) {
                date = new DateParserUtil().getParser().parse(crimes.get(i).getDateReport());
                formattedDate = new DateParserUtil().getDateTimeFormat().format(date);
                crimeViewHolder.dateReport.setText(formattedDate.substring(0, formattedDate.length() - 9));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        crimeViewHolder.timeOccur.setText(crimes.get(i).getTimeOccur());//.substring(0,2) + ":" + crimes.get(i).getTimeOccur().substring(2,crimes.get(i).getTimeOccur().length()));
        if(!crimes.get(i).getTimeReport().equalsIgnoreCase("")) {
            crimeViewHolder.timeReport.setText(crimes.get(i).getTimeReport());//.substring(0,2) + ":" + crimes.get(i).getTimeReport().substring(2,crimes.get(i).getTimeReport().length()));
        }
        String description = new CapitalizeString().justCapitalize(crimes.get(i).getDescription());
        crimeViewHolder.description.setText(description);
        crimeViewHolder.mCardView.setTag(i);
    }
}
