package com.eyresapps.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eyresapps.crimetracker.R;
import com.eyresapps.utils.CrimeNumbers;

import java.util.ArrayList;

public class StreetTotalFragmentWithColor extends Fragment {

    CrimeNumbers crimeNumbers = CrimeNumbers.getInstance();

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

        crimeNumbers.setStreetTextViews(views);
        crimeNumbers.setStreetColors(colors);

        return view;
    }
}
