package com.eyresapps.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eyresapps.crimetracker.R;
import com.eyresapps.utils.CrimeNumbers;

import java.util.ArrayList;

public class TotalFragment extends Fragment {

    CrimeNumbers crimeNumbers = CrimeNumbers.getInstance();

    private TextView oneTotal;
    private TextView twoTotal;
    private TextView threeTotal;
    private TextView fourTotal;
    private TextView fiveTotal;
    private TextView sixTotal;
    private TextView sevenTotal;
    private TextView eightTotal;
    private TextView nineTotal;
    private TextView tenTotal;
    private TextView elevenTotal;
    private TextView twelveTotal;
    private TextView thirteenTotal;
    private TextView fourteenTotal;
    private TextView fifteenTotal;
    private TextView sixteenTotal;
    private TextView seventeenTotal;
    private TextView eighteenTotal;
    private TextView nineteenTotal;
    private TextView twentyTotal;
    private TextView twentyOneTotal;
    private TextView twentyTwoTotal;
    private TextView twentyThreeTotal;
    private TextView twentyFourTotal;
    private TextView twentyFiveTotal;
    private TextView twentySixTotal;
    private TextView twentySevenTotal;
    private TextView twentyEightTotal;
    private TextView twentyNineTotal;
    private TextView thirtyTotal;
    private TextView thirtyOneTotal;
    private TextView thirtyTwoTotal;
    private TextView thirtyThreeTotal;
    private TextView thirtyFourTotal;
    private TextView thirtyFiveTotal;
    private TextView thirtySixTotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.totals_layout, container, false);

        ArrayList<TextView> views = new ArrayList<>();

        //-------total crime views
        oneTotal = view.findViewById(R.id.oneTotal);
        twoTotal = view.findViewById(R.id.twoTotal);
        threeTotal = view.findViewById(R.id.threeTotal);
        fourTotal = view.findViewById(R.id.fourTotal);
        fiveTotal = view.findViewById(R.id.fiveTotal);
        sixTotal = view.findViewById(R.id.sixTotal);
        sevenTotal = view.findViewById(R.id.sevenTotal);
        eightTotal = view.findViewById(R.id.eightTotal);
        nineTotal = view.findViewById(R.id.nineTotal);
        tenTotal = view.findViewById(R.id.tenTotal);
        elevenTotal = view.findViewById(R.id.elevenTotal);
        twelveTotal = view.findViewById(R.id.twelveTotal);
        thirteenTotal = view.findViewById(R.id.thirteenTotal);
        fourteenTotal = view.findViewById(R.id.fourteenTotal);
        fifteenTotal = view.findViewById(R.id.fifteenTotal);
        sixteenTotal = view.findViewById(R.id.sixteenTotal);
        seventeenTotal = view.findViewById(R.id.seventeenTotal);
        eighteenTotal = view.findViewById(R.id.eighteenTotal);
        nineteenTotal = view.findViewById(R.id.nineteenTotal);
        twentyTotal = view.findViewById(R.id.twentyTotal);
        twentyOneTotal = view.findViewById(R.id.twentyOneTotal);
        twentyTwoTotal = view.findViewById(R.id.twentyTwoTotal);
        twentyThreeTotal = view.findViewById(R.id.twentyThreeTotal);
        twentyFourTotal = view.findViewById(R.id.twentyFourTotal);
        twentyFiveTotal = view.findViewById(R.id.twentyFiveTotal);
        twentySixTotal = view.findViewById(R.id.twentySixTotal);
        twentySevenTotal = view.findViewById(R.id.twentySevenTotal);
        twentyEightTotal = view.findViewById(R.id.twentyEightTotal);
        twentyNineTotal = view.findViewById(R.id.twentyNineTotal);
        thirtyTotal = view.findViewById(R.id.thirtyTotal);
        thirtyOneTotal = view.findViewById(R.id.thirtyOneTotal);
        thirtyTwoTotal = view.findViewById(R.id.thirtyTwoTotal);
        thirtyThreeTotal = view.findViewById(R.id.thirtyThreeTotal);
        thirtyFourTotal = view.findViewById(R.id.thirtyFourTotal);
        thirtyFiveTotal = view.findViewById(R.id.thirtyFiveTotal);
        thirtySixTotal = view.findViewById(R.id.thirtySixTotal);

        views.add(oneTotal);
        views.add(twoTotal);
        views.add(threeTotal);
        views.add(fourTotal);
        views.add(fiveTotal);
        views.add(sixTotal);
        views.add(sevenTotal);
        views.add(eightTotal);
        views.add(nineTotal);
        views.add(tenTotal);
        views.add(elevenTotal);
        views.add(twelveTotal);
        views.add(thirteenTotal);
        views.add(fourteenTotal);
        views.add(fifteenTotal);
        views.add(sixteenTotal);
        views.add(seventeenTotal);
        views.add(eighteenTotal);
        views.add(nineteenTotal);
        views.add(twentyTotal);
        views.add(twentyOneTotal);
        views.add(twentyTwoTotal);
        views.add(twentyThreeTotal);
        views.add(twentyFourTotal);
        views.add(twentyFiveTotal);
        views.add(twentySixTotal);
        views.add(twentySevenTotal);
        views.add(twentyEightTotal);
        views.add(twentyNineTotal);
        views.add(thirtyTotal);
        views.add(thirtyOneTotal);
        views.add(thirtyTwoTotal);
        views.add(thirtyThreeTotal);
        views.add(thirtyFourTotal);
        views.add(thirtyFiveTotal);
        views.add(thirtySixTotal);

        crimeNumbers.setTotalTextViews(views);

        return view;
    }
}
