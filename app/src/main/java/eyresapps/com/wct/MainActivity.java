package eyresapps.com.wct;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.IntentFilter;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import eyresapps.com.adapter.RVAdapterCrimes;
import eyresapps.com.api_calls.GetCurrentWeather;
import eyresapps.com.broadcastRecievers.Network;
import eyresapps.com.broadcastRecievers.NetworkStateReceiver;
import eyresapps.com.crimeLocations.GenerateCrimeUrl;
import eyresapps.com.api_calls.GenerateNeighbourhoodLocation;
import eyresapps.com.data.Counter;
import eyresapps.com.data.CrimeCount;
import eyresapps.com.data.Crimes;
import eyresapps.com.utils.CrimeCountList;
import eyresapps.com.utils.CurrentAddressUtil;
import eyresapps.com.utils.DateUtil;
import eyresapps.com.utils.GPSTrackerUtil;
import eyresapps.com.utils.LatitudeAndLongitudeUtil;

import static android.text.util.Linkify.addLinks;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static eyresapps.com.utils.ScreenUtils.getMeasuredHeight;
import static eyresapps.com.utils.ScreenUtils.getScreenHeight;
import static eyresapps.com.utils.ScreenUtils.getStatusBarHeight;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, NetworkStateReceiver.NetworkStateReceiverListener {

    private int isClickCount = 0;
    private static int adViewHeight;

    private NetworkStateReceiver networkStateReceiver;

    CrimeCount crimeCount = CrimeCount.getInstance();

    //--------------------getting crimes ect dialog display
    private ProgressDialog dialog;

    //-------------------these are the views used for the custom scrolling animation
    private RelativeLayout layoutBody;
    private CardView layoutTitle;

    //-------------------variables used in the custom scroll animation
    private float previousPosition = 0;
    private float startingPosition = 0;
    private float distanceMoved = 0;
    private int statusBarHeight = 0;

    //----------------any google maps stuff
    private GoogleMap mMap;
    private CameraUpdate cameraUpdate;

    //---------- the actual custom scroll view
    private LockableScrollView lockableScrollView;
    private RelativeLayout informationLayout;

    //----------------------any text views used inside the custom scroll view
    private TextView streetName;
    private TextView about;
    private TextView time;
    private TextView aboutTitle;
    private TextView areaTitle;
    private TextView email;
    private TextView website;
    private TextView facebook;
    private TextView twitter;
    private TextView weather;
    private TextView oneTotal;
    private TextView twoTotal;
    private TextView threeTotal;
    private TextView fourTotal;
    private TextView fiveTotal;
    private TextView sixTotal;
    private TextView sevenTotal;
    private TextView eightTotal;
    private TextView nineTotal;
    private TextView tenTotal;
    private TextView elevenTotal;
    private TextView twelveTotal;
    private TextView thirteenTotal;
    private TextView fourteenTotal;
    private TextView one;
    private TextView two;
    private TextView three;
    private TextView four;
    private TextView five;
    private TextView six;
    private TextView seven;
    private TextView eight;
    private TextView nine;
    private TextView ten;
    private TextView eleven;
    private TextView twelve;
    private TextView thirteen;
    private TextView fourteen;
    private TextView areaTotalsTitle;
    private TextView crimesTitle;

    //-----------------objects used to get current latitude and longitude as a singleton
    private LatitudeAndLongitudeUtil latLng = LatitudeAndLongitudeUtil.getInstance();
    private GPSTrackerUtil gpsTracker;
    private LocationManager lm;

    //--------- how i store the crimes gotten from the api's
    private ArrayList<ArrayList<Crimes>> crimeList = new ArrayList<>();

    //--------- get current address as a string
    private CurrentAddressUtil currentAddress = CurrentAddressUtil.getInstance();

    //------------ all used for getting local time to display below the street name like google does
    private DateUtil dateUtil = DateUtil.getInstance();
    private Handler timeHandler;
    private Locale locale;

    //----------------used to generate the crimes and add them to the recycler view on screen
    private RVAdapterCrimes rvAdapterCrimes;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    //--------- cards to remove if no information inside them
    private CardView crimesCardView;
    private CardView aboutCardView;
    private CardView socialMediaCardView;

    private AdView mAdView;
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-------network state receiver signals when a connection has changed allowing code
        //-----to be run when a user goes from offline to online using the NetworkStateReceiver class
        networkStateReceiver = new NetworkStateReceiver();

        MobileAds.initialize(getApplicationContext(),
                "ca-app-pub-3940256099942544~3347511713");

        mAdView = (AdView) findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.setVisibility(INVISIBLE);


        //---------set up gps tracker and get lat long
        gpsTracker = new GPSTrackerUtil(this);
        latLng.setLatLng(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()));

        //------- set up dialog message to show during async tasks
        dialog = new ProgressDialog(this);

        //---------------find all views to be manipulated on the front end
        layoutBody = findViewById(R.id.informationLayout);
        layoutTitle = findViewById(R.id.informationCardView);
        informationLayout = findViewById(R.id.informationLayout);
        streetName = findViewById(R.id.streetName);
        recyclerView = findViewById(R.id.crimesRv);
        crimesCardView = findViewById(R.id.crimesCardView);
        time = findViewById(R.id.time);
        about = findViewById(R.id.about);
        aboutTitle = findViewById(R.id.aboutTitle);
        areaTitle = findViewById(R.id.areaTitle);
        email = findViewById(R.id.email);
        website = findViewById(R.id.website);
        facebook = findViewById(R.id.facebook);
        twitter = findViewById(R.id.twitter);
        aboutCardView = findViewById(R.id.aboutCardView);
        weather = findViewById(R.id.weather);
        socialMediaCardView = findViewById(R.id.socialMediaCardView);
        oneTotal = findViewById(R.id.oneTotal);
        twoTotal = findViewById(R.id.twoTotal);
        threeTotal = findViewById(R.id.threeTotal);
        fourTotal = findViewById(R.id.fourTotal);
        fiveTotal = findViewById(R.id.fiveTotal);
        sixTotal = findViewById(R.id.sixTotal);
        sevenTotal = findViewById(R.id.sevenTotal);
        eightTotal = findViewById(R.id.eightTotal);
        nineTotal = findViewById(R.id.nineTotal);
        tenTotal = findViewById(R.id.tenTotal);
        elevenTotal = findViewById(R.id.elevenTotal);
        twelveTotal = findViewById(R.id.twelveTotal);
        thirteenTotal = findViewById(R.id.thirteenTotal);
        fourteenTotal = findViewById(R.id.fourteenTotal);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);
        nine = findViewById(R.id.nine);
        ten = findViewById(R.id.ten);
        eleven = findViewById(R.id.eleven);
        twelve = findViewById(R.id.twelve);
        thirteen = findViewById(R.id.thirteen);
        fourteen = findViewById(R.id.fourteen);
        areaTotalsTitle = findViewById(R.id.areaTotalsTitle);
        crimesTitle = findViewById(R.id.crimesTitle);

        //--------------set up the custom scroll view and get the layout heights for the custom scroll view layouts
        layoutTitle.setY((getScreenHeight(this) / 10) * 6);
        layoutBody.setY(((getScreenHeight(this) / 10) * 6) - layoutTitle.getHeight());
        lockableScrollView = findViewById(R.id.scrollView);
        lockableScrollView.setScrollingEnabled(false);

        //---------- if the scrollview content is
        ViewGroup.LayoutParams lp = layoutBody.getLayoutParams();
        lp.height = layoutBody.getHeight() < getScreenHeight(this) ? getScreenHeight(this) : layoutBody.getHeight();
        layoutBody.setLayoutParams(lp);

        //-----------used for the map view
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //------------------gets the status bar height                              MAY BE INCORRECT
        statusBarHeight = getStatusBarHeight(this);

        //---------------setting on touch listener for the custom scroll view layouts
        layoutBody.setOnTouchListener(handleTouch);
        layoutTitle.setOnTouchListener(handleTouchTitle);

        //----------sets up the location manager for getting gps coordinates I THINK?
        lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //---------------starts an animation after the splash screen to indicate that the information bar is collapasable
        layoutTitle.animate()
                .y(getScreenHeight(MainActivity.this))
                .setDuration(250)
                .setStartDelay(2000)
                .start();
        layoutBody.animate()
                .y(getScreenHeight(MainActivity.this))
                .setDuration(250)
                .setStartDelay(2000)
                .start();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("Map", " Ready");
        mMap = googleMap;
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng newLatLng) {
                hidePopUpView();
                dialog.setMessage("Getting crimes...");
                dialog.show();
                latLng.setLatLng(newLatLng);
                findAddress();
                callNewCrime();
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                hidePopUpView();
            }
        });
    }

    public void setCrimesOnMarkerClick(ArrayList<Crimes> crimeList) {
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        rvAdapterCrimes = new RVAdapterCrimes(crimeList, MainActivity.this);
        recyclerView.setAdapter(rvAdapterCrimes);
    }

    public void hidePopUpView() {
        layoutTitle.animate()
                .y(getScreenHeight(MainActivity.this))
                .setDuration(250)
                .start();
        layoutBody.animate()
                .y(getScreenHeight(MainActivity.this))
                .setDuration(250)
                .start();
        mAdView.destroy();
        mAdView.setVisibility(INVISIBLE);
    }

    public void showPopUpViewTitle() {
        layoutTitle.animate()
                .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - statusBarHeight)
                .setDuration(250)
                .start();
        layoutBody.animate()
                .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - statusBarHeight)
                .setDuration(250)
                .start();
        mAdView.destroy();
        mAdView.setVisibility(INVISIBLE);
    }

    //------------------private methods

    private void findAddress() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.getLatLng().latitude, latLng.getLatLng().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            currentAddress.setAddress(address);
        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error",
                    Toast.LENGTH_SHORT).show();
        }
        if (null != currentAddress.getAddress() && currentAddress.getAddress().contains("UK")) {
            locale = Locale.UK;
        } else if (null != currentAddress.getAddress() && currentAddress.getAddress().contains("USA")) {
            locale = Locale.US;
        } else {
            locale = Locale.UK;
        }

        timeHandler = new Handler(getMainLooper());
        timeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                time.setText(new SimpleDateFormat("h:mm a", locale).format(new Date()));
                timeHandler.postDelayed(this, 1000);
            }
        }, 10);
    }

    private void callNewCrime() {
        dateUtil.resetDate();
        new GenerateCrimeUrl(MainActivity.this, false);
        new GenerateNeighbourhoodLocation(this, latLng.getLatLng()).execute();
        dateUtil.setMaxMonth(dateUtil.getMonth());
        dateUtil.setMaxYear(dateUtil.getYear());
        new GetCurrentWeather(MainActivity.this, latLng.getLatLng()).execute();
    }

    private void showPosition() {
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && new Network().isNetworkEnabled(MainActivity.this)) {
            dialog.setMessage("Getting crimes...");
            dialog.show();
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng.getLatLng(), 15);
            mMap.addMarker(new MarkerOptions().position(latLng.getLatLng()).title("You are here"));
            mMap.animateCamera(cameraUpdate);
            dialog.setMessage("Getting Crimes");
            dialog.show();
            findAddress();
            callNewCrime();
        }else{
            Toast.makeText(getApplicationContext(), "Internet connection required.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private Bitmap getMarkerBitmapFromView(int mapColour, View v) {

        //RelativeLayout customMarkerView = (RelativeLayout)((LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_map_marker, null);
        CardView cardView = (CardView) v.findViewById(R.id.custom_marker_view);
        TextView textView = (TextView) v.findViewById(R.id.txtCrimeCount);
        textView.setText(Integer.toString(mapColour));


        if (mapColour == 1) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color1));
        } else if (mapColour == 2) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color2));
        } else if (mapColour == 3) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color3));
        } else if (mapColour == 4) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color4));
        } else if (mapColour == 5) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color5));
        } else if (mapColour == 6) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color6));
        } else if (mapColour == 7) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color7));
        } else if (mapColour == 8) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color8));
        } else if (mapColour == 9) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color9));
        } else {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color10));
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        v.setLayoutParams(new ActionMenuView.LayoutParams(ActionMenuView.LayoutParams.WRAP_CONTENT, ActionMenuView.LayoutParams.WRAP_CONTENT));
        v.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        v.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        v.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);

        return bitmap;
    }

    //------------public methods

    public void dismissDialog() {
        dialog.dismiss();
        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "No crime Statistics for this date",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    //-----------------------------------------------------------put all markers on map and associate crimes and outcomes with markers
    public void updateMap(ArrayList<ArrayList<Crimes>> list) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        crimeList = list;
        try {
            mMap.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<Marker> markers = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            int mapColour = list.get(i).size();
            String streetName = list.get(i).get(0).getStreetName().toString();

            LayoutInflater inflater = (LayoutInflater) MainActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.custom_map_marker, null);

            markers.add(mMap.addMarker(new MarkerOptions()
                    .title(streetName)
                    .position(new LatLng(list.get(i).get(0).getLatitude(), list.get(i).get(0).getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mapColour, v)))));
            mMap.setOnMarkerClickListener(this);
        }


        cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.getLatLng().latitude, latLng.getLatLng().longitude), 15);
        mMap.animateCamera(cameraUpdate);
        Toast.makeText(getApplicationContext(), "Crime statistics around " + currentAddress.getAddress() + " for " + dateUtil.getMonth() + "/" + dateUtil.getYear(),
                Toast.LENGTH_LONG).show();
    }

    //-----------------------------------------------------------code for marker selected
    @Override
    public boolean onMarkerClick(Marker marker) {
        crimeCount.resetStreetCount();
        if (layoutTitle.getY() == getScreenHeight(MainActivity.this)) {
            layoutTitle.animate()
                    .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - statusBarHeight)
                    .setDuration(250)
                    .setStartDelay(0)
                    .start();
            layoutBody.animate()
                    .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - statusBarHeight)
                    .setDuration(250)
                    .setStartDelay(0)
                    .start();

        }
        String location = "";
        ArrayList<Crimes> tempCrimes = new ArrayList<>();
        ArrayList<Counter> counts = new ArrayList<>();
        for (int i = 0; i < crimeList.size(); i++) {
            if (crimeList.get(i).get(0).getLongitude() == marker.getPosition().longitude &&
                    crimeList.get(i).get(0).getLatitude() == marker.getPosition().latitude) {
                location = crimeList.get(i).get(0).getStreetName();
                for (int j = 0; j < crimeList.get(i).size(); j++) {

                    if (counts.isEmpty()) {
                        counts.add(new Counter(crimeList.get(i).get(j).getCrimeType(), 1));
                        continue;
                    }
                    for (int k = 0; k < counts.size(); k++) {
                        if (counts.get(k).getName().equalsIgnoreCase(crimeList.get(i).get(j).getCrimeType())) {
                            int temp = counts.get(k).getCount();
                            counts.set(k, new Counter(crimeList.get(i).get(j).getCrimeType(), ++temp));
                            break;
                        }
                        if (k == counts.size() - 1) {
                            counts.add(new Counter(crimeList.get(i).get(j).getCrimeType(), 1));
                            break;
                        }
                    }

                    tempCrimes.add(new Crimes(crimeList.get(i).get(j).getCrimeType(),
                            crimeList.get(i).get(j).getDate(),
                            crimeList.get(i).get(j).getTime(),
                            crimeList.get(i).get(j).getOutcome(), location,
                            crimeList.get(i).get(j).getLatitude(),
                            crimeList.get(i).get(j).getLongitude(),
                            crimeList.get(i).get(j).getWeapon(),
                            crimeList.get(i).get(j).getDescription()));
                }
                break;
            }
        }


        streetName.setText(location.trim());
        areaTotalsTitle.setText(location.trim());
        setCrimesOnMarkerClick(tempCrimes);
        new CrimeCountList(this).sortCrimesCount(counts, false);
        return false;
    }

    public void setDescription(String description) {
        if (null != description && !description.equals("")) {
            about.setText(description.replaceAll("\\<.*?\\>", ""));
        } else {
            aboutCardView.setVisibility(GONE);
        }
    }

    public void setNeighbourhoodDetails(String title) {
        if (null != title && !title.equals("")) {
            aboutCardView.setVisibility(VISIBLE);
            aboutTitle.setText(title.substring(0, 1).toUpperCase() + title.substring(1));

        }
    }

    public void setAboutArea(String area, String emailString, String facebookString, String youtubeString, String twitterString, String websiteString) {
        if (null != area && !area.equals("")) {
            String[] aresSplit = area.split(" ");
            String fullArea = "";
            for (String areas : aresSplit) {
                areas = areas.substring(0, 1).toUpperCase() + areas.substring(1);
                fullArea += areas + " ";
            }

            areaTitle.setText(fullArea.trim());
            crimesTitle.setText(fullArea.trim());
        }
        if (null != emailString && !emailString.equals("")) {
            email.setVisibility(VISIBLE);
            email.setText("Email: " + emailString);
            addLinks(email, Linkify.EMAIL_ADDRESSES);
            email.setLinkTextColor(ContextCompat.getColor(this, R.color.link));
        } else {
            email.setVisibility(GONE);
        }
        if (null != facebookString && !facebookString.equals("")) {
            facebook.setVisibility(VISIBLE);
            facebook.setText("Facebook: " + facebookString);
            addLinks(facebook, Linkify.WEB_URLS);
            facebook.setLinkTextColor(ContextCompat.getColor(this, R.color.link));
        } else {
            facebook.setVisibility(GONE);
        }
        if (null != twitterString && !twitterString.equals("")) {
            twitter.setVisibility(VISIBLE);
            twitter.setText("Twitter: " + twitterString);
            addLinks(twitter, Linkify.WEB_URLS);
            twitter.setLinkTextColor(ContextCompat.getColor(this, R.color.link));
        } else {
            twitter.setVisibility(GONE);
        }
        if (null != websiteString && !websiteString.equals("")) {
            website.setVisibility(VISIBLE);
            website.setText("Website: " + websiteString);
            addLinks(website, Linkify.WEB_URLS);
            website.setLinkTextColor(ContextCompat.getColor(this, R.color.link));
        } else {
            website.setVisibility(GONE);
        }
        if (null != youtubeString && !youtubeString.equals("")) {
        }
        if (null == emailString &&
                null == facebookString &&
                null == twitterString &&
                null == websiteString) {
            socialMediaCardView.setVisibility(GONE);
        } else {
            socialMediaCardView.setVisibility(VISIBLE);
        }

        ViewGroup.LayoutParams lp = layoutBody.getLayoutParams();
        lp.height = layoutBody.getHeight() < getScreenHeight(this) ? getScreenHeight(this) : layoutBody.getHeight();
        layoutBody.setLayoutParams(lp);

    }

    public void setWeather(String description, Double curentTemp) {
        char tmp = 0x00B0;
        weather.setText(description + " " + curentTemp.intValue() + tmp);
    }

    @Override
    public void onBackPressed() {
        if (layoutTitle.getY() == 0) {
            showPopUpViewTitle();
        } else if (layoutTitle.getY() == getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - statusBarHeight) {
            hidePopUpView();
        } else {
            super.onBackPressed();
        }
    }


    public ArrayList<TextView> getTextViews(boolean totals) {
        ArrayList<TextView> textViews = new ArrayList<>();
        if (totals) {
            textViews.add(oneTotal);
            textViews.add(twoTotal);
            textViews.add(threeTotal);
            textViews.add(fourTotal);
            textViews.add(fiveTotal);
            textViews.add(sixTotal);
            textViews.add(sevenTotal);
            textViews.add(eightTotal);
            textViews.add(nineTotal);
            textViews.add(nineTotal);
            textViews.add(tenTotal);
            textViews.add(elevenTotal);
            textViews.add(twelveTotal);
            textViews.add(thirteenTotal);
            textViews.add(fourteenTotal);
            return textViews;
        } else {
            textViews.add(one);
            textViews.add(two);
            textViews.add(three);
            textViews.add(four);
            textViews.add(five);
            textViews.add(six);
            textViews.add(seven);
            textViews.add(eight);
            textViews.add(nine);
            textViews.add(nine);
            textViews.add(ten);
            textViews.add(eleven);
            textViews.add(twelve);
            textViews.add(thirteen);
            textViews.add(fourteen);
            return textViews;
        }
    }

    //------------------------all code for on touch event for custom scroll view
    private View.OnTouchListener handleTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            return move(view, event);

        }
    };

    //noinspection AndroidLintClickableViewAccessibility
    private View.OnTouchListener handleTouchTitle = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            return move(view, event);

        }
    };

    private boolean move(View view, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isClickCount = 0;
                previousPosition = event.getRawY();
                startingPosition = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("click count = " + isClickCount);
                if(isClickCount < 5){
                    if(layoutTitle.getY() <= mAdView.getHeight() + 15 && layoutTitle.getY() >= mAdView.getHeight() - 15){
                        showPopUpViewTitle();
                    }else if(layoutTitle.getY() <= getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - statusBarHeight + 15 &&
                            layoutTitle.getY() >= getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - statusBarHeight - 15){
                        layoutTitle.animate()
                                .y(mAdView.getHeight())
                                .setDuration(250)
                                .start();
                        layoutBody.animate()
                                .y(mAdView.getHeight())
                                .setDuration(250)
                                .start();
                        mAdView.setVisibility(VISIBLE);
                        adRequest = new AdRequest.Builder().build();
                        mAdView.loadAd(adRequest);
                    }
                }
                if (startingPosition - previousPosition > 10
                        && layoutTitle.getY() > getScreenHeight(MainActivity.this) / 4
                        && layoutTitle.getY() != 0) {
                    System.out.println("4 || " + layoutTitle.getY());
                    layoutTitle.animate()
                            .y(mAdView.getHeight())
                            .setDuration(250)
                            .start();
                    layoutBody.animate()
                            .y(mAdView.getHeight())
                            .setDuration(250)
                            .start();
                    mAdView.setVisibility(VISIBLE);
                    adRequest = new AdRequest.Builder().build();
                    mAdView.loadAd(adRequest);
                } else if (startingPosition - previousPosition < -10
                        && layoutTitle.getY() < getScreenHeight(MainActivity.this) - getScreenHeight(MainActivity.this) / 3
                        && layoutTitle.getY() != getScreenHeight(MainActivity.this) - layoutTitle.getHeight()
                        && layoutBody.getY() >= 0) {
                    showPopUpViewTitle();
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                float topOfTitle = layoutTitle.getY();
                float topOfBody = layoutBody.getY();
                float bodyMeasuredHeight = getMeasuredHeight(informationLayout);
                float titleHeight = layoutTitle.getHeight();
                adViewHeight = mAdView.getHeight();
                isClickCount++;
                distanceMoved = previousPosition - event.getRawY();
                //moving up
                if (distanceMoved > 0) {
                    //if you have scrolled to the end of the line
                    if (topOfBody + bodyMeasuredHeight - distanceMoved <= getScreenHeight(MainActivity.this)) {
                        System.out.println("1");
                        layoutTitle.animate()
                                .y(adViewHeight)
                                .setDuration(0)
                                .start();
                        layoutBody.animate()
                                .y(getScreenHeight(MainActivity.this) - bodyMeasuredHeight)
                                .setDuration(0)
                                .start();
                    }
                    //keep title still but move body
                    else if (topOfBody + titleHeight + adViewHeight - statusBarHeight < titleHeight + adViewHeight) {
                        System.out.println("2");
                        layoutTitle.animate()
                                .y(adViewHeight)
                                .setDuration(0)
                                .start();
                        layoutBody.animate()
                                .y(topOfBody - distanceMoved)
                                .setDuration(0)
                                .start();

                    }
                    //move both title and body simultaneously
                    else {
                        System.out.println("3");
                        layoutTitle.animate()
                                .y(topOfTitle - distanceMoved)
                                .setDuration(0)
                                .start();
                        layoutBody.animate()
                                .y(topOfBody - distanceMoved)
                                .setDuration(0)
                                .start();
                    }
                }
                //moving down
                else {
                    if (topOfBody + titleHeight - statusBarHeight < (titleHeight + adViewHeight) - statusBarHeight) {
                        System.out.println("4");
                        layoutTitle.animate()
                                .y(adViewHeight)
                                .setDuration(0)
                                .start();
                    } else {
                        System.out.println("5");
                        if (topOfTitle != topOfBody) {
                            layoutTitle.animate()
                                    .y(topOfBody)
                                    .setDuration(0)
                                    .start();
                        }
                        if (topOfTitle >= getScreenHeight(MainActivity.this) - titleHeight - statusBarHeight) {
                            layoutTitle.animate()
                                    .y(getScreenHeight(MainActivity.this) - titleHeight - statusBarHeight)
                                    .setDuration(0)
                                    .start();
                        } else {
                            System.out.println("6");
                            layoutTitle.animate()
                                    .y(topOfTitle - distanceMoved)
                                    .setDuration(0)
                                    .start();
                        }
                    }
                    if (topOfBody >= getScreenHeight(MainActivity.this) - titleHeight - statusBarHeight) {
                        System.out.println("7");
                        layoutBody.animate()
                                .y(getScreenHeight(MainActivity.this) - titleHeight - statusBarHeight)
                                .setDuration(0)
                                .start();
                    } else {
                        System.out.println("8");
                        layoutBody.animate()
                                .y(topOfBody - distanceMoved)
                                .setDuration(0)
                                .start();
                    }

                }
                previousPosition = event.getRawY();

                break;
        }
        return false;
//            case MotionEvent.ACTION_DOWN:
//                isClickCount = 0;
//                previousPosition = event.getRawY();
//                startingPosition = event.getRawY();
//                break;
//            case MotionEvent.ACTION_UP:
//                System.out.println("click count = " + isClickCount);
//                if(isClickCount < 5){
//                    if(layoutTitle.getY() <= mAdView.getHeight() + 15 && layoutTitle.getY() >= mAdView.getHeight() - 15){
//                        showPopUpViewTitle();
//                    }else if(layoutTitle.getY() <= getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - statusBarHeight + 15 &&
//                            layoutTitle.getY() >= getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - statusBarHeight - 15){
//                        layoutTitle.animate()
//                                .y(mAdView.getHeight())
//                                .setDuration(250)
//                                .start();
//                        layoutBody.animate()
//                                .y(mAdView.getHeight())
//                                .setDuration(250)
//                                .start();
//                        mAdView.setVisibility(VISIBLE);
//                        adRequest = new AdRequest.Builder().build();
//                        mAdView.loadAd(adRequest);
//                    }
//                }
//                if (startingPosition - previousPosition > 10
//                        && layoutTitle.getY() > getScreenHeight(MainActivity.this) / 4
//                        && layoutTitle.getY() != 0) {
//                    System.out.println("4 || " + layoutTitle.getY());
//                    layoutTitle.animate()
//                            .y(mAdView.getHeight())
//                            .setDuration(250)
//                            .start();
//                    layoutBody.animate()
//                            .y(mAdView.getHeight())
//                            .setDuration(250)
//                            .start();
//                    mAdView.setVisibility(VISIBLE);
//                    adRequest = new AdRequest.Builder().build();
//                    mAdView.loadAd(adRequest);
//                } else if (startingPosition - previousPosition < -10
//                        && layoutTitle.getY() < getScreenHeight(MainActivity.this) - getScreenHeight(MainActivity.this) / 3
//                        && layoutTitle.getY() != getScreenHeight(MainActivity.this) - layoutTitle.getHeight()
//                        && layoutBody.getY() >= 0) {
//                    showPopUpViewTitle();
//                }
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                break;
//            case MotionEvent.ACTION_MOVE:
//                isClickCount++;
//                distanceMoved = previousPosition - event.getRawY();
//                //moving up
//                if (distanceMoved > 0) {
//                    //if you have scrolled to the end of the line
//                    if (layoutBody.getY() + layoutBody.getHeight() - distanceMoved <= getScreenHeight(MainActivity.this)) {
//                        System.out.println("1");
//                        layoutTitle.animate()
//                                .y(mAdView.getHeight())
//                                .setDuration(0)
//                                .start();
//                        layoutBody.animate()
//                                .y(getScreenHeight(MainActivity.this) - layoutBody.getHeight())
//                                .setDuration(0)
//                                .start();
//                    }
//                    //keep title still but move body
//                    else if (layoutBody.getY() + layoutTitle.getHeight() - statusBarHeight < layoutTitle.getHeight() + mAdView.getHeight()) {
//                        System.out.println("2");
//                        layoutTitle.animate()
//                                .y(mAdView.getHeight())
//                                .setDuration(0)
//                                .start();
//                        layoutBody.animate()
//                                .y(layoutBody.getY() - distanceMoved)
//                                .setDuration(0)
//                                .start();
//
//                    }
//                    //move both title and body simultaneously
//                    else {
//                        System.out.println("3");
//                        layoutTitle.animate()
//                                .y(layoutTitle.getY() - distanceMoved)
//                                .setDuration(0)
//                                .start();
//                        layoutBody.animate()
//                                .y(layoutBody.getY() - distanceMoved)
//                                .setDuration(0)
//                                .start();
//                    }
//                }
//                //moving down
//                else {
//                    if (layoutBody.getY() + layoutTitle.getHeight() - statusBarHeight < (layoutTitle.getHeight() + mAdView.getHeight()) - statusBarHeight) {
//                        System.out.println("4");
//                        layoutTitle.animate()
//                                .y(mAdView.getHeight())
//                                .setDuration(0)
//                                .start();
//                    } else {
//                        System.out.println("5");
//                        if (layoutTitle.getY() != layoutBody.getY()) {
//                            layoutTitle.animate()
//                                    .y(layoutBody.getY())
//                                    .setDuration(0)
//                                    .start();
//                        }
//                        if (layoutTitle.getY() >= getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - statusBarHeight) {
//                            layoutTitle.animate()
//                                    .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - statusBarHeight)
//                                    .setDuration(0)
//                                    .start();
//                        } else {
//                            System.out.println("6");
//                            layoutTitle.animate()
//                                    .y(layoutTitle.getY() - distanceMoved)
//                                    .setDuration(0)
//                                    .start();
//                        }
//                    }
//                    if (layoutBody.getY() >= getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - statusBarHeight) {
//                        System.out.println("7");
//                        layoutBody.animate()
//                                .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - statusBarHeight)
//                                .setDuration(0)
//                                .start();
//                    } else {
//                        System.out.println("8");
//                        layoutBody.animate()
//                                .y(layoutBody.getY() - distanceMoved)
//                                .setDuration(0)
//                                .start();
//                    }
//
//                }
//                previousPosition = event.getRawY();
//
//                break;
//        }
//        return false;

    }

    @Override
    public void networkAvailable() {
        if(latLng.getLatLng().latitude != 0.0d && latLng.getLatLng().longitude != 0.0d && new Network().isNetworkEnabled(MainActivity.this)){
            showPosition();
        }
    }

    @Override
    public void networkUnavailable() {
    }
    //--------on activity paused unregister receiver and remove list if this isnt
    // -------done it can still be running in the background when app closes
    public void onPause() {
        super.onPause();
        try {
            unregisterReceiver(networkStateReceiver);
            networkStateReceiver.removeListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //--------on activity stop unregister receiver and remove list if this isnt
    // -------done it can still be running in the background when app closes used to catch
    // -------any errors from on pause
    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(networkStateReceiver);
            networkStateReceiver.removeListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //----on resuming the app add a listener to the previously made network state receiver and
    // ---then register this to a broadcast receiver
    public void onResume() {
        super.onResume();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }
}