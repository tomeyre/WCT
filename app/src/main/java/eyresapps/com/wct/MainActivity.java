package eyresapps.com.wct;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.CardView;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import eyresapps.com.api_calls.GenerateNeighbourhoodLocation;
import eyresapps.com.api_calls.GetCurrentWeather;
import eyresapps.com.broadcastRecievers.Network;
import eyresapps.com.broadcastRecievers.NetworkStateReceiver;
import eyresapps.com.crimeLocations.GenerateCrimeUrl;
import eyresapps.com.data.Counter;
import eyresapps.com.data.CrimeCount;
import eyresapps.com.data.Crimes;
import eyresapps.com.data.FilterItem;
import eyresapps.com.utils.AnimateFilter;
import eyresapps.com.utils.CapitalizeString;
import eyresapps.com.utils.CrimeCountList;
import eyresapps.com.utils.CurrentAddressUtil;
import eyresapps.com.utils.DateUtil;
import eyresapps.com.utils.FindSearchLocation;
import eyresapps.com.utils.GPSTrackerUtil;
import eyresapps.com.utils.LatitudeAndLongitudeUtil;
import eyresapps.com.utils.ScreenUtils;

import static android.text.util.Linkify.addLinks;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static eyresapps.com.utils.ScreenUtils.convertDpToPixel;
import static eyresapps.com.utils.ScreenUtils.getMeasuredHeight;
import static eyresapps.com.utils.ScreenUtils.getScreenHeight;
import static eyresapps.com.utils.ScreenUtils.getStatusBarHeight;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, NetworkStateReceiver.NetworkStateReceiverListener {

    boolean filter = false;
    float filterHeight;

    LinearLayout dateRow;

    ArrayList<FilterItem> filterList;
    ArrayList<ArrayList<Crimes>> filteredCrimes;
    ImageView filterImage;
    Button filterSearchBtn;

    Spinner daySpinner;
    Spinner monthSpinner;
    Spinner yearSpinner;

    float topOfTitle;
    float topOfBody;
    float titleHeight;
    float bodyMeasuredHeight;

    private Geocoder geocoder;
    private List<Address> addresses;
    private EditText search;
    private CardView searchLayout;

    private CardView heatMapBtn;
    private ImageView btnImage;
    private ArrayList<Marker> markers;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    private CardView filterBtn;

    private int isClickCount = 0;
    private float adViewHeight;

    private NetworkStateReceiver networkStateReceiver;

    CrimeCount crimeCount = CrimeCount.getInstance();

    ArrayList<Crimes> markerCrimes = new ArrayList<>();

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

    private CardView filterOne;
    private CardView filterTwo;
    private CardView filterThree;
    private CardView filterFour;
    private CardView filterFive;
    private CardView filterSix;
    private CardView filterSeven;
    private CardView filterEight;
    private CardView filterNine;
    private CardView filterTen;
    private CardView filterEleven;
    private CardView filterTwelve;
    private CardView filterThirteen;
    private CardView filterFourteen;
    private CardView filterFifteen;
    private CardView filterSixteen;
    private CardView filterSeventeen;
    private CardView filterEighteen;
    private CardView filterNineteen;
    private CardView filterTwenty;
    private TextView filterOneTxt;
    private TextView filterTwoTxt;
    private TextView filterThreeTxt;
    private TextView filterFourTxt;
    private TextView filterFiveTxt;
    private TextView filterSixTxt;
    private TextView filterSevenTxt;
    private TextView filterEightTxt;
    private TextView filterNineTxt;
    private TextView filterTenTxt;
    private TextView filterElevenTxt;
    private TextView filterTwelveTxt;
    private TextView filterThirteenTxt;
    private TextView filterFourteenTxt;
    private TextView filterFifteenTxt;
    private TextView filterSixteenTxt;
    private TextView filterSeventeenTxt;
    private TextView filterEighteenTxt;
    private TextView filterNineteenTxt;
    private TextView filterTwentyTxt;

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

    //--------- cards to remove if no information inside them
    private CardView crimesCardView;
    private CardView aboutCardView;
    private CardView socialMediaCardView;

    private AdView mAdView;
    private AdRequest adRequest;

    private Button additionalInformationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_main);

        dateRow = findViewById(R.id.dateRow);

        daySpinner = findViewById(R.id.daySpinner);
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.days, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        daySpinner.setAdapter(adapter);
        daySpinner.setSelection(1);
        adapter = ArrayAdapter.createFromResource(this, R.array.months, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        monthSpinner.setAdapter(adapter);
        monthSpinner.setSelection(1);
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        ArrayList<Integer> years = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            years.add(year--);
        }
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, years);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item_layout);
        yearSpinner.setAdapter(arrayAdapter);
        yearSpinner.setSelection(1);



        filterBtn = findViewById(R.id.filterBtn);
        filterImage = findViewById(R.id.bntFilter);
        filterSearchBtn = findViewById(R.id.filterSearchBtn);
        filterSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterCrimeList();
                shrinkFilter(filterBtn);
            }
        });


        adViewHeight = ScreenUtils.convertDpToPixel(50,this);

        searchLayout = findViewById(R.id.searchLayout);
        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setCursorVisible(true);
            }
        });

        hideSoftKeyboard();
        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER){
                    if (!search.getText().toString().trim().equals("")) {
                        new FindSearchLocation(MainActivity.this).execute(search.getText().toString());
                    }
                    search.setText("");
                    search.clearFocus();
                    hideSoftKeyboard();
                    return true;
                }
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DEL) {
                    if(search.getText().length() > 0){
                        search.setText(search.getText().delete(search.getText().length() - 1,search.getText().length()));
                        search.setSelection(search.getText().length());
                    }
                }

                return true;
            }
        });


        markers = new ArrayList<>();

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
        heatMapBtn = findViewById(R.id.heatMapBtn);
        btnImage = findViewById(R.id.btnImage);
        additionalInformationBtn = findViewById(R.id.additionalInformationBtn);
        layoutBody = findViewById(R.id.informationLayout);
        layoutTitle = findViewById(R.id.informationCardView);
        informationLayout = findViewById(R.id.informationLayout);
        streetName = findViewById(R.id.streetName);
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

        //-----------
        filterOne = findViewById(R.id.filterOne);
        filterTwo = findViewById(R.id.filterTwo);
        filterThree = findViewById(R.id.filterThree);
        filterFour = findViewById(R.id.filterFour);
        filterFive = findViewById(R.id.filterFive);
        filterSix = findViewById(R.id.filterSix);
        filterSeven = findViewById(R.id.filterSeven);
        filterEight = findViewById(R.id.filterEight);
        filterNine = findViewById(R.id.filterNine);
        filterTen = findViewById(R.id.filterTen);
        filterEleven = findViewById(R.id.filterEleven);
        filterTwelve = findViewById(R.id.filterTwelve);
        filterThirteen = findViewById(R.id.filterThirteen);
        filterFourteen = findViewById(R.id.filterFourteen);
        filterFifteen = findViewById(R.id.filterFifteen);
        filterSixteen = findViewById(R.id.filterSixteen);
        filterSeventeen = findViewById(R.id.filterSeventeen);
        filterEighteen = findViewById(R.id.filterEighteen);
        filterNineteen = findViewById(R.id.filterNineteen);
        filterTwenty = findViewById(R.id.filterTwenty);

        filterOneTxt = findViewById(R.id.filterOneTxt);
        filterTwoTxt = findViewById(R.id.filterTwoTxt);
        filterThreeTxt = findViewById(R.id.filterThreeTxt);
        filterFourTxt = findViewById(R.id.filterFourTxt);
        filterFiveTxt = findViewById(R.id.filterFiveTxt);
        filterSixTxt = findViewById(R.id.filterSixTxt);
        filterSevenTxt = findViewById(R.id.filterSevenTxt);
        filterEightTxt = findViewById(R.id.filterEightTxt);
        filterNineTxt = findViewById(R.id.filterNineTxt);
        filterTenTxt = findViewById(R.id.filterTenTxt);
        filterElevenTxt = findViewById(R.id.filterElevenTxt);
        filterTwelveTxt = findViewById(R.id.filterTwelveTxt);
        filterThirteenTxt = findViewById(R.id.filterThirteenTxt);
        filterFourteenTxt = findViewById(R.id.filterFourteenTxt);
        filterFifteenTxt = findViewById(R.id.filterFifteenTxt);
        filterSixteenTxt = findViewById(R.id.filterSixteenTxt);
        filterSeventeenTxt = findViewById(R.id.filterSeventeenTxt);
        filterEighteenTxt = findViewById(R.id.filterEighteenTxt);
        filterNineteenTxt = findViewById(R.id.filterNineteenTxt);
        filterTwentyTxt = findViewById(R.id.filterTwentyTxt);

        filterList = new ArrayList<>();
        filterList.add(new FilterItem(filterOne, filterOneTxt,"",true));
        filterList.add(new FilterItem(filterTwo,filterTwoTxt,"", true));
        filterList.add(new FilterItem(filterThree,filterThreeTxt,"", true));
        filterList.add(new FilterItem(filterFour,filterFourTxt,"", true));
        filterList.add(new FilterItem(filterFive,filterFiveTxt,"", true));
        filterList.add(new FilterItem(filterSix,filterSixTxt,"", true));
        filterList.add(new FilterItem(filterSeven,filterSevenTxt,"", true));
        filterList.add(new FilterItem(filterEight,filterEightTxt,"", true));
        filterList.add(new FilterItem(filterNine,filterNineTxt,"", true));
        filterList.add(new FilterItem(filterTen,filterTenTxt,"", true));
        filterList.add(new FilterItem(filterEleven,filterElevenTxt,"", true));
        filterList.add(new FilterItem(filterTwelve,filterTwelveTxt,"", true));
        filterList.add(new FilterItem(filterThirteen,filterThirteenTxt,"", true));
        filterList.add(new FilterItem(filterFourteen,filterFourteenTxt,"", true));
        filterList.add(new FilterItem(filterFifteen,filterFifteenTxt,"", true));
        filterList.add(new FilterItem(filterSixteen,filterSixteenTxt,"", true));
        filterList.add(new FilterItem(filterSeventeen,filterSeventeenTxt,"", true));
        filterList.add(new FilterItem(filterEighteen,filterEighteenTxt,"", true));
        filterList.add(new FilterItem(filterNineteen,filterNineteenTxt,"", true));
        filterList.add(new FilterItem(filterTwenty,filterTwentyTxt,"", true));


        //--------
        additionalInformationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mock data for testing

                /*markerCrimes.add(new Crimes("TWAT","21/10/2017","10:10","arrest","balls",100,100,"nope","shit"));
                markerCrimes.add(new Crimes("TWAT","21/10/2017","10:10","arrest","balls",100,100,"nope","shit"));
                markerCrimes.add(new Crimes("TWAT","21/10/2017","10:10","arrest","balls",100,100,"nope","shit"));
                markerCrimes.add(new Crimes("TWAT","21/10/2017","10:10","arrest","balls",100,100,"nope","shit"));*/

                Intent intent = new Intent( MainActivity.this, AdditionalInformation.class);
                intent.putExtra("crimes", markerCrimes);
                startActivity(intent);
            }
        });

        heatMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(markers.isEmpty()){
                    if(filter){
                        updateMap(filteredCrimes, filter);
                    }else {
                        updateMap(crimeList, filter);
                    }
                }else {
                    try {
                        mMap.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    markers.clear();
                    mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                }
            }
        });

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

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandFilter(filterBtn);
            }
        });

    }

    private void filterCrimeList(){
        filteredCrimes = new ArrayList<>();
        ArrayList<Crimes> innerList;

        for(int i = 0; i < crimeList.size(); i++){
            innerList = new ArrayList<>();
            for(int j = 0; j < crimeList.get(i).size(); j++){
                for(int k = 0; k < filterList.size(); k++){
                    if(crimeList.get(i).get(j).getCrimeType().equalsIgnoreCase(filterList.get(k).getNameString()) && filterList.get(k).getShow()){
                        System.out.println("filter name  = " + filterList.get(k).getNameString() + " actual = " + crimeList.get(i).get(j).getCrimeType() + " / " + filterList.get(k).getShow());
                        innerList.add(crimeList.get(i).get(j));
                    }else if(crimeList.get(i).get(j).getCrimeType().equalsIgnoreCase(filterList.get(k).getName().getText().toString()) && !filterList.get(k).getShow()){
                        System.out.println("filter name  = " + filterList.get(k).getNameString() + " actual = " + crimeList.get(i).get(j).getCrimeType() + " / " + filterList.get(k).getShow());
                    }
                }
            }
            if(!innerList.isEmpty()) {
                filteredCrimes.add(innerList);
            }
        }
        filter = true;
        updateMap(filteredCrimes, filter);
    }

    public void expandFilter(CardView cv){
        if(cv.getHeight() == convertDpToPixel(36,MainActivity.this)) {
            filterImage.animate()
                    .alpha(0)
                    .setDuration(0)
                    .setStartDelay(0)
                    .start();
            new AnimateFilter().expandHeight(cv, MainActivity.this, filterHeight);
            new AnimateFilter().expandWidth(cv, MainActivity.this);
            new AnimateFilter().showAll(filterList,dateRow);
            new AnimateFilter().hideBackground();
        }
    }

    public void shrinkFilter(CardView cv){
        if(cv.getHeight() > convertDpToPixel(36,MainActivity.this)) {
            new AnimateFilter().shrinkHeight(cv, MainActivity.this, filterHeight);
            new AnimateFilter().shrinkWidth(cv, MainActivity.this);
            new AnimateFilter().hideAll(filterList,dateRow);
            new AnimateFilter().showBackground();
            filterImage.animate()
                    .alpha(1)
                    .setDuration(0)
                    .setStartDelay(750)
                    .start();
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("Map", " Ready");
        mMap = googleMap;
        showPosition();
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng newLatLng) {
                hideSoftKeyboard();
                hidePopUpView();
                latLng.setLatLng(newLatLng);
                showPosition();
                shrinkFilter(filterBtn);

            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                hidePopUpView();
                hideSoftKeyboard();
                shrinkFilter(filterBtn);
            }
        });
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
        searchLayout.setVisibility(VISIBLE);
        hideSoftKeyboard();
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
        searchLayout.setVisibility(VISIBLE);
        hideSoftKeyboard();

    }

    //------------------private methods

    private void findAddress() {
        try{
            addresses.clear();
        }catch (Exception e){
            e.printStackTrace();
        }
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.getLatLng().latitude, latLng.getLatLng().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            currentAddress.setAddress(address);
            callNewCrime();
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
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.getLatLng().latitude, latLng.getLatLng().longitude), 15);
        mMap.animateCamera(cameraUpdate);
    }

    public void showPosition() {
        filter = false;
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && new Network().isNetworkEnabled(MainActivity.this)) {
            dialog.setMessage("Getting crimes...");
            dialog.show();
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng.getLatLng(), 15);
            mMap.addMarker(new MarkerOptions().position(latLng.getLatLng()).title("You are here"));
            mMap.animateCamera(cameraUpdate);
            findAddress();
        }else{
            Toast.makeText(getApplicationContext(), "Internet connection required.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private Bitmap getMarkerBitmapFromView(int mapColour, View v) {
        CardView cardView = (CardView) v.findViewById(R.id.custom_marker_view);
        TextView textView = (TextView) v.findViewById(R.id.txtCrimeCount);
        textView.setText(Integer.toString(mapColour));

        if (mapColour <= 2) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color1));
        } else if (mapColour > 2 && mapColour <= 4) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color2));
        } else if (mapColour > 4 && mapColour <= 6) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color3));
        } else if (mapColour > 6 && mapColour <= 8) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color4));
        } else if (mapColour > 8 && mapColour <= 10) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color5));
        } else if (mapColour > 12 && mapColour <= 14) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color6));
        } else if (mapColour > 14 && mapColour <= 16) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color7));
        } else if (mapColour > 16 && mapColour <= 18) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color8));
        } else if (mapColour > 18 && mapColour <= 20) {
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

    public void dismissDialog(final String text) {
        dialog.dismiss();
        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
            }
        });
        try{
            mMap.clear();
        }catch (Exception e){e.printStackTrace();}
    }

    //-----------------------------------------------------------put all markers on map and associate crimes and outcomes with markers
    public void updateMap(final ArrayList<ArrayList<Crimes>> list, boolean filter) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if(!filter) {
            crimeList = list;
        }
        try {
            mMap.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            markers.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<WeightedLatLng> weightedLatLngs = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            int mapColour = list.get(i).size();
            String streetName = new CapitalizeString().getString(list.get(i).get(0).getStreetName().toString());

            LayoutInflater inflater = (LayoutInflater) MainActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.custom_map_marker, null);
            View bigV = inflater.inflate(R.layout.custom_map_marker_big, null);

            if(mapColour < 100) {
                weightedLatLngs.add(new WeightedLatLng(new LatLng(list.get(i).get(0).getLatitude(),list.get(i).get(0).getLongitude()),list.get(i).size()));
                markers.add(mMap.addMarker(new MarkerOptions()
                        .title(streetName)
                        .position(new LatLng(list.get(i).get(0).getLatitude(), list.get(i).get(0).getLongitude()))
                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mapColour, v)))));
                mMap.setOnMarkerClickListener(this);
            }else{
                weightedLatLngs.add(new WeightedLatLng(new LatLng(list.get(i).get(0).getLatitude(),list.get(i).get(0).getLongitude()),100));
                markers.add(mMap.addMarker(new MarkerOptions()
                        .title(streetName)
                        .position(new LatLng(list.get(i).get(0).getLatitude(), list.get(i).get(0).getLongitude()))
                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mapColour, bigV)))));
                mMap.setOnMarkerClickListener(this);
            }
        }

        Toast.makeText(getApplicationContext(), "Crime statistics for " + dateUtil.getMonthAsString() + " " + dateUtil.getYear(),
                Toast.LENGTH_LONG).show();

        // Create the gradient.
        int[] colors = {
                Color.rgb(102, 225, 0), // green
                Color.rgb(255, 0, 0)    // red
        };

        float[] startPoints = {
                0.2f, 1f
        };

        Gradient gradient = new Gradient(colors, startPoints);

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        mProvider = new HeatmapTileProvider.Builder()
                .weightedData(weightedLatLngs)
                .gradient(gradient)
                .radius(50)
                .build();


    }

    public void setFilterViews(ArrayList<Counter> list){
        int count = 0;
        for (int i = 0; i < filterList.size(); i++) {
            if (i < list.size()) {
                count++;
                filterList.get(i).getCardView().setVisibility(VISIBLE);
                filterList.get(i).getName().setText(list.get(i).getName());
                filterList.get(i).setNameString(list.get(i).getName());
                filterList.get(i).getCardView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        filter((CardView) view);
                    }
                });
            } else {
                filterList.get(i).getName().setText("");
                filterList.get(i).getCardView().setVisibility(GONE);
            }
        }

        filterHeight = count / 4;
        if(count % 4 > 0){
            filterHeight++;
        }
        filterHeight = filterHeight * convertDpToPixel(70, MainActivity.this) + convertDpToPixel(55, MainActivity.this);;
    }

    public void filter(CardView view){
        if(view.getAlpha() == 1){
            view.setAlpha(0.5f);
            for (FilterItem filterItem : filterList){
                if(filterItem.getCardView() == view){
                    filterItem.setShow(false);
                }
            }
        }else{
            view.setAlpha(1);
            for (FilterItem filterItem : filterList){
                if(filterItem.getCardView() == view){
                    filterItem.setShow(true);
                }
            }
        }


    }

    //-----------------------------------------------------------code for marker selected
    @Override
    public boolean onMarkerClick(Marker marker) {
        hideSoftKeyboard();
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
        ArrayList<ArrayList<Crimes>> temp;
        if(filter){
            temp = filteredCrimes;
        }else{
            temp = crimeList;
        }
        markerCrimes = new ArrayList<>();
        ArrayList<Counter> counts = new ArrayList<>();
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).get(0).getLongitude() == marker.getPosition().longitude &&
                    temp.get(i).get(0).getLatitude() == marker.getPosition().latitude) {
                location = new CapitalizeString().getString(temp.get(i).get(0).getStreetName());
                for (int j = 0; j < temp.get(i).size(); j++) {

                    markerCrimes.add(new Crimes(
                            temp.get(i).get(j).getCrimeType(),
                            temp.get(i).get(j).getDate(),
                            temp.get(i).get(j).getTime(),
                            temp.get(i).get(j).getOutcome(), location,
                            temp.get(i).get(j).getLatitude(),
                            temp.get(i).get(j).getLongitude(),
                            temp.get(i).get(j).getWeapon(),
                            temp.get(i).get(j).getDescription()));

                    if (counts.isEmpty()) {
                        counts.add(new Counter(temp.get(i).get(j).getCrimeType(), 1));
                        continue;
                    }
                    for (int k = 0; k < counts.size(); k++) {
                        if (counts.get(k).getName().equalsIgnoreCase(temp.get(i).get(j).getCrimeType())) {
                            int tempCount = counts.get(k).getCount();
                            counts.set(k, new Counter(temp.get(i).get(j).getCrimeType(), ++tempCount));
                            break;
                        }
                        if (k == counts.size() - 1) {
                            counts.add(new Counter(temp.get(i).get(j).getCrimeType(), 1));
                            break;
                        }
                    }
                }
                break;
            }
        }


        streetName.setText(location.trim());
        areaTotalsTitle.setText(location.trim());
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
                areas = areas.substring(0, 1).toUpperCase() + areas.substring(1).toLowerCase();
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
            email.setOnLongClickListener(new View.OnLongClickListener(){
                public boolean onLongClick(View v) {
                    return true;
                }
            });
        } else {
            email.setVisibility(GONE);
        }
        if (null != facebookString && !facebookString.equals("")) {
            facebook.setVisibility(VISIBLE);
            facebook.setText("Facebook: " + facebookString);
            addLinks(facebook, Linkify.WEB_URLS);
            facebook.setLinkTextColor(ContextCompat.getColor(this, R.color.link));
            facebook.setOnLongClickListener(new View.OnLongClickListener(){
                public boolean onLongClick(View v) {
                    return true;
                }
            });
        } else {
            facebook.setVisibility(GONE);
        }
        if (null != twitterString && !twitterString.equals("")) {
            twitter.setVisibility(VISIBLE);
            twitter.setText("Twitter: " + twitterString);
            addLinks(twitter, Linkify.WEB_URLS);
            twitter.setLinkTextColor(ContextCompat.getColor(this, R.color.link));
            twitter.setOnLongClickListener(new View.OnLongClickListener(){
                public boolean onLongClick(View v) {
                    return true;
                }
            });
        } else {
            twitter.setVisibility(GONE);
        }
        if (null != websiteString && !websiteString.equals("")) {
            website.setVisibility(VISIBLE);
            website.setText("Website: " + websiteString);
            addLinks(website, Linkify.WEB_URLS);
            website.setLinkTextColor(ContextCompat.getColor(this, R.color.link));
            website.setOnLongClickListener(new View.OnLongClickListener(){
                public boolean onLongClick(View v) {
                    return true;
                }
            });
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
        lp.height = (int) getMeasuredHeight(layoutBody) - statusBarHeight < getScreenHeight(this) ? getScreenHeight(this) : (int) getMeasuredHeight(layoutBody) - statusBarHeight;
        layoutBody.setLayoutParams(lp);

    }

    public void setWeather(String description, Double curentTemp) {
        char tmp = 0x00B0;
        weather.setText(new CapitalizeString().getString(description) + " " + curentTemp.intValue() + tmp);
    }

    @Override
    public void onBackPressed() {
        if(filterBtn.getHeight() > convertDpToPixel(36,MainActivity.this)) {
            new AnimateFilter().shrinkHeight(filterBtn, MainActivity.this, filterHeight);
            new AnimateFilter().shrinkWidth(filterBtn, MainActivity.this);
            new AnimateFilter().hideAll(filterList,dateRow);
            new AnimateFilter().showBackground();
            filterImage.animate()
                    .alpha(1)
                    .setDuration(0)
                    .setStartDelay(750)
                    .start();
        } else if (layoutTitle.getY() == adViewHeight) {
            showPopUpViewTitle();
        } else if (layoutTitle.getY() == getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - statusBarHeight) {
            hidePopUpView();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
                topOfTitle = layoutTitle.getY();
                topOfBody = layoutBody.getY();
                titleHeight = layoutTitle.getHeight();
                bodyMeasuredHeight = getMeasuredHeight(informationLayout, titleHeight, adViewHeight);
                float movementAmmount = startingPosition - previousPosition;
                System.out.println("click count = " + isClickCount);

                //on click
                if (isClickCount < 5) {
                    if (topOfTitle <= adViewHeight + 30 && topOfTitle >= adViewHeight - 30) {
                        showPopUpViewTitle();
                    } else if (topOfTitle <= getScreenHeight(MainActivity.this) - titleHeight - statusBarHeight + 30 &&
                            topOfTitle >= getScreenHeight(MainActivity.this) - titleHeight - statusBarHeight - 30) {
                        layoutTitle.animate()
                                .y(adViewHeight)
                                .setDuration(250)
                                .start();
                        layoutBody.animate()
                                .y(adViewHeight)
                                .setDuration(250)
                                .start();
                        mAdView.setVisibility(VISIBLE);
                        adRequest = new AdRequest.Builder().build();
                        mAdView.loadAd(adRequest);
                        searchLayout.setVisibility(GONE);
                    }
                }
                // on move
                else if (movementAmmount > 200
                        && topOfTitle > getScreenHeight(MainActivity.this) / 4
                        && topOfTitle != 0) {
                    System.out.println("1 = " + (movementAmmount));
                    layoutTitle.animate()
                            .y(adViewHeight)
                            .setDuration(250)
                            .start();
                    layoutBody.animate()
                            .y(adViewHeight)
                            .setDuration(250)
                            .start();
                    mAdView.setVisibility(VISIBLE);
                    adRequest = new AdRequest.Builder().build();
                    mAdView.loadAd(adRequest);
                    searchLayout.setVisibility(GONE);
                } else if (!(movementAmmount > 200) && movementAmmount > 0
                        && topOfTitle > getScreenHeight(MainActivity.this) / 4
                        && topOfTitle != 0) {
                    System.out.println("2 = " + (movementAmmount));
                    showPopUpViewTitle();
                }
                //on move

                else if (movementAmmount < -200
                        && topOfTitle > getScreenHeight(MainActivity.this) / 5
                        && topOfTitle != getScreenHeight(MainActivity.this) - titleHeight
                        && topOfBody >= 0) {
                    System.out.println("3 = " + (movementAmmount) + " / " + topOfTitle);
                    showPopUpViewTitle();
                }
                else if (movementAmmount > -200 && movementAmmount < 0
                        && topOfTitle < getScreenHeight(MainActivity.this) - getScreenHeight(MainActivity.this) / 4
                        && topOfTitle != getScreenHeight(MainActivity.this) - titleHeight
                        && topOfBody >= 0) {
                    System.out.println("4 = " + (movementAmmount) + " / " + topOfTitle);
                    layoutTitle.animate()
                            .y(adViewHeight)
                            .setDuration(250)
                            .start();
                    layoutBody.animate()
                            .y(adViewHeight)
                            .setDuration(250)
                            .start();
                    mAdView.setVisibility(VISIBLE);
                    adRequest = new AdRequest.Builder().build();
                    mAdView.loadAd(adRequest);
                    searchLayout.setVisibility(GONE);
                }
                else if (movementAmmount < -200
                        && topOfTitle < getScreenHeight(MainActivity.this) - getScreenHeight(MainActivity.this) / 4
                        && topOfTitle != getScreenHeight(MainActivity.this) - titleHeight
                        && topOfBody >= 0) {
                    System.out.println("5 = " + (movementAmmount) + " / " + topOfTitle);
                    layoutTitle.animate()
                            .y(adViewHeight)
                            .setDuration(250)
                            .start();
                    layoutBody.animate()
                            .y(adViewHeight)
                            .setDuration(250)
                            .start();
                    mAdView.setVisibility(VISIBLE);
                    adRequest = new AdRequest.Builder().build();
                    mAdView.loadAd(adRequest);
                    searchLayout.setVisibility(GONE);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                topOfTitle = layoutTitle.getY();
                topOfBody = layoutBody.getY();
                titleHeight = layoutTitle.getHeight();
                bodyMeasuredHeight = getMeasuredHeight(informationLayout, titleHeight, adViewHeight);
                isClickCount++;
                distanceMoved = previousPosition - event.getRawY();
                //moving up
                if (distanceMoved > 0) {
                    //if you have scrolled to the end of the line
                    if (topOfTitle <= adViewHeight && topOfBody + bodyMeasuredHeight - distanceMoved <= getScreenHeight(MainActivity.this)) {
                        System.out.println("have scrolled to the end of the line");
                        layoutTitle.animate()
                                .y(adViewHeight)
                                .setDuration(0)
                                .start();
                        layoutBody.animate()
                                .y(getScreenHeight(MainActivity.this) - bodyMeasuredHeight)
                                .setDuration(0)
                                .start();
                    }
                    //keep title still but move body up
                    else if (topOfTitle <= adViewHeight && topOfBody + bodyMeasuredHeight - distanceMoved > getScreenHeight(MainActivity.this)) {
                        System.out.println("keep title still but move body up");
                        layoutTitle.animate()
                                .y(adViewHeight)
                                .setDuration(0)
                                .start();
                        layoutBody.animate()
                                .y(topOfBody - distanceMoved)
                                .setDuration(0)
                                .start();

                    }
                    //move both title and body up
                    else {
                        System.out.println("move both title and body up");
                        layoutTitle.setY(layoutBody.getY());
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
                    //move body keep title still
                    if (topOfTitle <= adViewHeight && topOfBody - distanceMoved < topOfTitle) {
                        System.out.println("move body keep title still");
                        layoutTitle.animate()
                                .y(adViewHeight)
                                .setDuration(0)
                                .start();
                        layoutBody.animate()
                                .y(topOfBody - distanceMoved)
                                .setDuration(0)
                                .start();
                    }
                    //move both down
                    else if(topOfTitle - distanceMoved < getScreenHeight(MainActivity.this) - titleHeight - statusBarHeight){
                        System.out.println("move both down");
                        layoutTitle.setY(layoutBody.getY());
                        layoutTitle.animate()
                                .y(topOfTitle - distanceMoved)
                                .setDuration(0)
                                .start();
                        layoutBody.animate()
                                .y(topOfBody - distanceMoved)
                                .setDuration(0)
                                .start();
                    }
                    //at bottom
                    else if(topOfTitle - distanceMoved >= getScreenHeight(MainActivity.this) - titleHeight - statusBarHeight){
                        System.out.println("at bottom");
                        layoutTitle.animate()
                                .y(getScreenHeight(MainActivity.this) - titleHeight - statusBarHeight)
                                .setDuration(0)
                                .start();
                        layoutBody.animate()
                                .y(getScreenHeight(MainActivity.this) - titleHeight - statusBarHeight)
                                .setDuration(0)
                                .start();
                    }

                }
                previousPosition = event.getRawY();

                break;
        }
        return false;
    }

    @Override
    public void networkAvailable() {
//        if(latLng.getLatLng().latitude != 0.0d && latLng.getLatLng().longitude != 0.0d && new Network().isNetworkEnabled(MainActivity.this)){
//
//        }
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
/*
    public static class ListenerEditText extends android.support.v7.widget.AppCompatEditText {

        private KeyImeChange keyImeChangeListener;

        public ListenerEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public void setKeyImeChangeListener(KeyImeChange listener) {
            keyImeChangeListener = listener;
        }

        public interface KeyImeChange {
            public void onKeyIme(int keyCode, KeyEvent event);
        }

        @Override
        public boolean onKeyPreIme(int keyCode, KeyEvent event) {
            if (keyImeChangeListener != null) {
                keyImeChangeListener.onKeyIme(keyCode, event);
            }
            return false;
        }
    }
*/
    private void hideSoftKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
        search.setCursorVisible(false);
        search.clearFocus();

    }
}