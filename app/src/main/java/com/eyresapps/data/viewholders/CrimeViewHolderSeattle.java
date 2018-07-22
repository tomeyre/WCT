package com.eyresapps.data.viewholders;

import android.view.View;
import android.widget.TextView;

import com.eyresapps.crimetracker.R;

/**
 * Created by thomaseyre on 14/07/2018.
 */

public class CrimeViewHolderSeattle extends CrimeViewHolder {

    public TextView time;
    public TextView weapon;
    public TextView weaponTitle;
    public TextView dateReport;

    public CrimeViewHolderSeattle(View itemView) {
        super(itemView);
        time = itemView.findViewById(R.id.time);
        weapon = itemView.findViewById(R.id.weapon);
        weaponTitle = itemView.findViewById(R.id.weaponTitle);
        dateReport = itemView.findViewById(R.id.dateReport);

    }
}
