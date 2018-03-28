package eyresapps.com.utils;

import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
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


        if(total){
            crimeCount.setTotalCountList(temp);
            setCrimeCountList(crimeCount.getTotalCountList(), total);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity)context).setFilterViews(temp);
                }
            });
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
                String text = new CapitalizeString().getString(counter.get(i).getName()) + ": <b>" + counter.get(i).getCount()+ "</b>";
                textViews.get(i).setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
            } else {
                textViews.get(i).setVisibility(GONE);
            }
        }
    }

}
