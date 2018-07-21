package com.eyresapps.crimetracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.eyresapps.crimes.yearlycrimes.GetUKCrimeByYear;
import com.eyresapps.data.Counter;
import com.eyresapps.data.CrimeCount;
import com.eyresapps.data.Crimes;
import com.eyresapps.data.MonthCountList;
import com.eyresapps.data.MonthCounter;
import com.eyresapps.utils.Colors;
import com.eyresapps.utils.CrimeCountList;
import com.eyresapps.utils.DateParserUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.eyresapps.utils.ScreenUtils.convertDpToPixel;

/**
 * Created by thomaseyre on 19/07/2018.
 */

public class YearStats extends AppCompatActivity {

    private String id;
    private ArrayList<Counter> counts;
    private MonthCountList monthCountList;
    private ArrayList<Integer> colors;
    private LinearLayout llProgressBar;
    private LinearLayout llBarChart;
    private Colors color = Colors.getInstance();
    CrimeCount crimeCount = CrimeCount.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");
        setContentView(R.layout.yearly_crime_stats);

        //monthCounts = new ArrayList<>();


        colors = color.getColors(this);

        llProgressBar = findViewById(R.id.llProgressBar);
        llBarChart = findViewById(R.id.llBarChart);

        new GetUKCrimeByYear(this, id).execute();
    }

    public void customProgressBar(ArrayList<Counter> counters) {
        float base = 0;
        for(int i = 0; i < counters.size(); i++) {
            base += counters.get(i).getCount();
        }
        base = 300 / base;
        for(int i = 0; i < counters.size(); i++) {
            CardView cardView = new CardView(this);
            int size = (int) convertDpToPixel(counters.get(i).getCount() * base, YearStats.this);
            ViewGroup.LayoutParams layoutParams = new CardView.LayoutParams(size,
                    (int) convertDpToPixel(50, YearStats.this));
            cardView.setBackgroundColor(colors.get(i));
            cardView.setLayoutParams(layoutParams);
            llProgressBar.addView(cardView);
        }
    }

    public void customBarChart(MonthCountList monthCountList) {
        float base = 0;
        for(int i = 0; i < 12; i++) {
            if(monthCountList.getMonth(i).size() > base)
            base = monthCountList.getMonth(i).size();
        }
        base = 200 / base;
        for(int i = 0; i < 12; i++) {
            CardView cardView = new CardView(this);
            int size = (int) convertDpToPixel(monthCountList.getMonth(i).size() * base, YearStats.this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) convertDpToPixel(25, YearStats.this),
                    size);
            if(i == 0 || i % 2 == 0) {
                cardView.setBackgroundColor(getResources().getColor(R.color.blue));
            }else{
                cardView.setBackgroundColor(getResources().getColor(R.color.link));
            }
            cardView.setLayoutParams(layoutParams);
            llBarChart.addView(cardView);
        }
    }

    public void setCrimes(ArrayList<Crimes> crimes) {
        counts = new ArrayList<>();
        monthCountList = new MonthCountList();
        Integer month = 0;
        for (int i = 0; i < crimes.size(); i++) {
            try {
                Date date = new DateParserUtil().getParser().parse(crimes.get(i).getDateOccur());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                month = cal.get(Calendar.MONTH);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (counts.isEmpty()) {
                counts.add(new Counter(crimes.get(i).getCrimeType(), 1));
                ArrayList<MonthCounter> innerList = monthCountList.getMonth(month);
                innerList.add(new MonthCounter(crimes.get(i).getCrimeType(), 1, month));
                monthCountList.updateMonth(innerList, month);
                continue;
            }
            for (int k = 0; k < counts.size(); k++) {
                if (counts.get(k).getName().equalsIgnoreCase(crimes.get(i).getCrimeType())) {
                    int temp = counts.get(k).getCount();
                    counts.set(k, new Counter(crimes.get(i).getCrimeType(), ++temp));
                    ArrayList<MonthCounter> innerList = monthCountList.getMonth(month);
                    innerList.add(new MonthCounter(crimes.get(i).getCrimeType(), 1, month));
                    monthCountList.updateMonth(innerList, month);
                    break;
                }
                if (k == counts.size() - 1) {
                    counts.add(new Counter(crimes.get(i).getCrimeType(), 1));
                    ArrayList<MonthCounter> innerList = monthCountList.getMonth(month);
                    innerList.add(new MonthCounter(crimes.get(i).getCrimeType(), 1, month));
                    monthCountList.updateMonth(innerList, month);
                    break;
                }
            }
        }
        new CrimeCountList(this).sortCrimesCountStreet(counts, false, false, this, false);
        customProgressBar(crimeCount.getStreetCountList());
        customBarChart(monthCountList);
    }

}