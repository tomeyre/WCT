package eyresapps.com.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import eyresapps.com.data.Crimes;
import eyresapps.com.utils.CapitalizeString;
import eyresapps.com.utils.DateParserUtil;
import eyresapps.com.wct.R;

public class RVAdapterCrimes extends RecyclerView.Adapter<RVAdapterCrimes.CrimeViewHolder> {

    public static class CrimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CardView mCardView;
        TextView crime;
        TextView outcome;
        TextView weapon;
        TextView descriptionTitle;
        TextView description;
        TextView date;
        TextView time;


        CrimeViewHolder(View itemView) {
            super(itemView);
            crime = itemView.findViewById(R.id.crime);
            outcome = itemView.findViewById(R.id.outcome);
            weapon = itemView.findViewById(R.id.weapon);
            description = itemView.findViewById(R.id.description);
            descriptionTitle = itemView.findViewById(R.id.descriptionTitle);
            mCardView = itemView.findViewById(R.id.cardViewCrime);
            descriptionTitle = itemView.findViewById(R.id.descriptionTitle);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
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
        crimeViewHolder.crime.setText(new CapitalizeString().getString(crimes.get(i).getCrimeType()));

        String outcomeText = crimes.get(i).getOutcome();
        if(outcomeText.contains(";")){outcomeText = outcomeText.replace(";","");}
        crimeViewHolder.outcome.setText(new CapitalizeString().getString(outcomeText.toLowerCase()));

        if(crimes.get(i).getWeapon().equals("")){
            crimeViewHolder.weapon.setVisibility(View.GONE);
        }else {
            crimeViewHolder.weapon.setText(Html.fromHtml("<b>Weapon: </b>" + new CapitalizeString().getString(crimes.get(i).getWeapon()), Html.FROM_HTML_MODE_LEGACY));
        }
        if(crimes.get(i).getDescription().equals("")){
            crimeViewHolder.description.setVisibility(View.GONE);
            crimeViewHolder.descriptionTitle.setVisibility(View.GONE);
        }else {
            crimeViewHolder.description.setText(crimes.get(i).getDescription().replaceAll("\\<.*?\\>", ""));
            crimeViewHolder.descriptionTitle.setVisibility(View.VISIBLE);
        }
        if("" != crimes.get(i).getDate()){
            try {
               Date date = new DateParserUtil().getParser().parse(crimes.get(i).getDate());
                String formattedDate = new DateParserUtil().getFormat().format(date);
                crimeViewHolder.date.setText(formattedDate);
            }catch (Exception e){e.printStackTrace();}
        }else{
            crimeViewHolder.date.setVisibility(View.GONE);
        }
        if("" != crimes.get(i).getTime()){
            try {
                if(crimes.get(i).getTime().length() == 9){
                    String[] splitTime = crimes.get(i).getTime().split(":");
                    String occuredAmPm;
                    String foundAmPm;
                    if(Integer.parseInt(splitTime[0].substring(0,2)) >= 12){
                        occuredAmPm = " PM";
                    }else{
                        occuredAmPm = " AM";
                    }
                    if(Integer.parseInt(splitTime[1].substring(0,2)) >= 12){
                        foundAmPm = " PM";
                    }else{
                        foundAmPm = " AM";
                    }
                    crimeViewHolder.time.setText("Time Crime Occured: " + splitTime[0].substring(0,2) + ":" + splitTime[0].substring(2,4) + occuredAmPm + " \nTime Police Found: " + splitTime[1].substring(0,2) + ":" + splitTime[1].substring(2,4) + foundAmPm);
                }else {
                    Date date = new SimpleDateFormat("HH:mm").parse(crimes.get(i).getTime());
                    crimeViewHolder.time.setText(date.toString());
                }
            }catch (Exception e){e.printStackTrace();}
        }else{
            crimeViewHolder.time.setVisibility(View.GONE);
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