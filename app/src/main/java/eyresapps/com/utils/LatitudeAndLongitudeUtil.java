package eyresapps.com.utils;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Tom on 03/11/2017.
 */
public class LatitudeAndLongitudeUtil {

    LatLng latLng = new LatLng(0,0);
    boolean latlngChaned = false;

    private static LatitudeAndLongitudeUtil ourInstance = new LatitudeAndLongitudeUtil();

    public static LatitudeAndLongitudeUtil getInstance() {
        return ourInstance;
    }

    private LatitudeAndLongitudeUtil() {
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
        Log.i("Set LatLng : ",latLng.latitude + "/" + latLng.longitude);
    }

    public boolean isLatlngChaned() {
        return latlngChaned;
    }

    public void setLatlngChaned(boolean latlngChaned) {
        this.latlngChaned = latlngChaned;
    }
}
