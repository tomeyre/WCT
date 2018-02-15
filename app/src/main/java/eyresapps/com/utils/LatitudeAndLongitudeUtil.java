package eyresapps.com.utils;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Tom on 03/11/2017.
 */
public class LatitudeAndLongitudeUtil {

    LatLng latLng = new LatLng(0,0);

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
    }
}
