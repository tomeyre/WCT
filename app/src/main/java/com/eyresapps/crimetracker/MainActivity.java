package com.eyresapps.crimetracker;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eyresapps.api_calls.GetCurrentWeather;
import com.eyresapps.broadcastRecievers.Network;
import com.eyresapps.broadcastRecievers.NetworkStateReceiver;
import com.eyresapps.crimeLocations.GenerateCrimeUrl;
import com.eyresapps.data.Counter;
import com.eyresapps.data.CrimeCount;
import com.eyresapps.data.Crimes;
import com.eyresapps.data.CurrentLocale;
import com.eyresapps.data.FilterItem;
import com.eyresapps.utils.AnimateFilter;
import com.eyresapps.utils.BitmapGenerator;
import com.eyresapps.utils.CapitalizeString;
import com.eyresapps.utils.CrimeCountList;
import com.eyresapps.utils.CurrentAddressUtil;
import com.eyresapps.utils.DateUtil;
import com.eyresapps.utils.FilterCrimeList;
import com.eyresapps.utils.FilterList;
import com.eyresapps.utils.FindSearchLocation;
import com.eyresapps.utils.GPSTrackerUtil;
import com.eyresapps.utils.LatitudeAndLongitudeUtil;
import com.eyresapps.utils.ReadWriteStuff;
import com.eyresapps.utils.ScreenUtils;
import com.eyresapps.utils.UpdateMap;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.eyresapps.utils.ScreenUtils.convertDpToPixel;
import static com.eyresapps.utils.ScreenUtils.getMeasuredHeight;
import static com.eyresapps.utils.ScreenUtils.getScreenHeight;
import static com.eyresapps.utils.ScreenUtils.getStatusBarHeight;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        NetworkStateReceiver.NetworkStateReceiverListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private Integer animationTime = 350;
    private boolean timeRunning = false;
    private boolean haveCheckedIdleness = false;
    private int onLoad = 0;

    private Button resetBtn;
    private Button closeBtn;

    private CardView locateMe;

    private FilterList filterList = FilterList.getInstance();

    private TextView dateTxt;

    boolean heatMapUsed = false;

    boolean isMyLocation = true;

    boolean filterByCrime = false;
    boolean filterByMonth = false;

    float filterHeight;
    LinearLayout dateRow;
    LinearLayout btnRow;
    LinearLayout rowOne;
    LinearLayout rowTwo;
    LinearLayout rowThree;
    LinearLayout rowFour;
    LinearLayout rowFive;
    LinearLayout rowSix;
    ArrayList<LinearLayout> rows;


    ArrayList<ArrayList<Crimes>> filteredCrimes;
    ImageView filterImage;
    Button filterSearchBtn;

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

    //--------------------getting crimes ect gettingCrimesDialog display
    private ProgressDialog updatingMapDialog;

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
    private TextView time;
    //    private TextView about;
//    private TextView aboutTitle;
//    private TextView areaTitle;
//    private TextView email;
//    private TextView website;
//    private TextView facebook;
//    private TextView twitter;
    private TextView weather;

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

    //------------ all used for getting local timeOccur to display below the street name like google does
    private DateUtil dateUtil = DateUtil.getInstance();
    private Handler timeHandler;
    private Runnable timeRunner;
    private CurrentLocale locale = CurrentLocale.getInstance();

    //--------- cards to remove if no information inside them
    private CardView crimesCardView;
    private CardView aboutCardView;
    private CardView socialMediaCardView;

    private AdView mAdView;
    private AdRequest adRequest;

    private Button additionalInformationBtn;

    private ArrayList<String> months;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_main);

        dateTxt  = findViewById(R.id.dateTxt);

        closeBtn = findViewById(R.id.closeBtn);
        resetBtn = findViewById(R.id.resetBtn);

        locateMe = findViewById(R.id.locateMe);
        locateMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locateMeNow();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AnimateFilter().shrinkFilter(filterBtn, filterImage, MainActivity.this, filterHeight, filterList.getFilterList(), dateRow, btnRow,
                        monthSpinner, yearSpinner, filterSearchBtn);
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetFilter();
            }
        });

        setViews();

        //----rows i THINK these are used for filtering----------------------------------------------------------------------
        rows = new ArrayList<>();
        rows.add(rowOne);
        rows.add(rowTwo);
        rows.add(rowThree);
        rows.add(rowFour);
        rows.add(rowFive);
        rows.add(rowSix);

        // this is for setting the spinners based on current date--------------------------------------------------------------
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.
                createFromResource(this, R.array.days, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        months = new ArrayList<>();
        months.add(getResources().getStringArray(R.array.months)[0]);
        months.add(getResources().getStringArray(R.array.months)[1]);
        months.add(getResources().getStringArray(R.array.months)[2]);
        months.add(getResources().getStringArray(R.array.months)[3]);
        months.add(getResources().getStringArray(R.array.months)[4]);
        months.add(getResources().getStringArray(R.array.months)[5]);
        months.add(getResources().getStringArray(R.array.months)[6]);
        months.add(getResources().getStringArray(R.array.months)[7]);
        months.add(getResources().getStringArray(R.array.months)[8]);
        months.add(getResources().getStringArray(R.array.months)[9]);
        months.add(getResources().getStringArray(R.array.months)[10]);
        months.add(getResources().getStringArray(R.array.months)[11]);
        ArrayAdapter<String> adapterMonths = new ArrayAdapter<String>(this,
                R.layout.spinner_item, months);
        adapterMonths.setDropDownViewResource(R.layout.spinner_item_layout);
        monthSpinner.setAdapter(adapterMonths);
        final Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        ArrayList<Integer> years = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            years.add(year--);
        }
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, years);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item_layout);
        yearSpinner.setAdapter(arrayAdapter);
        yearSpinner.setSelection(0);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    for (int i = 11; i > dateUtil.getCurrentMonth(); i--) {
                        months.remove(i);
                    }
                    ArrayAdapter<String> adapterMonths = new ArrayAdapter<String>(MainActivity.this,
                            R.layout.spinner_item, months);
                    adapterMonths.setDropDownViewResource(R.layout.spinner_item_layout);
                    monthSpinner.setAdapter(adapterMonths);
                } else if (monthSpinner.getChildCount() < 11 && position > 0) {
                    int currentPos = monthSpinner.getSelectedItemPosition();
                    months.clear();
                    months.add(getResources().getStringArray(R.array.months)[0]);
                    months.add(getResources().getStringArray(R.array.months)[1]);
                    months.add(getResources().getStringArray(R.array.months)[2]);
                    months.add(getResources().getStringArray(R.array.months)[3]);
                    months.add(getResources().getStringArray(R.array.months)[4]);
                    months.add(getResources().getStringArray(R.array.months)[5]);
                    months.add(getResources().getStringArray(R.array.months)[6]);
                    months.add(getResources().getStringArray(R.array.months)[7]);
                    months.add(getResources().getStringArray(R.array.months)[8]);
                    months.add(getResources().getStringArray(R.array.months)[9]);
                    months.add(getResources().getStringArray(R.array.months)[10]);
                    months.add(getResources().getStringArray(R.array.months)[11]);
                    ArrayAdapter<String> adapterMonths = new ArrayAdapter<String>(MainActivity.this,
                            R.layout.spinner_item, months);
                    adapterMonths.setDropDownViewResource(R.layout.spinner_item_layout);
                    monthSpinner.setAdapter(adapterMonths);
                    monthSpinner.setSelection(currentPos);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        //filterByCrime button inside of the filterByCrime cardview------------------------------------------------------------
        filterSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filterByCrime = false;
                for(FilterItem filterItem : filterList.getFilterList()){
                    if(!filterItem.getShow()) {
                        filterByCrime = true;
                        break;
                    }
                }

                if((monthSpinner.getSelectedItemPosition()+1) != dateUtil.getMonth() ||
                        Integer.parseInt(yearSpinner.getSelectedItem().toString()) !=
                                (dateUtil.getYear()) && filterByCrime){
                    filterByMonth = true;
                    latLng.setLatlngChaned(false);
                    dateUtil.setMonth(monthSpinner.getSelectedItemPosition()+1);
                    dateUtil.setYear(Integer.parseInt(yearSpinner.getSelectedItem().toString()));
                    showPosition(true);
                    new AnimateFilter().shrinkFilter(filterBtn, filterImage,
                            MainActivity.this, filterHeight, filterList.getFilterList(),
                            dateRow, btnRow, monthSpinner, yearSpinner, filterSearchBtn);
                }
                else if ((monthSpinner.getSelectedItemPosition()+1) != dateUtil.getMonth() ||
                        Integer.parseInt(yearSpinner.getSelectedItem().toString()) !=
                                (dateUtil.getYear())) {
                    latLng.setLatlngChaned(false);
                    dateUtil.setMonth(monthSpinner.getSelectedItemPosition()+1);
                    dateUtil.setYear(Integer.parseInt(yearSpinner.getSelectedItem().toString()));
                    filteredCrimes = new FilterCrimeList().filter(crimeList,
                            filterList.getFilterList());
                    filterByMonth = true;
                    //filterCrimeListUpdate(true);
                    showPosition(true);
                    new AnimateFilter().shrinkFilter(filterBtn, filterImage,
                            MainActivity.this, filterHeight, filterList.getFilterList(),
                            dateRow, btnRow, monthSpinner, yearSpinner, filterSearchBtn);
                } else if(filterByCrime){
                    filterByMonth = false;
                    filteredCrimes = new FilterCrimeList().filter(crimeList, filterList.getFilterList());
                    filterCrimeListUpdate();
                    new AnimateFilter().shrinkFilter(filterBtn, filterImage,
                            MainActivity.this, filterHeight, filterList.getFilterList(),
                            dateRow, btnRow, monthSpinner, yearSpinner, filterSearchBtn);
                }else{
                    filterByCrime = true;
                    filterByMonth = false;
                    filteredCrimes = new FilterCrimeList().filter(crimeList, filterList.getFilterList());
                    filterCrimeListUpdate();
                    new AnimateFilter().shrinkFilter(filterBtn, filterImage,
                            MainActivity.this, filterHeight, filterList.getFilterList(),
                            dateRow, btnRow, monthSpinner, yearSpinner, filterSearchBtn);
                }
            }
        });
        filterSearchBtn.setEnabled(false);
        filterSearchBtn.setClickable(false);

        //--- set adview height---------------------------------------------------------------------------------------------------------------------------
        adViewHeight = ScreenUtils.convertDpToPixel(50, this);

        //----location search when clicked hide other stuff-------------------------------------------------------------------------------------------------
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AnimateFilter().shrinkFilter(filterBtn, filterImage, MainActivity.this,
                        filterHeight, filterList.getFilterList(), dateRow, btnRow,
                        monthSpinner, yearSpinner, filterSearchBtn);
            }
        });

        //-----allows you to initiate search with the enter key-----------------------------------------------------------------------------------------------
        hideSoftKeyboard();
        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!search.getText().toString().trim().equals("")) {
                        resetFilter();
                        isMyLocation = false;
                        filterByCrime = false;
                        new FindSearchLocation(MainActivity.this, search.getText().toString()).execute();
                    }
                    search.setText("");
                    search.clearFocus();
                    hidePopUpView();
                    hideSoftKeyboard();
                    return true;
                }
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DEL) {
                    if (search.getText().length() > 0) {
                        search.setText(search.getText().delete(search.getText().length() - 1, search.getText().length()));
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
                getResources().getString(R.string.test));
        adRequest = new AdRequest.Builder().build();
        new AnimateFilter().hideAdView(mAdView, this);


        //---------set up gps tracker and get lat long
        gpsTracker = new GPSTrackerUtil(this);
        latLng.setLatLng(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()));
        latLng.setLatlngChaned(true);

        //---------------find all views to be manipulated on the front end


        //--------
        additionalInformationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mock data for testing

                /*markerCrimes.add(new Crimes("TWAT","21/10/2017","10:10","arrest","balls",100,100,"nope","shit"));
                markerCrimes.add(new Crimes("TWAT","21/10/2017","10:10","arrest","balls",100,100,"nope","shit"));
                markerCrimes.add(new Crimes("TWAT","21/10/2017","10:10","arrest","balls",100,100,"nope","shit"));
                markerCrimes.add(new Crimes("TWAT","21/10/2017","10:10","arrest","balls",100,100,"nope","shit"));*/

                Intent intent = new Intent(MainActivity.this, AdditionalInformation.class);
                intent.putExtra("crimes", markerCrimes);
                startActivity(intent);
            }
        });


        updatingMapDialog = new ProgressDialog(MainActivity.this);
        updatingMapDialog.setMessage("Updating map...");

        heatMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AnimateFilter().shrinkFilter(filterBtn, filterImage, MainActivity.this, filterHeight, filterList.getFilterList(), dateRow, btnRow,
                        monthSpinner, yearSpinner, filterSearchBtn);
                hideSoftKeyboard();
                heatMapUsed = true;
                if (markers.isEmpty()) {
                    if (filterByCrime) {
                        if (null != filteredCrimes && !filteredCrimes.isEmpty()) {
                            new UpdateMap(MainActivity.this,filteredCrimes).execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "All crimes filtered...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        new UpdateMap(MainActivity.this,crimeList).execute();
                    }
                } else {
                    try {
                        mMap.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    markers.clear();
                    if (!filterByCrime) {
                        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                    } else if (filterByCrime && null != filteredCrimes && !filteredCrimes.isEmpty()) {
                        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                    } else {
                        Toast.makeText(getApplicationContext(), "All crimes filtered...",
                                Toast.LENGTH_SHORT).show();
                    }
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
        lp.height = layoutBody.getHeight() < (getScreenHeight(this) - adViewHeight) ? (int) (getScreenHeight(this) - adViewHeight) : layoutBody.getHeight();
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
                .y(getScreenHeight(MainActivity.this) + convertDpToPixel(100, this))
                .setDuration(animationTime)
                .setStartDelay(2000)
                .start();
        layoutBody.animate()
                .y(getScreenHeight(MainActivity.this) + convertDpToPixel(100, this))
                .setDuration(animationTime)
                .setStartDelay(2000)
                .start();

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterBtn.getHeight() > convertDpToPixel(36, MainActivity.this)) {
//                    new AnimateFilter().shrinkFilter(filterBtn, filterImage, MainActivity.this, filterHeight, filterList.getFilterList(), dateRow, btnRow,
//                            monthSpinner, yearSpinner, filterSearchBtn);
                } else {
                    hideSoftKeyboard();
                    hidePopUpView();
                    new AnimateFilter().expandFilter(filterBtn, filterImage, MainActivity.this, filterHeight, filterList.getFilterList(), dateRow, btnRow,
                            monthSpinner, yearSpinner, filterSearchBtn);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(dateRow.getAlpha() != 1 || !monthSpinner.isEnabled() || !yearSpinner.isEnabled() || monthSpinner.getAdapter().isEmpty() || yearSpinner.getAdapter().isEmpty()){
                                handler.postDelayed(this,100);
                            }else {
                                dateRow.setVisibility(View.VISIBLE);
                                final Handler handler = new Handler();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        yearSpinner.setSelection(dateUtil.getMaxYear() - dateUtil.getYear());
                                        monthSpinner.setSelection(dateUtil.getMonth() - 1);
                                    }
                                });
                            }

                        }
                    },100);

                }
            }
        });

    }

    private void setViews() {
        //--rows
        dateRow = findViewById(R.id.dateRow);
        btnRow = findViewById(R.id.btnRow);
        rowOne = findViewById(R.id.rowOne);
        rowTwo = findViewById(R.id.rowTwo);
        rowThree = findViewById(R.id.rowThree);
        rowFour = findViewById(R.id.rowFour);
        rowFive = findViewById(R.id.rowFive);
        rowSix = findViewById(R.id.rowSix);

        //-----Spinners
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);

        //------ filterByCrime button on main screen
        filterBtn = findViewById(R.id.filterBtn);
        filterImage = findViewById(R.id.bntFilter);
        filterSearchBtn = findViewById(R.id.filterSearchBtn);

        //---- ad view
        mAdView = findViewById(R.id.adView);

        //----- search stuff
        searchLayout = findViewById(R.id.searchLayout);
        search = findViewById(R.id.search);

        //-----heat map button on main screen
        heatMapBtn = findViewById(R.id.heatMapBtn);
        btnImage = findViewById(R.id.btnImage);

        //------additional information button located on the pop up information
        additionalInformationBtn = findViewById(R.id.additionalInformationBtn);

        //------- pop up information title
        layoutTitle = findViewById(R.id.informationCardView);

        //----------pop up information body
        layoutBody = findViewById(R.id.informationLayout);

        //-----stuff not sure yet
        informationLayout = findViewById(R.id.informationLayout);
        streetName = findViewById(R.id.streetName);
        crimesCardView = findViewById(R.id.crimesCardView);
        time = findViewById(R.id.time);

        //----- weather show in the pop up by area
        weather = findViewById(R.id.weather);

        //--------stuff
        areaTotalsTitle = findViewById(R.id.areaTotalsTitle);
        crimesTitle = findViewById(R.id.crimesTitle);

    }

    private void filterCrimeListUpdate() {
        if (!filteredCrimes.isEmpty()) {
            new UpdateMap(this,filteredCrimes).execute();
        } else {
            mMap.clear();
            Toast.makeText(getApplicationContext(), "All crimes filtered...",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void resetFilter() {
        if(null != filteredCrimes && !filteredCrimes.isEmpty()) {
            filteredCrimes.clear();
        }
        for (FilterItem filterItem : filterList.getFilterList()) {
            filterItem.setShow(true);
            filterItem.getCardView().setAlpha(1);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("Map", " Ready");
        mMap = googleMap;
        mMap.setIndoorEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        filterByCrime = false;
        filterByMonth = false;
        showPosition(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng newLatLng) {
                latLng.setLatlngChaned(true);
                isMyLocation = false;
                hideSoftKeyboard();
                hidePopUpView();
                latLng.setLatLng(newLatLng);
                filterByCrime = false;
                filterByMonth = false;
                showPosition(false);
                new AnimateFilter().shrinkFilter(filterBtn, filterImage, MainActivity.this, filterHeight, filterList.getFilterList(), dateRow, btnRow,
                        monthSpinner, yearSpinner, filterSearchBtn);

            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                hidePopUpView();
                hideSoftKeyboard();
                new AnimateFilter().shrinkFilter(filterBtn, filterImage, MainActivity.this, filterHeight, filterList.getFilterList(), dateRow, btnRow,
                        monthSpinner, yearSpinner, filterSearchBtn);
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                try {
                    if (!haveCheckedIdleness) {
                        final int subArrayMaxSize = 100;
                        final int ammountOfSubArrays;
                        ammountOfSubArrays = markers.size() / subArrayMaxSize == 0 ? 1 : markers.size() % subArrayMaxSize > 0 ? (markers.size() / subArrayMaxSize) + 1 : markers.size() / subArrayMaxSize;
                        for (int j = 0; j < ammountOfSubArrays; j++) {
                            final int position = j;
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    if ((position + 1) == ammountOfSubArrays && ammountOfSubArrays == 1) {
                                        checkMarkerVisibility(0, markers.size() - 1);
                                    } else if ((position + 1) == ammountOfSubArrays) {
                                        checkMarkerVisibility((position * subArrayMaxSize), markers.size() - 1);
                                    } else {
                                        checkMarkerVisibility((position * subArrayMaxSize), (position + 1) * subArrayMaxSize);
                                    }
                                }
                            };
                            thread.start();

                        }
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    private void checkMarkerVisibility(final int startArray, final int endArray) {
        // Get a handler that can be used to post to the main thread

        try {
            for (int i = startArray; i < endArray; i++) {
                final int position = i;
                Handler mainHandler = new Handler(Looper.getMainLooper());

                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                        if (bounds.contains(markers.get(position).getPosition())) {
                            markers.get(position).setVisible(true);
                        } else {
                            markers.get(position).setVisible(false);
                        }
                    }
                };
                mainHandler.post(myRunnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void hidePopUpView() {
        layoutTitle.animate()
                .y(getScreenHeight(MainActivity.this) + convertDpToPixel(100, MainActivity.this))
                .setDuration(animationTime)
                .start();
        layoutBody.animate()
                .y(getScreenHeight(MainActivity.this) + convertDpToPixel(100, MainActivity.this))
                .setDuration(animationTime)
                .start();
        mAdView.destroy();
        new AnimateFilter().hideAdView(mAdView, this);
        searchLayout.setVisibility(VISIBLE);
        if(null != crimeList && !crimeList.isEmpty()) {
            dateTxt.setVisibility(VISIBLE);
        }
        hideSoftKeyboard();
    }

    public void showPopUpViewTitle() {
        layoutTitle.animate()
                .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - statusBarHeight)
                .setDuration(animationTime)
                .start();
        layoutBody.animate()
                .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - statusBarHeight)
                .setDuration(animationTime)
                .start();
        mAdView.destroy();
        new AnimateFilter().hideAdView(mAdView, this);
        searchLayout.setVisibility(VISIBLE);
        if(null != crimeList && !crimeList.isEmpty()) {
            dateTxt.setVisibility(VISIBLE);
        }
        hideSoftKeyboard();

    }

    //------------------private methods

    private void findAddress(boolean alreadyHaveAddress) {
        boolean notSaved = true;
        JSONArray storedLocations = new JSONArray();
        if(null != addresses && !addresses.isEmpty() && !alreadyHaveAddress){
            addresses.clear();
        }
        if (!alreadyHaveAddress && latLng.isLatlngChaned()) {
            try {
                String str = new ReadWriteStuff().readFromFile(MainActivity.this);
                storedLocations = new JSONArray(str);
                if (storedLocations != null) {
                    DecimalFormat df = new DecimalFormat("#.####");
                    df.setRoundingMode(RoundingMode.CEILING);
                    Double lat = latLng.getLatLng().latitude;
                    Double lon = latLng.getLatLng().longitude;
                    for (int i = 0; i < storedLocations.length(); i++) {
                        if (storedLocations.getJSONObject(i).getString("lat").equals(df.format(lat)) && storedLocations.getJSONObject(i).getString("long").equals(df.format(lon))) {
                            currentAddress.setAddress(storedLocations.getJSONObject(i).getString("location"));
                            notSaved = false;
                            callNewCrime();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (alreadyHaveAddress) {
            callNewCrime();
        }
        if (notSaved && !alreadyHaveAddress) {
            geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latLng.getLatLng().latitude, latLng.getLatLng().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                currentAddress.setAddress(address);
                callNewCrime();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Oh no I can't find you!",
                        Toast.LENGTH_SHORT).show();
            }
        }

        final Date date = new Date();
        final DateFormat df = new SimpleDateFormat("h:mm a");

        if (null != currentAddress.getAddress() && currentAddress.getAddress().toLowerCase().contains("uk")) {
            locale.setLocale(Locale.UK);
            df.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        } else if (null != currentAddress.getAddress() && currentAddress.getAddress().toLowerCase().contains("los angeles")) {
            locale.setLocale(Locale.US);
            df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        } else if (null != currentAddress.getAddress() && currentAddress.getAddress().toLowerCase().contains("chicago")) {
            locale.setLocale(Locale.US);
            df.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
        } else if (null != currentAddress.getAddress() && currentAddress.getAddress().toLowerCase().contains("durham, nc")) {
            locale.setLocale(Locale.US);
            df.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        } else if (null != currentAddress.getAddress() && currentAddress.getAddress().toLowerCase().contains("new orleans")) {
            locale.setLocale(Locale.US);
            df.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
        } else {
            locale.setLocale(Locale.UK);
            df.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        }

        latLng.setLatlngChaned(false);
        if(timeRunning){
            timeHandler.removeCallbacks(timeRunner);
            timeRunning = false;
        }
        timeHandler = new Handler(getMainLooper());
        timeRunner = new Runnable() {
            @Override
            public void run() {
                timeRunning = true;
                time.setText(df.format(date));
                timeHandler.postDelayed(this, 1000);
            }
        };
        timeHandler.postDelayed(timeRunner, 10);
    }

    private void callNewCrime() {
        if (!filterByMonth && !filterByCrime) {
            dateUtil.resetDate();
        }
        heatMapUsed = false;
        new GenerateCrimeUrl(MainActivity.this, filterByCrime, filterByMonth);
        if (latLng.isLatlngChaned()) {
            dateUtil.setMaxMonth(dateUtil.getMonth());
            dateUtil.setMaxYear(dateUtil.getYear());
            new GetCurrentWeather(MainActivity.this, latLng.getLatLng()).execute();
        }
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.getLatLng().latitude, latLng.getLatLng().longitude), 16);
        mMap.animateCamera(cameraUpdate);
    }

    public void showPosition(boolean alreadyHaveAdress) {

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && new Network().isNetworkEnabled(MainActivity.this)) {
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng.getLatLng(), 16);
            mMap.animateCamera(cameraUpdate);
            findAddress(alreadyHaveAdress);
        } else {
            Toast.makeText(getApplicationContext(), "Internet connection required.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    //------------public methods

    public void dismissDialog(final String text) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                if(!text.equals("")) {
                    Toast.makeText(getApplicationContext(), text,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //-----------------------------------------------------------put all markers on map and associate crimes and outcomes with markers
    public void updateMap(final ArrayList<ArrayList<Crimes>> inputList, Context context) {
        System.out.println("filter crime : " + filterByCrime + " / filter month : " + filterByMonth);
        ArrayList<ArrayList<Crimes>> temp;
        if (!filterByCrime) {
            crimeList = inputList;
            temp = inputList;
        } else if(filterByCrime && filterByMonth){
            temp = new FilterCrimeList().filter(inputList, filterList.getFilterList());
            filteredCrimes = temp;
            crimeList = inputList;
            new CrimeCountList(context).sortCrimesCount(temp,true, filterByCrime, context);
        }
        else {
            temp = inputList;
            new CrimeCountList(context).sortCrimesCount(temp,true, filterByCrime, context);
        }
        final ArrayList<ArrayList<Crimes>> list = temp;
        runOnUiThread(new Runnable() {
            public void run() {


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
                    try {
                        int mapColour = list.get(i).size();
                        String streetName = new CapitalizeString().getString(list.get(i).get(0).getStreetName().toString());

                        LayoutInflater inflater = (LayoutInflater) MainActivity.this
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        if (mapColour < 100) {
                            View v = inflater.inflate(R.layout.custom_map_marker, null);
                            weightedLatLngs.add(new WeightedLatLng(new LatLng(list.get(i).get(0).getLatitude(), list.get(i).get(0).getLongitude()), list.get(i).size()));
                            markers.add(mMap.addMarker(new MarkerOptions()
                                    .title(streetName)
                                    .position(new LatLng(list.get(i).get(0).getLatitude(), list.get(i).get(0).getLongitude()))
                                    .icon(BitmapDescriptorFactory.fromBitmap(new BitmapGenerator().getMarkerBitmapFromView(mapColour, v, MainActivity.this)))));
                            mMap.setOnMarkerClickListener(MainActivity.this);
                        } else {
                            View bigV = inflater.inflate(R.layout.custom_map_marker_big, null);
                            weightedLatLngs.add(new WeightedLatLng(new LatLng(list.get(i).get(0).getLatitude(), list.get(i).get(0).getLongitude()), 100));
                            markers.add(mMap.addMarker(new MarkerOptions()
                                    .title(streetName)
                                    .position(new LatLng(list.get(i).get(0).getLatitude(), list.get(i).get(0).getLongitude()))
                                    .icon(BitmapDescriptorFactory.fromBitmap(new BitmapGenerator().getMarkerBitmapFromView(mapColour, bigV, MainActivity.this)))));
                            mMap.setOnMarkerClickListener(MainActivity.this);
                        }
                        Log.i("MAP COLOR ", "" + mapColour + " / MAP LOCATION " + list.get(i).get(0).getStreetName() + " / " + list.get(i).get(0).getLatitude() + list.get(i).get(0).getLongitude());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

//        if (isMyLocation) {
//            mMap.addMarker(new MarkerOptions().position(latLng.getLatLng())
//                    .title("Your Location"));
//        } else {
//            mMap.addMarker(new MarkerOptions().position(latLng.getLatLng())
//                    .title("Search Location"));
//        }


                if(list.isEmpty() && filterByCrime){
                    Toast.makeText(getApplicationContext(), "No crimes found...",
                            Toast.LENGTH_SHORT).show();
                } else if ((!filterByCrime || filterByMonth) && !heatMapUsed) {
                    Toast.makeText(getApplicationContext(), "Crime statistics for " + dateUtil.getMonthAsString() + " " + dateUtil.getYear(),
                            Toast.LENGTH_LONG).show();
                    crimesTitle.setText("Area Crime\n" + dateUtil.getMonthAsString() + "/" + dateUtil.getYear());
                }

                // Create the gradient.
                int[] colors = {
                        Color.rgb(102, 225, 0), // green
                        Color.rgb(255, 0, 0)    // red
                };

                float[] startPoints = {
                        0.2f, 1f
                };

                Gradient gradient = new Gradient(colors, startPoints);

                if(null != weightedLatLngs && !weightedLatLngs.isEmpty()) {
                    // Create a heat map tile provider, passing it the latlngs of the police stations.
                    mProvider = new HeatmapTileProvider.Builder()
                            .weightedData(weightedLatLngs)
                            .gradient(gradient)
                            .radius(50)
                            .build();
                }
                if(dateTxt.getVisibility() == INVISIBLE) {
                    dateTxt.setVisibility(VISIBLE);
                }
                if(null != crimeList && !crimeList.isEmpty()){
                    dateTxt.setText(dateUtil.getMonthAsString() + " " + dateUtil.getYear());
                }
                if(filterBtn.getVisibility() == INVISIBLE){
                    filterBtn.setVisibility(VISIBLE);
                }
            }
        });

    }

    public void setFilterViews(ArrayList<Counter> list) {
        ArrayList<String> crimesToShow = new ArrayList<>();
        if(filterByCrime) {
            for (int i = 0; i < filterList.getFilterList().size(); i++) {
                if (i < list.size() && filterList.getFilterList().get(i).getShow()) {
                    crimesToShow.add(filterList.getFilterList().get(i).getNameString());
                }
            }
        }
        int count = 0;
        for (int i = 0; i < filterList.getFilterList().size(); i++) {
            if (i < list.size()) {
                if(null != crimesToShow && !crimesToShow.isEmpty()) {
                    for (int j = 0; j < crimesToShow.size(); j++) {
                        if(list.get(i).getName().equalsIgnoreCase(crimesToShow.get(j))){
                            filterList.getFilterList().get(i).setShow(false);
                            break;
                        }
                        filterList.getFilterList().get(i).setShow(true);
                    }
                }
                count++;
                filterList.getFilterList().get(i).getCardView().setVisibility(VISIBLE);
                filterList.getFilterList().get(i).getName().setText(new CapitalizeString().getString(list.get(i).getName()));
                filterList.getFilterList().get(i).setNameString(list.get(i).getName());
                filterList.getFilterList().get(i).getCardView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        filter((CardView) view);
                    }
                });
            } else {
                filterList.getFilterList().get(i).getName().setText("");
                filterList.getFilterList().get(i).getCardView().setVisibility(GONE);
            }
        }

        filterHeight = count / 4;
        if (count % 4 > 0) {
            filterHeight++;
        }
        for (int i = 0; i < rows.size(); i++) {
            if (i >= (int) filterHeight) {
                rows.get(i).setVisibility(GONE);
            } else {
                rows.get(i).setVisibility(VISIBLE);
            }
        }
        filterHeight = filterHeight * convertDpToPixel(75, MainActivity.this) + convertDpToPixel(55, MainActivity.this);
        ;
    }

    public void filter(CardView view) {
        if (view.getAlpha() == 1) {
            view.setAlpha(0.5f);
            for (FilterItem filterItem : filterList.getFilterList()) {
                if (filterItem.getCardView() == view) {
                    filterItem.setShow(false);
                }
            }
        } else {
            view.setAlpha(1);
            for (FilterItem filterItem : filterList.getFilterList()) {
                if (filterItem.getCardView() == view) {
                    filterItem.setShow(true);
                }
            }
        }


    }

    //-----------------------------------------------------------code for marker selected
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle().equalsIgnoreCase("Search Location") || marker.getTitle().equalsIgnoreCase("Your Location")) {
            hideSoftKeyboard();
            new AnimateFilter().shrinkFilter(filterBtn, filterImage, MainActivity.this, filterHeight, filterList.getFilterList(), dateRow, btnRow,
                    monthSpinner, yearSpinner, filterSearchBtn);
            hidePopUpView();
        } else {
            hideSoftKeyboard();
            new AnimateFilter().shrinkFilter(filterBtn, filterImage, MainActivity.this, filterHeight, filterList.getFilterList(), dateRow, btnRow,
                    monthSpinner, yearSpinner, filterSearchBtn);
            crimeCount.resetStreetCount();
            if (layoutTitle.getY() == getScreenHeight(MainActivity.this) + convertDpToPixel(100, MainActivity.this)) {
                layoutTitle.animate()
                        .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - statusBarHeight)
                        .setDuration(animationTime)
                        .setStartDelay(0)
                        .start();
                layoutBody.animate()
                        .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - statusBarHeight)
                        .setDuration(animationTime)
                        .setStartDelay(0)
                        .start();

            }
            String location = "";
            ArrayList<ArrayList<Crimes>> temp;
            if (filterByCrime) {
                temp = filteredCrimes;
            } else {
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
                                temp.get(i).get(j).getDateOccur(),
                                temp.get(i).get(j).getDateReport(),
                                temp.get(i).get(j).getOutcome(), location,
                                temp.get(i).get(j).getLatitude(),
                                temp.get(i).get(j).getLongitude(),
                                temp.get(i).get(j).getWeapon(),
                                temp.get(i).get(j).getDescription(),
                                temp.get(i).get(j).getTimeOccur(),
                                temp.get(i).get(j).getTimeReport()));

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
            new CrimeCountList(this).sortCrimesCountStreet(counts, false, (filterByCrime || filterByMonth), MainActivity.this);
        }
        return false;
    }

    public void setWeather(String description, Double curentTemp) {
        char tmp = 0x00B0;
        weather.setText(new CapitalizeString().getString(description) + " " + curentTemp.intValue() + tmp);
    }

    @Override
    public void onBackPressed() {
        if (filterBtn.getHeight() > convertDpToPixel(36, MainActivity.this)) {
            new AnimateFilter().shrinkHeight(filterBtn, MainActivity.this, filterHeight);
            new AnimateFilter().shrinkWidth(filterBtn, MainActivity.this);
            new AnimateFilter().hideAll(filterList.getFilterList(), dateRow, btnRow);
            new AnimateFilter().showBackground();
            new AnimateFilter().disableButtons(monthSpinner, yearSpinner, filterSearchBtn);
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
        System.out.println("Focus : " + search.isFocused());
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
                bodyMeasuredHeight = getMeasuredHeight(informationLayout, titleHeight, adViewHeight) < (getScreenHeight(this) - adViewHeight) ? (getScreenHeight(this) - adViewHeight) : getMeasuredHeight(informationLayout, titleHeight, adViewHeight);
                float movementAmmount = startingPosition - previousPosition;
                System.out.println("click count = " + isClickCount);

                //on click
                if (isClickCount < 20 && movementAmmount < 30 && movementAmmount > -30) {
                    if (topOfTitle <= adViewHeight + 30 && topOfTitle >= adViewHeight - 30) {
                        showPopUpViewTitle();
                    } else if (topOfTitle <= getScreenHeight(MainActivity.this) - titleHeight - statusBarHeight + 30 &&
                            topOfTitle >= getScreenHeight(MainActivity.this) - titleHeight - statusBarHeight - 30) {
                        layoutTitle.animate()
                                .y(adViewHeight)
                                .setDuration(animationTime)
                                .start();
                        layoutBody.animate()
                                .y(adViewHeight)
                                .setDuration(animationTime)
                                .start();
                        new AnimateFilter().showAdView(mAdView);
                        adRequest = new AdRequest.Builder().build();
                        mAdView.loadAd(adRequest);
                        searchLayout.setVisibility(GONE);
                        dateTxt.setVisibility(GONE);

                    }
                }
                // on move
                else if (movementAmmount > 200
                        && topOfTitle > getScreenHeight(MainActivity.this) / 4
                        && topOfTitle != 0) {
                    System.out.println("1 = " + (movementAmmount));
                    layoutTitle.animate()
                            .y(adViewHeight)
                            .setDuration(animationTime)
                            .start();
                    layoutBody.animate()
                            .y(adViewHeight)
                            .setDuration(animationTime)
                            .start();
                    new AnimateFilter().showAdView(mAdView);
                    adRequest = new AdRequest.Builder().build();
                    mAdView.loadAd(adRequest);
                    searchLayout.setVisibility(GONE);
                    dateTxt.setVisibility(GONE);
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
                } else if (movementAmmount > -200 && movementAmmount < 0
                        && topOfTitle < getScreenHeight(MainActivity.this) - getScreenHeight(MainActivity.this) / 4
                        && topOfTitle != getScreenHeight(MainActivity.this) - titleHeight
                        && topOfBody >= 0) {
                    System.out.println("4 = " + (movementAmmount) + " / " + topOfTitle);
                    layoutTitle.animate()
                            .y(adViewHeight)
                            .setDuration(animationTime)
                            .start();
                    layoutBody.animate()
                            .y(adViewHeight)
                            .setDuration(animationTime)
                            .start();
                    new AnimateFilter().showAdView(mAdView);
                    adRequest = new AdRequest.Builder().build();
                    mAdView.loadAd(adRequest);
                    searchLayout.setVisibility(GONE);
                    dateTxt.setVisibility(GONE);
                } else if (movementAmmount < -200
                        && topOfTitle < getScreenHeight(MainActivity.this) - getScreenHeight(MainActivity.this) / 4
                        && topOfTitle != getScreenHeight(MainActivity.this) - titleHeight
                        && topOfBody >= 0) {
                    System.out.println("5 = " + (movementAmmount) + " / " + topOfTitle);
                    layoutTitle.animate()
                            .y(adViewHeight)
                            .setDuration(animationTime)
                            .start();
                    layoutBody.animate()
                            .y(adViewHeight)
                            .setDuration(animationTime)
                            .start();
                    new AnimateFilter().showAdView(mAdView);
                    adRequest = new AdRequest.Builder().build();
                    mAdView.loadAd(adRequest);
                    searchLayout.setVisibility(GONE);
                    dateTxt.setVisibility(GONE);
                }
                System.out.println("Focus : " + search.isFocused());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                topOfTitle = layoutTitle.getY();
                topOfBody = layoutBody.getY();
                titleHeight = layoutTitle.getHeight();
                bodyMeasuredHeight = getMeasuredHeight(informationLayout, titleHeight, adViewHeight) < (getScreenHeight(this) - adViewHeight) ? (getScreenHeight(this) - adViewHeight) : getMeasuredHeight(informationLayout, titleHeight, adViewHeight);
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
                    else if (topOfTitle - distanceMoved < getScreenHeight(MainActivity.this) - titleHeight - statusBarHeight) {
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
                    else if (topOfTitle - distanceMoved >= getScreenHeight(MainActivity.this) - titleHeight - statusBarHeight) {
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
        if(onLoad > 1) {
            Toast.makeText(getApplicationContext(), "Connected",
                    Toast.LENGTH_SHORT).show();
        }else {
            onLoad++;
        }
    }

    public void locateMeNow(){
        latLng.setLatlngChaned(true);
        isMyLocation = true;
        hideSoftKeyboard();
        hidePopUpView();
        gpsTracker.getLocation();
        latLng.setLatLng(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()));
        filterByCrime = false;
        filterByMonth = false;
        showPosition(false);
        new AnimateFilter().shrinkFilter(filterBtn, filterImage, MainActivity.this, filterHeight, filterList.getFilterList(), dateRow, btnRow,
                monthSpinner, yearSpinner, filterSearchBtn);
    }

    @Override
    public void networkUnavailable() {
        Toast.makeText(getApplicationContext(), "Disconnected",
                Toast.LENGTH_SHORT).show();
    }

    //--------on activity paused unregister receiver and remove list if this isnt
    // -------done it can still be running in the background when app closes
    public void onPause() {
        try {
            unregisterReceiver(networkStateReceiver);
            networkStateReceiver.removeListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    //--------on activity stop unregister receiver and remove list if this isnt
    // -------done it can still be running in the background when app closes used to catch
    // -------any errors from on pause
    @Override
    protected void onStop() {
        super.onStop();
    }

    //----on resuming the app add a listener to the previously made network state receiver and
    // ---then register this to a broadcast receiver
    public void onResume() {
        onLoad = 0;
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
    }


    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
        search.clearFocus();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        gpsTracker = new GPSTrackerUtil(this);
                        latLng.setLatLng(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()));
                        showPosition(false);
                    }
                }
            }
        }
    }
}