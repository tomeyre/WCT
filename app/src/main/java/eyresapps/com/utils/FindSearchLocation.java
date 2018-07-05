package eyresapps.com.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
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
    private boolean nothingStored = true;
    private String searchString;
    private JSONArray storedLocations;

    public FindSearchLocation(Context context, String searchString) {
        this.context = context;
        this.searchString = searchString;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... params) {
        boolean notSaved = true;

//        try{
//            String str =  new ReadWriteStuff().readFromFile(context);
//            storedLocations = new JSONArray(str);
//            if(storedLocations != null) {
//                if (!searchLocations.isEmpty()) {
//                    searchLocations.clear();
//                }
//                for(int i = 0; i < storedLocations.length(); i++) {
//                    if(storedLocations.getJSONObject(i).getString("searchQuery").trim().equalsIgnoreCase(searchString.trim())) {
//                        searchLocations.add(new SearchLocation(storedLocations.getJSONObject(i).getString("location"),
//                                new LatLng(storedLocations.getJSONObject(i).getLong("lat"), storedLocations.getJSONObject(i).getLong("long"))));
//                        notSaved = false;
//                        break;
//                    }
//                }
//                nothingStored = false;
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }

        if(notSaved) {
            try {
                if (!searchLocations.isEmpty()) {
                    searchLocations.clear();
                }
                // create new instance of the httpConnect class
                HttpConnectUtil jParser = new HttpConnectUtil();

                // get json string from service url
                String json = jParser.getJSONFromUrl("https://maps.googleapis.com/maps/api/geocode/json?address=" + searchString.trim().replaceAll(" ", "+") + "+UK&key=AIzaSyDEVB46xX2jKw43znXsEwMxeamw-iOPyKo");

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
                        latLng.setLatLng(searchLocations.get(0).getFullLoc());
//                        String newLocation = creatJsonStringForAddress(nothingStored);
//                        new ReadWriteStuff().writeToFile(newLocation, context);
                    }
                } else {
                    Toast.makeText(context, "Can't find  " + searchString,
                            Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String strFromDoInBg) {
        if(!searchLocations.isEmpty()) {
            currentAddress.setAddress(searchLocations.get(0).getAddress());
            ((MainActivity) context).showPosition(true);
        }else{
            ((MainActivity)context).dismissDialog("Error can't find address...");
        }
    }

    private String creatJsonStringForAddress(boolean nothingStored){
        StringBuilder json = new StringBuilder();
        if(storedLocations != null){
            json.append(storedLocations.toString());
            json.deleteCharAt(0);
            json.deleteCharAt(json.length()-1);
            json.append(",");
        }
        json.append("{ lat:" + latLng.getLatLng().latitude + ", long:" + latLng.getLatLng().longitude + ", location:\"" + searchLocations.get(0).getAddress() + "\", searchQuery:\"" + searchString + "\"}");

        return  json.toString();
    }
}