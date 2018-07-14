package com.eyresapps.data.viewholders;

import android.view.View;
import android.widget.TextView;

import com.eyresapps.crimetracker.R;

/**
 * Created by thomaseyre on 14/07/2018.
 */

public class CrimeViewHolderNewOrleans extends CrimeViewHolder {

    public TextView dateReport;

    public CrimeViewHolderNewOrleans(View itemView) {
        super(itemView);
        dateReport = itemView.findViewById(R.id.dateReport);

    }
}
