package eyresapps.com.api_calls;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import eyresapps.com.utils.HttpConnectUtil;
import eyresapps.com.wct.MainActivity;

public class GetCurrentWeather extends AsyncTask<String, String, String> {

    String myWeatherUrl;
    String json;
    Context context;
    String description;
    String currentTemp;
    Double currentTempParser = 0.0d;

    public GetCurrentWeather(Context context, LatLng latLng){
        this.context = context;
        myWeatherUrl = "http://api.openweathermap.org/data/2.5/weather?lat=" + latLng.latitude + "&lon=" + latLng.longitude + "&APPID=d3a20c8d17b614c06b45e57b3c864846";
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            HttpConnectUtil jParser = new HttpConnectUtil();

            json = jParser.getJSONFromUrl(myWeatherUrl);

            if(!json.contains("Not found city")) {


                JSONObject obj = new JSONObject(json);

                if (obj != null) {
                    description = obj.getJSONArray("weather").getJSONObject(0).getString("description");
                    currentTemp = obj.getJSONObject("main").getString("temp");
                    //weatherId = obj.getJSONArray("weather").getJSONObject(0).getInt("id");

                }
            }


        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    protected void onPostExecute(String strFromDoInBg) {
        try{currentTempParser = Double.parseDouble(currentTemp) - 273.0;}catch (Exception e){e.printStackTrace();currentTempParser = 0.0;}
        ((MainActivity)context).setWeather(description, currentTempParser);
    }
}