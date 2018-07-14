package com.eyresapps.data.viewholders;

import android.view.View;
import android.widget.TextView;

import com.eyresapps.crimetracker.R;

/**
 * Created by thomaseyre on 14/07/2018.
 */

public class CrimeViewHolderDurham extends CrimeViewHolder {

    public TextView timeOccur;
    public TextView timeReport;
    public TextView weapon;
    public TextView weaponTitle;
    public TextView dateReport;

    public CrimeViewHolderDurham(View itemView) {
        super(itemView);
        timeOccur = itemView.findViewById(R.id.timeOccur);
        timeReport = itemView.findViewById(R.id.timeReport);
        weapon = itemView.findViewById(R.id.weapon);
        weaponTitle = itemView.findViewById(R.id.weaponTitle);
        dateReport = itemView.findViewById(R.id.dateReport);

    }
}
