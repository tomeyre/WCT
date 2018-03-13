package eyresapps.com.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import eyresapps.com.data.Counter;
import eyresapps.com.data.CrimeCount;
import eyresapps.com.wct.MainActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by thomaseyre on 27/02/2018.
 */

public class CrimeCountList {

    private Context context;
    private ArrayList<TextView> textViews;

    public CrimeCountList(Context context) {
        this.context = context;
    }

    CrimeCount crimeCount = CrimeCount.getInstance();

    public void sortCrimesCount(ArrayList<Counter> counts, boolean total) {
        ArrayList<Counter> temp = new ArrayList<>();

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


        if(total){
            crimeCount.setTotalCountList(temp);
            setCrimeCountList(crimeCount.getTotalCountList(), total);
        }else {
            crimeCount.setStreetCountList(temp);
            setCrimeCountList(crimeCount.getStreetCountList(), total);
        }

    }


    public void setCrimeCountList(ArrayList<Counter> counter, boolean totals) {
        textViews = ((MainActivity) context).getTextViews(totals);

        for (int i = 0; i < textViews.size(); i++) {
            if (i < counter.size()) {
                textViews.get(i).setVisibility(VISIBLE);
                textViews.get(i).setText(new CrimeTypeString().setString(counter.get(i).getName()) + ": " + counter.get(i).getCount());
            } else {
                textViews.get(i).setVisibility(GONE);
            }
        }
    }

}
