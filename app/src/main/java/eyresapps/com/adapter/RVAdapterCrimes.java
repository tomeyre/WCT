package eyresapps.com.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import eyresapps.com.data.Crimes;
import eyresapps.com.utils.CrimeTypeString;
import eyresapps.com.wct.R;

public class RVAdapterCrimes extends RecyclerView.Adapter<RVAdapterCrimes.CrimeViewHolder> {

    public static class CrimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CardView mCardView;
        TextView crime;
        TextView outcome;
        TextView weapon;
        TextView descriptionTitle;
        TextView description;


        CrimeViewHolder(View itemView) {
            super(itemView);
            crime = (TextView) itemView.findViewById(R.id.crime);
            outcome = (TextView) itemView.findViewById(R.id.outcome);
            weapon = (TextView) itemView.findViewById(R.id.weapon);
            description = (TextView) itemView.findViewById(R.id.description);
            descriptionTitle = itemView.findViewById(R.id.descriptionTitle);
            mCardView = (CardView)itemView.findViewById(R.id.cardViewCrime);
            mCardView.setOnClickListener(this);
        }

        public void onClick(View v) {

        }
    }


    ArrayList<Crimes> crimes;
    Context context;

    public RVAdapterCrimes(ArrayList<Crimes> crimes, Context context)
    {
        this.crimes = crimes;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CrimeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.crime, viewGroup, false);
        return new CrimeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CrimeViewHolder crimeViewHolder, int i) {
        String crimeName = crimes.get(i).getCrimeType().toLowerCase();
        crimeViewHolder.crime.setText(new CrimeTypeString().setString(crimeName));

        String outcomeText = crimes.get(i).getOutcome();
        if(outcomeText.contains(";")){outcomeText = outcomeText.replace(";","");}
        crimeViewHolder.outcome.setText(new CrimeTypeString().setString(outcomeText.toLowerCase()));

        if(crimes.get(i).getWeapon().equals("")){
            crimeViewHolder.weapon.setVisibility(View.GONE);
        }else {
            crimeViewHolder.weapon.setText(crimes.get(i).getWeapon());
        }
        if(crimes.get(i).getDescription().equals("")){
            crimeViewHolder.description.setVisibility(View.GONE);
            crimeViewHolder.descriptionTitle.setVisibility(View.GONE);
        }else {
            crimeViewHolder.description.setText(crimes.get(i).getDescription().replaceAll("\\<.*?\\>", ""));
            crimeViewHolder.descriptionTitle.setVisibility(View.VISIBLE);
        }
        crimeViewHolder.mCardView.setTag(i);
    }

    @Override
    public int getItemCount() {
        if(crimes != null) {
            return crimes.size();
        }
        else return 0;
    }
}