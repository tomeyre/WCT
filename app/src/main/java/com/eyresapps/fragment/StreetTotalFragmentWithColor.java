package com.eyresapps.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eyresapps.crimetracker.R;
import com.eyresapps.utils.CrimeNumberForYear;

import java.util.ArrayList;

public class StreetTotalFragmentWithColor extends Fragment {

    CrimeNumberForYear crimeNumbers = CrimeNumberForYear.getInstance();

    private TextView one;
    private TextView two;
    private TextView three;
    private TextView four;
    private TextView five;
    private TextView six;
    private TextView seven;
    private TextView eight;
    private TextView nine;
    private TextView ten;
    private TextView eleven;
    private TextView twelve;
    private TextView thirteen;
    private TextView fourteen;
    private TextView fifteen;
    private TextView sixteen;
    private TextView seventeen;
    private TextView eighteen;
    private TextView nineteen;
    private TextView twenty;
    private TextView twentyOne;
    private TextView twentyTwo;
    private TextView twentyThree;
    private TextView twentyFour;
    private TextView twentyFive;
    private TextView twentySix;
    private TextView twentySeven;
    private TextView twentyEight;
    private TextView twentyNine;
    private TextView thirty;

    private CardView colorOne;
    private CardView colorTwo;
    private CardView colorThree;
    private CardView colorFour;
    private CardView colorFive;
    private CardView colorSix;
    private CardView colorSeven;
    private CardView colorEight;
    private CardView colorNine;
    private CardView colorTen;
    private CardView colorEleven;
    private CardView colorTwelve;
    private CardView colorThirteen;
    private CardView colorFourteen;
    private CardView colorFifteen;
    private CardView colorSixteen;
    private CardView colorSeventeen;
    private CardView colorEighteen;
    private CardView colorNineteen;
    private CardView colorTwenty;
    private CardView colorTwentyOne;
    private CardView colorTwentyTwo;
    private CardView colorTwentyThree;
    private CardView colorTwentyFour;
    private CardView colorTwentyFive;
    private CardView colorTwentySix;
    private CardView colorTwentySeven;
    private CardView colorTwentyEight;
    private CardView colorTwentyNine;
    private CardView colorThirty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.street_totals_layout_with_colors, container, false);

        ArrayList<TextView> views = new ArrayList<>();
        ArrayList<CardView> colors = new ArrayList<>();

        //-------streeet crime views
        one = view.findViewById(R.id.one);
        two = view.findViewById(R.id.two);
        three = view.findViewById(R.id.three);
        four = view.findViewById(R.id.four);
        five = view.findViewById(R.id.five);
        six = view.findViewById(R.id.six);
        seven = view.findViewById(R.id.seven);
        eight = view.findViewById(R.id.eight);
        nine = view.findViewById(R.id.nine);
        ten = view.findViewById(R.id.ten);
        eleven = view.findViewById(R.id.eleven);
        twelve = view.findViewById(R.id.twelve);
        thirteen = view.findViewById(R.id.thirteen);
        fourteen = view.findViewById(R.id.fourteen);
        fifteen = view.findViewById(R.id.fifteen);
        sixteen = view.findViewById(R.id.sixteen);
        seventeen = view.findViewById(R.id.seventeen);
        eighteen = view.findViewById(R.id.eighteen);
        nineteen = view.findViewById(R.id.nineteen);
        twenty = view.findViewById(R.id.twenty);
        twentyOne = view.findViewById(R.id.twentyOne);
        twentyTwo = view.findViewById(R.id.twentyTwo);
        twentyThree = view.findViewById(R.id.twentyThree);
        twentyFour = view.findViewById(R.id.twentyFour);
        twentyFive = view.findViewById(R.id.twentyFive);
        twentySix = view.findViewById(R.id.twentySix);
        twentySeven = view.findViewById(R.id.twentySeven);
        twentyEight = view.findViewById(R.id.twentyEight);
        twentyNine = view.findViewById(R.id.twentyNine);
        thirty = view.findViewById(R.id.thirty);

        colorOne = view.findViewById(R.id.colorOne);
        colorTwo = view.findViewById(R.id.colorTwo);
        colorThree = view.findViewById(R.id.colorThree);
        colorFour = view.findViewById(R.id.colorFour);
        colorFive = view.findViewById(R.id.colorFive);
        colorSix = view.findViewById(R.id.colorSix);
        colorSeven = view.findViewById(R.id.colorSeven);
        colorEight = view.findViewById(R.id.colorEight);
        colorNine = view.findViewById(R.id.colorNine);
        colorTen = view.findViewById(R.id.colorTen);
        colorEleven = view.findViewById(R.id.colorEleven);
        colorTwelve = view.findViewById(R.id.colorTwelve);
        colorThirteen = view.findViewById(R.id.colorThirteen);
        colorFourteen = view.findViewById(R.id.colorFourteen);
        colorFifteen = view.findViewById(R.id.colorFifteen);
        colorSixteen = view.findViewById(R.id.colorSixteen);
        colorSeventeen = view.findViewById(R.id.colorSeventeen);
        colorEighteen = view.findViewById(R.id.colorEighteen);
        colorNineteen = view.findViewById(R.id.colorNineteen);
        colorTwenty = view.findViewById(R.id.colorTwenty);
        colorTwentyOne = view.findViewById(R.id.colorTwentyOne);
        colorTwentyTwo = view.findViewById(R.id.colorTwentyTwo);
        colorTwentyThree = view.findViewById(R.id.colorTwentyThree);
        colorTwentyFour = view.findViewById(R.id.colorTwentyFour);
        colorTwentyFive = view.findViewById(R.id.colorTwentyFive);
        colorTwentySix = view.findViewById(R.id.colorTwentySix);
        colorTwentySeven = view.findViewById(R.id.colorTwentySeven);
        colorTwentyEight = view.findViewById(R.id.colorTwentyEight);
        colorTwentyNine = view.findViewById(R.id.colorTwentyNine);
        colorThirty = view.findViewById(R.id.colorThirty);

        views.add(one);
        views.add(two);
        views.add(three);
        views.add(four);
        views.add(five);
        views.add(six);
        views.add(seven);
        views.add(eight);
        views.add(nine);
        views.add(ten);
        views.add(eleven);
        views.add(twelve);
        views.add(thirteen);
        views.add(fourteen);
        views.add(fifteen);
        views.add(sixteen);
        views.add(seventeen);
        views.add(eighteen);
        views.add(nineteen);
        views.add(twenty);
        views.add(twentyOne);
        views.add(twentyTwo);
        views.add(twentyThree);
        views.add(twentyFour);
        views.add(twentyFive);
        views.add(twentySix);
        views.add(twentySeven);
        views.add(twentyEight);
        views.add(twentyNine);
        views.add(thirty);

        colors.add(colorOne);
        colors.add(colorTwo);
        colors.add(colorThree);
        colors.add(colorFour);
        colors.add(colorFive);
        colors.add(colorSix);
        colors.add(colorSeven);
        colors.add(colorEight);
        colors.add(colorNine);
        colors.add(colorTen);
        colors.add(colorEleven);
        colors.add(colorTwelve);
        colors.add(colorThirteen);
        colors.add(colorFourteen);
        colors.add(colorFifteen);
        colors.add(colorSixteen);
        colors.add(colorSeventeen);
        colors.add(colorEighteen);
        colors.add(colorNineteen);
        colors.add(colorTwenty);
        colors.add(colorTwentyOne);
        colors.add(colorTwentyTwo);
        colors.add(colorTwentyThree);
        colors.add(colorTwentyFour);
        colors.add(colorTwentyFive);
        colors.add(colorTwentySix);
        colors.add(colorTwentySeven);
        colors.add(colorTwentyEight);
        colors.add(colorTwentyNine);
        colors.add(colorThirty);

        crimeNumbers.setStreetTextViews(views);
        crimeNumbers.setStreetColors(colors);

        return view;
    }
}
