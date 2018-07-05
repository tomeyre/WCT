package eyresapps.com.wct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import eyresapps.com.adapter.RVAdapterCrimes;
import eyresapps.com.data.Crimes;
import eyresapps.com.utils.CapitalizeString;

/**
 * Created by thomaseyre on 14/03/2018.
 */

public class AdditionalInformation extends AppCompatActivity {

    //----------------used to generate the crimes and add them to the recycler view on screen
    private RVAdapterCrimes rvAdapterCrimes;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TextView streetName;

    private AdView mAdView;
    private AdRequest adRequest;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        ArrayList<Crimes> crimeList = (ArrayList<Crimes>) extras.getSerializable("crimes");
        setContentView(R.layout.additional_information_layout);
        //---- ad view
        mAdView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if(null != crimeList && !crimeList.isEmpty()) {
            streetName = findViewById(R.id.streetName);
//            SpannableString content = new SpannableString(new CapitalizeString().getString(crimeList.get(0).getStreetName()));
//            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            streetName.setText(new CapitalizeString().getString(crimeList.get(0).getStreetName()));
            recyclerView = findViewById(R.id.crimesRv);
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            rvAdapterCrimes = new RVAdapterCrimes(crimeList, this);
            recyclerView.setAdapter(rvAdapterCrimes);
        }
    }
}
