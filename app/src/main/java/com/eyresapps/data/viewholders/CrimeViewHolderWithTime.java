package com.eyresapps.data.viewholders;

import android.view.View;
import android.widget.TextView;

import com.eyresapps.crimetracker.R;

/**
 * Created by thomaseyre on 14/07/2018.
 */

public class CrimeViewHolderWithTime extends CrimeViewHolder {

    public TextView time;

    public CrimeViewHolderWithTime(View itemView) {
        super(itemView);
        time = itemView.findViewById(R.id.time);

    }
}
