package eyresapps.com.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;

import eyresapps.com.data.SearchLocation;
import eyresapps.com.wct.MainActivity;

//------------------------------------------finds a lat long from the address entered in search using a google api
public class FindSearchLocation extends AsyncTask<String, String, String> {
    private ArrayList<SearchLocation> searchLocations = new ArrayList<>();
    private CurrentAddressUtil currentAddress = CurrentAddressUtil.getInstance();
    private LatitudeAndLongitudeUtil latLng = LatitudeAndLongitudeUtil.getInstance();
    private Context context;

    public FindSearchLocation(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... params) {

        try {
            if (!searchLocations.isEmpty()) {
                searchLocations.clear();
            }
            // create new instance of the httpConnect class
            HttpConnectUtil jParser = new HttpConnectUtil();

            // get json string from service url
            String json = jParser.getJSONFromUrl("https://maps.googleapis.com/maps/api/geocode/json?address=" + params[0].toString().trim().replaceAll(" ", "+") + "+UK&key=AIzaSyDEVB46xX2jKw43znXsEwMxeamw-iOPyKo");

            // Get JSON object contains an object and an array
            JSONObject jsonObject = new JSONObject(json);

            if (jsonObject.getString("status").equals("OK")) {
                for (int i = 0; i < jsonObject.getJSONArray("results").length(); i++) {
                    //save results list here maybe in an array list need to have a drop down menu so if you get more than
                    //one address you can select which one to check so nee to save whole address so you can select the
                    //right one
                    searchLocations.add(new SearchLocation(jsonObject.getJSONArray("results").getJSONObject(i).getString("formatted_address"),
                            new LatLng(jsonObject.getJSONArray("results").getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                                    jsonObject.getJSONArray("results").getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng"))));
                }
            } else {
                Toast.makeText(context, "Can't find  " + params[0],
                        Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String strFromDoInBg) {
        currentAddress.setAddress(searchLocations.get(0).getAddress());
        latLng.setLatLng(searchLocations.get(0).getFullLoc());
        ((MainActivity) context).showPosition();
    }
}