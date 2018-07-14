package com.eyresapps.bindviewholders;

import android.content.Context;
import android.view.View;

import com.eyresapps.data.Crimes;
import com.eyresapps.data.viewholders.CrimeViewHolderLa;
import com.eyresapps.utils.CapitalizeString;
import com.eyresapps.utils.DateParserUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by thomaseyre on 14/07/2018.
 */

public class BindViewHolderLa {
    public void bind(CrimeViewHolderLa crimeViewHolder, int i, ArrayList<Crimes> crimes, Context context) {
        crimeViewHolder.crime.setText(new CapitalizeString().getString(crimes.get(i).getCrimeType()));
        String outcomeText = crimes.get(i).getOutcome();
        crimeViewHolder.outcome.setText(new CapitalizeString().getString(outcomeText.toLowerCase()));
        try {
            Date date = new DateParserUtil().getParser().parse(crimes.get(i).getDateOccur());
            String formattedDate = new DateParserUtil().getDateTimeFormat().format(date);
            crimeViewHolder.dateOccur.setText(formattedDate.substring(0,formattedDate.length()-9));
            date = new DateParserUtil().getParser().parse(crimes.get(i).getDateReport());
            formattedDate = new DateParserUtil().getDateTimeFormat().format(date);
            crimeViewHolder.dateReport.setText(formattedDate.substring(0,formattedDate.length()-9));

        } catch (Exception e) {
            e.printStackTrace();
        }
        crimeViewHolder.time.setText(crimes.get(i).getTimeOccur().substring(0,2) + ":" + crimes.get(i).getTimeOccur().substring(2,crimes.get(i).getTimeOccur().length()));
        if(!crimes.get(i).getWeapon().equalsIgnoreCase("")){
            crimeViewHolder.weapon.setText(crimes.get(i).getWeapon());
        }else{
            crimeViewHolder.weapon.setVisibility(View.GONE);
            crimeViewHolder.weaponTitle.setVisibility(View.GONE);
        }
        String description = new CapitalizeString().justCapitalize(crimes.get(i).getDescription());
        crimeViewHolder.description.setText(description);
        crimeViewHolder.mCardView.setTag(i);
    }
}
