package eyresapps.com.wct;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static eyresapps.com.utils.ScreenUtils.getScreenHeight;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    RelativeLayout layoutBody;
    CardView layoutTitle;

    private GoogleMap mMap;
    float oldY = 0;
    float distanceToMove = 0;

    LockableScrollView lockableScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutBody = findViewById(R.id.informationLayout);
        layoutTitle = findViewById(R.id.informationCardView);

        layoutTitle.setY((getScreenHeight(this) / 10) * 6);
        layoutBody.setY(((getScreenHeight(this) / 10) * 6) - layoutTitle.getHeight());

        lockableScrollView = findViewById(R.id.scrollView);
        lockableScrollView.setScrollingEnabled(false);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //noinspection AndroidLintClickableViewAccessibility
        layoutBody.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        oldY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (layoutTitle.getY() < getScreenHeight(MainActivity.this) / 4 && layoutTitle.getY() != 0) {
                            layoutTitle.animate()
                                    .y(0)
                                    .setDuration(250)
                                    .start();
                            layoutBody.animate()
                                    .y(0)
                                    .setDuration(250)
                                    .start();
                        } else if (layoutTitle.getY() > getScreenHeight(MainActivity.this) - getScreenHeight(MainActivity.this) / 3
                                && layoutTitle.getY() != getScreenHeight(MainActivity.this) - layoutTitle.getHeight()) {
                            layoutTitle.animate()
                                    .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight())
                                    .setDuration(250)
                                    .start();
                            layoutBody.animate()
                                    .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight())
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
                                layoutTitle.animate()
                                        .y(0)
                                        .setDuration(0)
                                        .start();
                                layoutBody.animate()
                                        .y(getScreenHeight(MainActivity.this) - layoutBody.getHeight())
                                        .setDuration(0)
                                        .start();
                            } else if (layoutBody.getY() + layoutTitle.getHeight() < layoutTitle.getHeight()) {
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
                            if (layoutBody.getY() + layoutTitle.getHeight() < layoutTitle.getHeight()) {
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
                                layoutTitle.animate()
                                        .y(layoutTitle.getY() - distanceToMove)
                                        .setDuration(0)
                                        .start();
                            }
                            layoutBody.animate()
                                    .y(layoutBody.getY() - distanceToMove)
                                    .setDuration(0)
                                    .start();

                        }
                        oldY = event.getRawY();

                        break;
                }
                return false;

            }
        });
        //noinspection AndroidLintClickableViewAccessibility
        layoutTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        oldY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (layoutTitle.getY() < getScreenHeight(MainActivity.this) / 4) {
                            layoutTitle.animate()
                                    .y(0)
                                    .setDuration(250)
                                    .start();
                            layoutBody.animate()
                                    .y(0)
                                    .setDuration(250)
                                    .start();
                        } else if (layoutTitle.getY() > getScreenHeight(MainActivity.this) - getScreenHeight(MainActivity.this) / 3) {
                            layoutTitle.animate()
                                    .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight())
                                    .setDuration(250)
                                    .start();
                            layoutBody.animate()
                                    .y(getScreenHeight(MainActivity.this) - layoutTitle.getHeight())
                                    .setDuration(250)
                                    .start();
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:

                        System.out.println("in event");
                        distanceToMove = oldY - event.getRawY();

                        if (distanceToMove > 0) {
                            if (layoutBody.getY() + layoutBody.getHeight() - distanceToMove <= getScreenHeight(MainActivity.this)) {
                                layoutTitle.animate()
                                        .y(0)
                                        .setDuration(0)
                                        .start();
                                layoutBody.animate()
                                        .y(getScreenHeight(MainActivity.this) - layoutBody.getHeight())
                                        .setDuration(0)
                                        .start();
                            } else if (layoutBody.getY() + layoutTitle.getHeight() < layoutTitle.getHeight()) {
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
                            if (layoutBody.getY() + layoutTitle.getHeight() < layoutTitle.getHeight()) {
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
                                layoutTitle.animate()
                                        .y(layoutTitle.getY() - distanceToMove)
                                        .setDuration(0)
                                        .start();
                            }
                            layoutBody.animate()
                                    .y(layoutBody.getY() - distanceToMove)
                                    .setDuration(0)
                                    .start();

                        }
                        oldY = event.getRawY();

                        break;
                }
                return false;

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}