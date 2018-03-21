package eyresapps.com.data;

import com.google.android.gms.maps.model.LatLng;

public class SearchLocation {

    private String address;
    private LatLng fullLoc;

    public SearchLocation(String address, LatLng fullLoc){
        this.address = address;
        this.fullLoc = fullLoc;
    }

    public String getAddress(){
        return address;
    }
    public LatLng getFullLoc(){
        return fullLoc;
    }
}