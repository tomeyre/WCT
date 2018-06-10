package eyresapps.com.api_calls;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import eyresapps.com.utils.HttpConnectUtil;

/**
 * Created by thomaseyre on 18/02/2018.
 */

public class GenerateNeighbourhoodLocation extends AsyncTask<String, String, String> {

    private Context context;
    private String url;
    private String force;
    private String neighbourhood;

    public GenerateNeighbourhoodLocation(Context context, LatLng latLng) {
        this.context = context;
        this.url = "https://data.police.uk/api/locate-neighbourhood?q=" + latLng.latitude + "," + latLng.longitude;
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            // create new instance of the httpConnect class
            HttpConnectUtil jParser = new HttpConnectUtil();

            // get json string from service url
            String json = jParser.getJSONFromUrl(url);

            Log.i("Force JSON : ",json);

            // Get JSON object contains an object and an array
            JSONObject jsonObject = new JSONObject(json);

            if(jsonObject.has("force") && jsonObject.has("neighbourhood")) {
                force = jsonObject.getString("force").toString();
                neighbourhood = jsonObject.getString("neighbourhood");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String strFromDoInBg) {
       // ((MainActivity)context).setNeighbourhoodDetails(force);
        new GenerateNeighbourhoodDescriptionUrl(context, "https://data.police.uk/api/" + force + "/" + neighbourhood).execute();
    }
}
