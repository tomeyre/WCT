package com.eyresapps.data.viewholders;

import android.view.View;
import android.widget.TextView;

import com.eyresapps.crimetracker.R;

/**
 * Created by thomaseyre on 14/07/2018.
 */

public class CrimeViewHolderNewYork extends CrimeViewHolder {

    public TextView timeOccur;
    public TextView timeReport;
    public TextView dateReport;

    public CrimeViewHolderNewYork(View itemView) {
        super(itemView);
        timeOccur = itemView.findViewById(R.id.timeOccur);
        timeReport = itemView.findViewById(R.id.timeReport);
        dateReport = itemView.findViewById(R.id.dateReport);

    }
}
