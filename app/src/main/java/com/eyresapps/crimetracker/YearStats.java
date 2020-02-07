package com.eyresapps.crimetracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eyresapps.crimes.yearlycrimes.GetNewYorkCrimeByYear;
import com.eyresapps.crimes.yearlycrimes.GetUKCrimeByYear;
import com.eyresapps.data.Counter;
import com.eyresapps.data.Crimes;
import com.eyresapps.data.MonthCountList;
import com.eyresapps.data.MonthCounter;
import com.eyresapps.utils.Colors;
import com.eyresapps.utils.CrimeCountList;
import com.eyresapps.utils.CrimeNumberForYear;
import com.eyresapps.utils.CurrentAddressUtil;
import com.eyresapps.utils.DateParserUtil;
import com.eyresapps.utils.DateUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.eyresapps.utils.ScreenUtils.convertDpToPixel;

/**
 * Created by thomaseyre on 19/07/2018.
 */

public class YearStats extends AppCompatActivity {

    private Crimes id;
    private ArrayList<Counter> counts = new ArrayList<>();
    private MonthCountList monthCountList;
    private ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<LinearLayout> barChartMonthLayouts = new ArrayList<>();
    //private LinearLayout llProgressBar;
    private LinearLayout llOne;
    private LinearLayout llTwo;
    private LinearLayout llThree;
    private LinearLayout llFour;
    private LinearLayout llFive;
    private LinearLayout llSix;
    private LinearLayout llSeven;
    private LinearLayout llEight;
    private LinearLayout llNine;
    private LinearLayout llTen;
    private LinearLayout llEleven;
    private LinearLayout llTwelve;
    private TextView streetName;
    private Colors color = Colors.getInstance();
    CrimeNumberForYear crimeNumbers = CrimeNumberForYear.getInstance();
    private AdView mAdView;
    private AdRequest adRequest;
    private CurrentAddressUtil currentAddressUtil = CurrentAddressUtil.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        id = (Crimes) extras.getSerializable("id");
        setContentView(R.layout.yearly_crime_stats);

        MobileAds.initialize(getApplicationContext(),
                getResources().getString(R.string.test));
        mAdView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        streetName = findViewById(R.id.streetName);

        colors = color.getColors(this);

        //llProgressBar = findViewById(R.id.llProgressBar);
        llOne = findViewById(R.id.llOne);
        llTwo = findViewById(R.id.llTwo);
        llThree = findViewById(R.id.llThree);
        llFour = findViewById(R.id.llFour);
        llFive = findViewById(R.id.llFive);
        llSix = findViewById(R.id.llSix);
        llSeven = findViewById(R.id.llSeven);
        llEight = findViewById(R.id.llEight);
        llNine = findViewById(R.id.llNine);
        llTen = findViewById(R.id.llTen);
        llEleven = findViewById(R.id.llEleven);
        llTwelve = findViewById(R.id.llTwelve);
        barChartMonthLayouts.add(llOne);
        barChartMonthLayouts.add(llTwo);
        barChartMonthLayouts.add(llThree);
        barChartMonthLayouts.add(llFour);
        barChartMonthLayouts.add(llFive);
        barChartMonthLayouts.add(llSix);
        barChartMonthLayouts.add(llSeven);
        barChartMonthLayouts.add(llEight);
        barChartMonthLayouts.add(llNine);
        barChartMonthLayouts.add(llTen);
        barChartMonthLayouts.add(llEleven);
        barChartMonthLayouts.add(llTwelve);

        if(currentAddressUtil.getAddress().toLowerCase().contains("uk")) {
            new GetUKCrimeByYear(this, id.getId()).execute();
        } else if(currentAddressUtil.getAddress().toLowerCase().contains(", ny") && currentAddressUtil.getAddress().toLowerCase().contains("usa")){
            new GetNewYorkCrimeByYear(this, id).execute();
        }
    }

//    public void customProgressBar(ArrayList<Counter> counters) {
//        float base = 0;
//        for(int i = 0; i < counters.size(); i++) {
//            if(!counters.get(i).getName().equalsIgnoreCase("none")) {
//                base += counters.get(i).getCount();
//            }
//        }
//        base = 300 / base;
//        for(int i = 0; i < counters.size(); i++) {
//            if(!counters.get(i).getName().equalsIgnoreCase("none")) {
//                CardView cardView = new CardView(this);
//                int size = (int) convertDpToPixel(counters.get(i).getCount() * base, YearStats.this);
//                ViewGroup.LayoutParams layoutParams = new CardView.LayoutParams(size,
//                        (int) convertDpToPixel(50, YearStats.this));
//                cardView.setBackgroundColor(colors.get(i));
//                cardView.setLayoutParams(layoutParams);
//                llProgressBar.addView(cardView);
//            }
//        }
//    }

    public void customBarChart(MonthCountList monthCountList) {
        ArrayList<TextView> textViews = crimeNumbers.getTextViews();
        monthCountList.sortByMonth();
        float base = 0;
        ArrayList<MonthCounter> temp;
        for(int i = 0; i < 12; i++) {
            if(monthCountList.getMonth(i).size() > base) {
                base = monthCountList.getMonth(i).size();
            }
        }
        base = 200 / base;
        for(int i = 0; i < 12; i++) {
            temp = sortMonthlyCrimes(monthCountList.getMonth(i));
            TextView textView = new TextView(this);
            RelativeLayout.LayoutParams layoutParamsTv = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if(temp.get(0).getName().equalsIgnoreCase("none")){
                textView.setText("0");
            }else {
                textView.setText("" + temp.size());
            }
            textView.setTextSize(10);
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setLayoutParams(layoutParamsTv);
            barChartMonthLayouts.get(i).addView(textView);
            for(int j = temp.size() - 1; j > -1; j--) {
                for(int k = 0; k < textViews.size(); k++) {
                    String[] split = temp.get(j).getName().split(":");
                    String check = split[0].replace("-"," ");
                    if(check.equalsIgnoreCase("none")){
                        break;
                    }
                    if (textViews.get(k).getText().toString().toLowerCase().trim().contains(check.trim().toLowerCase())) {
                        CardView cardView = new CardView(this);
                        int size = (int) convertDpToPixel(temp.get(j).getCount() * base, YearStats.this);
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) convertDpToPixel(25, YearStats.this),
                                size);
                        layoutParams.setMargins(0,1,0,0);
                        cardView.setBackgroundColor(colors.get(k));
                        cardView.setLayoutParams(layoutParams);
                        barChartMonthLayouts.get(i).addView(cardView);
                        break;
                    }
                }
            }
            textView = new TextView(this);
            layoutParamsTv = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsTv.setMargins(0,2,0,0);
            textView.setText(DateUtil.getInstance().getShortMonthAsString(temp.get(0).getMonth()));
            textView.setTextSize(8);
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setLayoutParams(layoutParamsTv);
            barChartMonthLayouts.get(i).addView(textView);
            textView = new TextView(this);
            layoutParamsTv = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setText("" + temp.get(0).getYear());
            textView.setTextSize(8);
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setLayoutParams(layoutParamsTv);
            barChartMonthLayouts.get(i).addView(textView);
        }
    }

    public ArrayList<MonthCounter> sortMonthlyCrimes(ArrayList<MonthCounter> counts){
        final ArrayList<MonthCounter> temp = new ArrayList<>();

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
        return temp;
    }

    public void setCrimes(ArrayList<Crimes> crimes) {
        for(Crimes crime : crimes) {
            if(!crime.getStreetName().equalsIgnoreCase("")) {
                streetName.setText(crime.getStreetName());
                break;
            }
        }
        counts = new ArrayList<>();
        monthCountList = new MonthCountList();
        Integer month = 0;
        Integer year = 0;
        for (int i = 0; i < crimes.size(); i++) {
            try {
                if(crimes.get(i).getCrimeType().equalsIgnoreCase("none")){
                    month = Integer.parseInt(crimes.get(i).getDateOccur());
                    month--;
                    year = Integer.parseInt(crimes.get(i).getDateReport());
                }else {
                    Date date = new DateParserUtil().getParser().parse(crimes.get(i).getDateOccur());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    month = cal.get(Calendar.MONTH);
                    year = cal.get(Calendar.YEAR);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (counts.isEmpty()) {
                counts.add(new Counter(crimes.get(i).getCrimeType(), 1));
                ArrayList<MonthCounter> innerList = monthCountList.getMonth(month);
                innerList.add(new MonthCounter(crimes.get(i).getCrimeType(), 1, month, year));
                monthCountList.updateMonth(innerList, month);
                continue;
            }
            for (int k = 0; k < counts.size(); k++) {
                if(crimes.get(i).getCrimeType().equalsIgnoreCase("none")){
                    ArrayList<MonthCounter> innerList = monthCountList.getMonth(month);
                    innerList.add(new MonthCounter(crimes.get(i).getCrimeType(), 0, month, year));
                    monthCountList.updateMonth(innerList, month);
                    break;
                } else if (counts.get(k).getName().equalsIgnoreCase(crimes.get(i).getCrimeType())) {
                    int temp = counts.get(k).getCount();
                    counts.set(k, new Counter(crimes.get(i).getCrimeType(), ++temp));
                    ArrayList<MonthCounter> innerList = monthCountList.getMonth(month);
                    innerList.add(new MonthCounter(crimes.get(i).getCrimeType(), 1, month, year));
                    monthCountList.updateMonth(innerList, month);
                    break;
                }
                if (k == counts.size() - 1) {
                    counts.add(new Counter(crimes.get(i).getCrimeType(), 1));
                    ArrayList<MonthCounter> innerList = monthCountList.getMonth(month);
                    innerList.add(new MonthCounter(crimes.get(i).getCrimeType(), 1, month, year));
                    monthCountList.updateMonth(innerList, month);
                    break;
                }
            }
        }
        new CrimeCountList(this).sortCrimesCountStreet(counts, false, false, this, false);
        //customProgressBar(crimeCount.getStreetCountList());
        customBarChart(monthCountList);
    }

}