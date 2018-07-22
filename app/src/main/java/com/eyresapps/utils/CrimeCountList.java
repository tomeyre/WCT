package com.eyresapps.utils;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.eyresapps.crimetracker.MainActivity;
import com.eyresapps.crimetracker.YearStats;
import com.eyresapps.data.Counter;
import com.eyresapps.data.CrimeCount;
import com.eyresapps.data.Crimes;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by thomaseyre on 27/02/2018.
 */

public class CrimeCountList {

    private ArrayList<TextView> textViews;

    CrimeNumbers crimeNumbers = CrimeNumbers.getInstance();
    CrimeNumberForYear crimeNumberForYear = CrimeNumberForYear.getInstance();

    public CrimeCountList(Context context) {

    }

    CrimeCount crimeCount = CrimeCount.getInstance();

    public void sortCrimesCountStreet(ArrayList<Counter> counts, boolean total, boolean filtering, final Context context, boolean mainActivity) {
        final ArrayList<Counter> temp = new ArrayList<>();

        for(int i = 0; i < counts.size(); i++) {
            if (temp.isEmpty()) {
                temp.add(counts.get(i));
                continue;
            }
            for(int j = 0; j < temp.size(); j++) {
                if (counts.get(i).getCount() > temp.get(j).getCount()) {
                    temp.add(j, counts.get(i));
                    break;
                } else if (temp.size() - 1 == j) {
                    temp.add(counts.get(i));
                    break;
                }
            }
        }


        if(total && !filtering){
            crimeCount.setTotalCountList(temp);
            setCrimeCountList(crimeCount.getTotalCountList(), total, context,mainActivity);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity)context).setFilterViews(temp);
                }
            });
        }else {
            crimeCount.setStreetCountList(temp);
            setCrimeCountList(crimeCount.getStreetCountList(), total, context,mainActivity);
        }

    }

    public void sortCrimesCount(ArrayList<ArrayList<Crimes>> crimeList, boolean total, boolean filtering, final Context context, boolean mainActivity) {
        ArrayList<Counter> counts = new ArrayList<>();
        if (crimeList != null && !crimeList.isEmpty()) {
            for (int i = 0; i < crimeList.size(); i++) {
                for (int j = 0; j < crimeList.get(i).size(); j++) {

                    if (counts.isEmpty()) {
                        counts.add(new Counter(crimeList.get(i).get(j).getCrimeType(), 1));
                        continue;
                    }
                    for (int k = 0; k < counts.size(); k++) {
                        if (counts.get(k).getName().equalsIgnoreCase(crimeList.get(i).get(j).getCrimeType())) {
                            int temp = counts.get(k).getCount();
                            counts.set(k, new Counter(crimeList.get(i).get(j).getCrimeType(), ++temp));
                            break;
                        }
                        if (k == counts.size() - 1) {
                            counts.add(new Counter(crimeList.get(i).get(j).getCrimeType(), 1));
                            break;
                        }
                    }
                }
            }
        }
        final ArrayList<Counter> temp = new ArrayList<>();

        for(int i = 0; i < counts.size(); i++) {
            if (temp.isEmpty()) {
                temp.add(counts.get(i));
                continue;
            }
            for(int j = 0; j < temp.size(); j++) {
                if (counts.get(i).getCount() > temp.get(j).getCount()) {
                    temp.add(j, counts.get(i));
                    break;
                } else if (temp.size() - 1 == j) {
                    temp.add(counts.get(i));
                    break;
                }
            }
        }


        if(total && !filtering){
            crimeCount.setTotalCountList(temp);
            setCrimeCountList(crimeCount.getTotalCountList(), total, context,mainActivity);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity)context).setFilterViews(temp);
                }
            });
        }else {
            crimeCount.setStreetCountList(temp);
            setCrimeCountList(crimeCount.getStreetCountList(), total, context,mainActivity);
        }

    }


    public void setCrimeCountList(final ArrayList<Counter> counter, final boolean totals, final Context context, boolean mainActivity) {
        if(mainActivity) {
            ((MainActivity) context).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    textViews = crimeNumbers.getTextViews(totals);

                    for (int i = 0; i < textViews.size(); i++) {
                        if (i < counter.size()) {
                            textViews.get(i).setVisibility(VISIBLE);
                            String text = new CapitalizeString().getString(counter.get(i).getName()) + ": <b>" + counter.get(i).getCount() + "</b>";
                            textViews.get(i).setText(Html.fromHtml(text));
                        } else {
                            textViews.get(i).setVisibility(GONE);
                        }
                    }
                }
            });
        }else{
            ((YearStats) context).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    ArrayList<CardView> colorViews = crimeNumberForYear.getStreetColors();
                    textViews = crimeNumberForYear.getTextViews();
                    Colors color = Colors.getInstance();
                    ArrayList<Integer> colors = color.getColors(context);

                    for (int i = 0; i < textViews.size(); i++) {
                        if (i < counter.size() && !counter.get(i).getName().equalsIgnoreCase("none")) {
                            textViews.get(i).setVisibility(VISIBLE);
                            String text = new CapitalizeString().getString(counter.get(i).getName()) + ": <b>" + counter.get(i).getCount() + "</b>";
                            textViews.get(i).setText(Html.fromHtml(text));
                            colorViews.get(i).setVisibility(VISIBLE);
                            colorViews.get(i).setCardBackgroundColor(colors.get(i));
                        } else {
                            textViews.get(i).setVisibility(GONE);
                            colorViews.get(i).setVisibility(View.GONE);
                        }
                    }
                }
            });
        }
    }

}
