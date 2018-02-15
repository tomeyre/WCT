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
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import eyresapps.com.crimeLocations.GenerateCrimeUrl;
import eyresapps.com.data.Crimes;
import eyresapps.com.utils.CurrentAddressUtil;
import eyresapps.com.utils.DateUtil;
import eyresapps.com.utils.GPSTrackerUtil;
import eyresapps.com.utils.LatitudeAndLongitudeUtil;

import static eyresapps.com.utils.ScreenUtils.getScreenHeight;
import static eyresapps.com.utils.ScreenUtils.getStatusBarHeight;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private ProgressDialog dialog;

    private RelativeLayout layoutBody;
    private CardView layoutTitle;

    private GoogleMap mMap;
    private float oldY = 0;
    private float firstY = 0;
    private float distanceToMove = 0;
    private int heightDiff = 0;

    private TextView streetName;

    private LockableScrollView lockableScrollView;

    private LatitudeAndLongitudeUtil latLng = LatitudeAndLongitudeUtil.getInstance();
    private GPSTrackerUtil gpsTracker;
    private CurrentAddressUtil currentAddress = CurrentAddressUtil.getInstance();

    private DateUtil dateUtil = DateUtil.getInstance();

    private GenerateCrimeUrl generateCrimeUrl;

    private LocationManager lm;

    private CameraUpdate cameraUpdate;

    private ArrayList<ArrayList<Crimes>> crimeList = new ArrayList<>();

    private Marker currentSelection;

    private RVAdapterCrimes rvAdapterCrimes;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private  CardView crimesCardView;
    private TextView crimesTitle;
    private RelativeLayout crimeRl;
    private TextView time;
    private Handler someHandler;
    private Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(this);

        layoutBody = findViewById(R.id.informationLayout);
        layoutTitle = findViewById(R.id.informationCardView);
        streetName = findViewById(R.id.streetName);
        recyclerView = findViewById(R.id.crimesRv);
        crimesCardView = findViewById(R.id.crimesCardView);
        crimesTitle = findViewById(R.id.crimesTitle);
        crimeRl = findViewById(R.id.crimeRl);
        time = findViewById(R.id.time);

        layoutTitle.setY((getScreenHeight(this) / 10) * 6);
        layoutBody.setY(((getScreenHeight(this) / 10) * 6) - layoutTitle.getHeight());

        lockableScrollView = findViewById(R.id.scrollView);
        lockableScrollView.setScrollingEnabled(false);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        heightDiff = getStatusBarHeight(MainActivity.this);

        //noinspection AndroidLintClickableViewAccessibility

        layoutBody.setOnTouchListener(handleTouch);
        layoutTitle.setOnTouchListener(handleTouchTitle);

        gpsTracker = new GPSTrackerUtil(this);
        latLng.setLatLng(new LatLng(/*53.230688,-0.5405789999999999));*/gpsTracker.getLatitude(),gpsTracker.getLongitude()));
        lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

    }

    private View.OnTouchListener handleTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    oldY = event.getRawY();
                    firstY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    if (oldY == firstY && layoutTitle.getY() == 0 && layoutBody.getY() == 0) {
                        layoutTitle.animate()
                                .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff)
                                .setDuration(250)
                                .start();
                        layoutBody.animate()
                                .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff)
                                .setDuration(250)
                                .start();
                    } else if (oldY == firstY && layoutTitle.getY() == getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff
                            && layoutBody.getY() == getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff) {
                        layoutTitle.animate()
                                .y(0)
                                .setDuration(250)
                                .start();
                        layoutBody.animate()
                                .y(0)
                                .setDuration(250)
                                .start();
                    } else if (firstY - oldY > 10
                            && layoutTitle.getY() > getScreenHeight(MainActivity.this) / 4
                            && layoutTitle.getY() != 0) {
                        System.out.println("4 || " + layoutTitle.getY());
                        layoutTitle.animate()
                                .y(0)
                                .setDuration(250)
                                .start();
                        layoutBody.animate()
                                .y(0)
                                .setDuration(250)
                                .start();
                    } else if (firstY - oldY < -10
                            && layoutTitle.getY() < getScreenHeight(MainActivity.this) - getScreenHeight(MainActivity.this) / 3
                            && layoutTitle.getY() != getScreenHeight(MainActivity.this) - layoutTitle.getHeight()
                            && layoutBody.getY() >= 0) {
                        layoutTitle.animate()
                                .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff)
                                .setDuration(250)
                                .start();
                        layoutBody.animate()
                                .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff)
                                .setDuration(250)
                                .start();
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    distanceToMove = oldY - event.getRawY();
                    if (distanceToMove > 0) {
                        if (layoutBody.getY() + layoutBody.getHeight() - distanceToMove <= getScreenHeight(MainActivity.this)) {
                            System.out.println("8");
                            layoutTitle.animate()
                                    .y(0)
                                    .setDuration(0)
                                    .start();
                            layoutBody.animate()
                                    .y(getScreenHeight(MainActivity.this) - layoutBody.getHeight())
                                    .setDuration(0)
                                    .start();
                        } else if (layoutBody.getY() + layoutTitle.getHeight() - heightDiff < layoutTitle.getHeight() - heightDiff) {
                            layoutTitle.animate()
                                    .y(0)
                                    .setDuration(0)
                                    .start();
                            layoutBody.animate()
                                    .y(layoutBody.getY() - distanceToMove)
                                    .setDuration(0)
                                    .start();

                        } else {
                            layoutTitle.animate()
                                    .y(layoutTitle.getY() - distanceToMove)
                                    .setDuration(0)
                                    .start();
                            layoutBody.animate()
                                    .y(layoutBody.getY() - distanceToMove)
                                    .setDuration(0)
                                    .start();
                        }
                    } else {
                        if (layoutBody.getY() + layoutTitle.getHeight() - heightDiff < layoutTitle.getHeight() - heightDiff) {
                            layoutTitle.animate()
                                    .y(0)
                                    .setDuration(0)
                                    .start();
                        } else {
                            if (layoutTitle.getY() != layoutBody.getY()) {
                                layoutTitle.animate()
                                        .y(layoutBody.getY())
                                        .setDuration(0)
                                        .start();
                            }
                            if (layoutTitle.getY() >= getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff) {
                                layoutTitle.animate()
                                        .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff)
                                        .setDuration(0)
                                        .start();
                            } else {
                                layoutTitle.animate()
                                        .y(layoutTitle.getY() - distanceToMove)
                                        .setDuration(0)
                                        .start();
                            }
                        }
                        if (layoutBody.getY() >= getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff) {
                            layoutBody.animate()
                                    .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff)
                                    .setDuration(0)
                                    .start();
                        } else {
                            layoutBody.animate()
                                    .y(layoutBody.getY() - distanceToMove)
                                    .setDuration(0)
                                    .start();
                        }

                    }
                    oldY = event.getRawY();

                    break;
            }
            return false;

        }
    };

    //noinspection AndroidLintClickableViewAccessibility
    private View.OnTouchListener handleTouchTitle = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    oldY = event.getRawY();
                    firstY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    if (oldY == firstY && layoutTitle.getY() == 0) {
                        layoutTitle.animate()
                                .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff)
                                .setDuration(250)
                                .start();
                        layoutBody.animate()
                                .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff)
                                .setDuration(250)
                                .start();
                    } else if (oldY == firstY && layoutTitle.getY() == getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff
                            && layoutBody.getY() == getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff) {
                        layoutTitle.animate()
                                .y(0)
                                .setDuration(250)
                                .start();
                        layoutBody.animate()
                                .y(0)
                                .setDuration(250)
                                .start();
                    } else if (firstY - oldY > 10
                            && layoutTitle.getY() > getScreenHeight(MainActivity.this) / 4
                            && layoutTitle.getY() != 0) {
                        System.out.println("4 || " + layoutTitle.getY());
                        layoutTitle.animate()
                                .y(0)
                                .setDuration(250)
                                .start();
                        layoutBody.animate()
                                .y(0)
                                .setDuration(250)
                                .start();
                    } else if (firstY - oldY < -10
                            && layoutTitle.getY() < getScreenHeight(MainActivity.this) - getScreenHeight(MainActivity.this) / 3
                            && layoutTitle.getY() != getScreenHeight(MainActivity.this) - layoutTitle.getHeight()
                            && layoutBody.getY() >= 0) {
                        layoutTitle.animate()
                                .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff)
                                .setDuration(250)
                                .start();
                        layoutBody.animate()
                                .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff)
                                .setDuration(250)
                                .start();
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    distanceToMove = oldY - event.getRawY();
                    if (distanceToMove > 0) {
                        if (layoutBody.getY() + layoutBody.getHeight() - distanceToMove <= getScreenHeight(MainActivity.this)) {
                            System.out.println("8");
                            layoutTitle.animate()
                                    .y(0)
                                    .setDuration(0)
                                    .start();
                            layoutBody.animate()
                                    .y(getScreenHeight(MainActivity.this) - layoutBody.getHeight())
                                    .setDuration(0)
                                    .start();
                        } else if (layoutBody.getY() + layoutTitle.getHeight() - heightDiff < layoutTitle.getHeight() - heightDiff) {
                            layoutTitle.animate()
                                    .y(0)
                                    .setDuration(0)
                                    .start();
                            layoutBody.animate()
                                    .y(layoutBody.getY() - distanceToMove)
                                    .setDuration(0)
                                    .start();

                        } else {
                            layoutTitle.animate()
                                    .y(layoutTitle.getY() - distanceToMove)
                                    .setDuration(0)
                                    .start();
                            layoutBody.animate()
                                    .y(layoutBody.getY() - distanceToMove)
                                    .setDuration(0)
                                    .start();
                        }
                    } else {
                        if (layoutBody.getY() + layoutTitle.getHeight() - heightDiff < layoutTitle.getHeight() - heightDiff) {
                            layoutTitle.animate()
                                    .y(0)
                                    .setDuration(0)
                                    .start();
                        } else {
                            if (layoutTitle.getY() != layoutBody.getY()) {
                                layoutTitle.animate()
                                        .y(layoutBody.getY())
                                        .setDuration(0)
                                        .start();
                            }
                            if (layoutTitle.getY() >= getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff) {
                                layoutTitle.animate()
                                        .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff)
                                        .setDuration(0)
                                        .start();
                            } else {
                                layoutTitle.animate()
                                        .y(layoutTitle.getY() - distanceToMove)
                                        .setDuration(0)
                                        .start();
                            }
                        }
                        if (layoutBody.getY() >= getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff) {
                            layoutBody.animate()
                                    .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight() - heightDiff)
                                    .setDuration(0)
                                    .start();
                        } else {
                            layoutBody.animate()
                                    .y(layoutBody.getY() - distanceToMove)
                                    .setDuration(0)
                                    .start();
                        }

                    }
                    oldY = event.getRawY();

                    break;
            }
            return false;

        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng newLatLng) {
                dialog.setMessage("Getting crimes...");
                dialog.show();
                latLng.setLatLng(newLatLng);
                findAddress();
                callNewCrime();
            }
        });
        showPosition();
    }

    public void setCrimesOnMarkerClick(ArrayList<Crimes> crimeList){
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        rvAdapterCrimes = new RVAdapterCrimes(crimeList);
        recyclerView.setAdapter(rvAdapterCrimes);
    }

    //------------------private methods

    private void findAddress(){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.getLatLng().latitude, latLng.getLatLng().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            currentAddress.setAddress(address);
        }catch (Exception e){e.printStackTrace();
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error",
                    Toast.LENGTH_SHORT).show();
        }
        if(currentAddress.getAddress().contains("UK")) {
            locale = Locale.UK;
        }else if(currentAddress.getAddress().contains("USA")){
            locale = Locale.US;
        }

        someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                time.setText(new SimpleDateFormat("HH:mm", locale).format(new Date()));
                someHandler.postDelayed(this, 1000);
            }
        }, 10);
    }

    private void callNewCrime(){
        dateUtil.resetDate();
        generateCrimeUrl = new GenerateCrimeUrl(MainActivity.this, false);
        https://data.police.uk/api/leicestershire/NC04
        dateUtil.setMaxMonth(dateUtil.getMonth());
        dateUtil.setMaxYear(dateUtil.getYear());
    }

    private void showPosition() {
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            dialog.setMessage("Getting crimes...");
            dialog.show();
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng.getLatLng(), 15);
            mMap.addMarker(new MarkerOptions().position(latLng.getLatLng()).title("You are here"));
            mMap.animateCamera(cameraUpdate);
            dialog.setMessage("Getting Crimes");
            dialog.show();
            findAddress();
            callNewCrime();
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

    public void dismissDialog(){
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
        Toast.makeText(getApplicationContext(), "Crime statistics around " + currentAddress.getAddress(),
                Toast.LENGTH_LONG).show();
    }

    //-----------------------------------------------------------code for marker selected
    @Override
    public boolean onMarkerClick(Marker marker) {
        String location = "";
        ArrayList<Crimes> tempCrimes = new ArrayList<>();
        for (int i = 0; i < crimeList.size(); i++) {
            if (crimeList.get(i).get(0).getLongitude() == marker.getPosition().longitude &&
                    crimeList.get(i).get(0).getLatitude() == marker.getPosition().latitude) {
                location = crimeList.get(i).get(0).getStreetName();
                for (int j = 0; j < crimeList.get(i).size(); j++) {
                    tempCrimes.add(new Crimes(crimeList.get(i).get(j).getCrimeType(),crimeList.get(i).get(j).getDate(),crimeList.get(i).get(j).getTime(),crimeList.get(i).get(j).getOutcome(),location,crimeList.get(i).get(j).getLatitude(),crimeList.get(i).get(j).getLongitude(),"",""));
                }
                break;
            }
        }


        streetName.setText(location.trim());
        setCrimesOnMarkerClick(tempCrimes);

        currentSelection = marker;
        return false;
    }
}