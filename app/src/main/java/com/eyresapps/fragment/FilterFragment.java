package com.eyresapps.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.eyresapps.data.FilterItem;
import com.eyresapps.utils.FilterList;
import com.eyresapps.crimetracker.R;

public class FilterFragment extends Fragment {

    private FilterList filterList = FilterList.getInstance();

    private ArrayList<FilterItem> tempFilterList;

    private CardView filterOne;
    private CardView filterTwo;
    private CardView filterThree;
    private CardView filterFour;
    private CardView filterFive;
    private CardView filterSix;
    private CardView filterSeven;
    private CardView filterEight;
    private CardView filterNine;
    private CardView filterTen;
    private CardView filterEleven;
    private CardView filterTwelve;
    private CardView filterThirteen;
    private CardView filterFourteen;
    private CardView filterFifteen;
    private CardView filterSixteen;
    private CardView filterSeventeen;
    private CardView filterEighteen;
    private CardView filterNineteen;
    private CardView filterTwenty;
    private CardView filterTwentyOne;
    private CardView filterTwentyTwo;
    private CardView filterTwentyThree;
    private CardView filterTwentyFour;
    private TextView filterOneTxt;
    private TextView filterTwoTxt;
    private TextView filterThreeTxt;
    private TextView filterFourTxt;
    private TextView filterFiveTxt;
    private TextView filterSixTxt;
    private TextView filterSevenTxt;
    private TextView filterEightTxt;
    private TextView filterNineTxt;
    private TextView filterTenTxt;
    private TextView filterElevenTxt;
    private TextView filterTwelveTxt;
    private TextView filterThirteenTxt;
    private TextView filterFourteenTxt;
    private TextView filterFifteenTxt;
    private TextView filterSixteenTxt;
    private TextView filterSeventeenTxt;
    private TextView filterEighteenTxt;
    private TextView filterNineteenTxt;
    private TextView filterTwentyTxt;
    private TextView filterTwentyOneTxt;
    private TextView filterTwentyTwoTxt;
    private TextView filterTwentyThreeTxt;
    private TextView filterTwentyFourTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_layout, container, false);

        //-----------filter card view
        filterOne = view.findViewById(R.id.filterOne);
        filterTwo = view.findViewById(R.id.filterTwo);
        filterThree = view.findViewById(R.id.filterThree);
        filterFour = view.findViewById(R.id.filterFour);
        filterFive = view.findViewById(R.id.filterFive);
        filterSix = view.findViewById(R.id.filterSix);
        filterSeven = view.findViewById(R.id.filterSeven);
        filterEight = view.findViewById(R.id.filterEight);
        filterNine = view.findViewById(R.id.filterNine);
        filterTen = view.findViewById(R.id.filterTen);
        filterEleven = view.findViewById(R.id.filterEleven);
        filterTwelve = view.findViewById(R.id.filterTwelve);
        filterThirteen = view.findViewById(R.id.filterThirteen);
        filterFourteen = view.findViewById(R.id.filterFourteen);
        filterFifteen = view.findViewById(R.id.filterFifteen);
        filterSixteen = view.findViewById(R.id.filterSixteen);
        filterSeventeen = view.findViewById(R.id.filterSeventeen);
        filterEighteen = view.findViewById(R.id.filterEighteen);
        filterNineteen = view.findViewById(R.id.filterNineteen);
        filterTwenty = view.findViewById(R.id.filterTwenty);
        filterTwentyOne = view.findViewById(R.id.filterTwentyOne);
        filterTwentyTwo = view.findViewById(R.id.filterTwentyTwo);
        filterTwentyThree = view.findViewById(R.id.filterTwentyThree);
        filterTwentyFour = view.findViewById(R.id.filterTwentyFour);

        //-----filter text view
        filterOneTxt = view.findViewById(R.id.filterOneTxt);
        filterTwoTxt = view.findViewById(R.id.filterTwoTxt);
        filterThreeTxt = view.findViewById(R.id.filterThreeTxt);
        filterFourTxt = view.findViewById(R.id.filterFourTxt);
        filterFiveTxt = view.findViewById(R.id.filterFiveTxt);
        filterSixTxt = view.findViewById(R.id.filterSixTxt);
        filterSevenTxt = view.findViewById(R.id.filterSevenTxt);
        filterEightTxt = view.findViewById(R.id.filterEightTxt);
        filterNineTxt = view.findViewById(R.id.filterNineTxt);
        filterTenTxt = view.findViewById(R.id.filterTenTxt);
        filterElevenTxt = view.findViewById(R.id.filterElevenTxt);
        filterTwelveTxt = view.findViewById(R.id.filterTwelveTxt);
        filterThirteenTxt = view.findViewById(R.id.filterThirteenTxt);
        filterFourteenTxt = view.findViewById(R.id.filterFourteenTxt);
        filterFifteenTxt = view.findViewById(R.id.filterFifteenTxt);
        filterSixteenTxt = view.findViewById(R.id.filterSixteenTxt);
        filterSeventeenTxt = view.findViewById(R.id.filterSeventeenTxt);
        filterEighteenTxt = view.findViewById(R.id.filterEighteenTxt);
        filterNineteenTxt = view.findViewById(R.id.filterNineteenTxt);
        filterTwentyTxt = view.findViewById(R.id.filterTwentyTxt);
        filterTwentyOneTxt = view.findViewById(R.id.filterTwentyOneTxt);
        filterTwentyTwoTxt = view.findViewById(R.id.filterTwentyTwoTxt);
        filterTwentyThreeTxt = view.findViewById(R.id.filterTwentyThreeTxt);
        filterTwentyFourTxt = view.findViewById(R.id.filterTwentyFourTxt);

        tempFilterList = new ArrayList<>();
        tempFilterList.add(new FilterItem(filterOne, filterOneTxt,"",true));
        tempFilterList.add(new FilterItem(filterTwo,filterTwoTxt,"", true));
        tempFilterList.add(new FilterItem(filterThree,filterThreeTxt,"", true));
        tempFilterList.add(new FilterItem(filterFour,filterFourTxt,"", true));
        tempFilterList.add(new FilterItem(filterFive,filterFiveTxt,"", true));
        tempFilterList.add(new FilterItem(filterSix,filterSixTxt,"", true));
        tempFilterList.add(new FilterItem(filterSeven,filterSevenTxt,"", true));
        tempFilterList.add(new FilterItem(filterEight,filterEightTxt,"", true));
        tempFilterList.add(new FilterItem(filterNine,filterNineTxt,"", true));
        tempFilterList.add(new FilterItem(filterTen,filterTenTxt,"", true));
        tempFilterList.add(new FilterItem(filterEleven,filterElevenTxt,"", true));
        tempFilterList.add(new FilterItem(filterTwelve,filterTwelveTxt,"", true));
        tempFilterList.add(new FilterItem(filterThirteen,filterThirteenTxt,"", true));
        tempFilterList.add(new FilterItem(filterFourteen,filterFourteenTxt,"", true));
        tempFilterList.add(new FilterItem(filterFifteen,filterFifteenTxt,"", true));
        tempFilterList.add(new FilterItem(filterSixteen,filterSixteenTxt,"", true));
        tempFilterList.add(new FilterItem(filterSeventeen,filterSeventeenTxt,"", true));
        tempFilterList.add(new FilterItem(filterEighteen,filterEighteenTxt,"", true));
        tempFilterList.add(new FilterItem(filterNineteen,filterNineteenTxt,"", true));
        tempFilterList.add(new FilterItem(filterTwenty,filterTwentyTxt,"", true));
        tempFilterList.add(new FilterItem(filterTwentyOne,filterTwentyOneTxt,"", true));
        tempFilterList.add(new FilterItem(filterTwentyTwo,filterTwentyTwoTxt,"", true));
        tempFilterList.add(new FilterItem(filterTwentyThree,filterTwentyThreeTxt,"", true));
        tempFilterList.add(new FilterItem(filterTwentyFour,filterTwentyFourTxt,"", true));

        filterList.setFilterList(tempFilterList);

        return view;
    }
}