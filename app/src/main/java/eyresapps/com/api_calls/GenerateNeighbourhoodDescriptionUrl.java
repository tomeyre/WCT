package eyresapps.com.api_calls;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import eyresapps.com.utils.HttpConnectUtil;

public class GenerateNeighbourhoodDescriptionUrl extends AsyncTask<String, String, String> {

    Context context;
    String url;
    String description;
    String area;
    private String twitter;
    private String facebook;
    private String email;
    private String youtube;
    private String website;

    public GenerateNeighbourhoodDescriptionUrl(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            // create new instance of the httpConnect class
            HttpConnectUtil jParser = new HttpConnectUtil();

            // get json string from service url
            String json = jParser.getJSONFromUrl(url);

            Log.i("Neighbourhood JSON : ",json);

            // Get JSON object contains an object and an array
            JSONObject jsonObject = new JSONObject(json);

            if(jsonObject != null) {
                if(jsonObject.has("description")) {
                    description = jsonObject.getString("description").toString();
                }
                area = jsonObject.getString("name").toString();
                if(jsonObject.getJSONObject("contact_details").has("twitter")){
                    twitter = jsonObject.getJSONObject("contact_details").getString("twitter");
                }
                if(jsonObject.getJSONObject("contact_details").has("facebook")){
                    facebook = jsonObject.getJSONObject("contact_details").getString("facebook");
                }
                if(jsonObject.getJSONObject("contact_details").has("email")){
                    email = jsonObject.getJSONObject("contact_details").getString("email");
                }
                if(jsonObject.getJSONObject("contact_details").has("youtube")){
                    youtube = jsonObject.getJSONObject("contact_details").getString("youtube");
                }
                if(jsonObject.getJSONObject("contact_details").has("website")){
                    website = jsonObject.getJSONObject("contact_details").getString("website");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String strFromDoInBg) {
       // ((MainActivity)context).setDescription(description);
        //((MainActivity)context).setAboutArea(area, email, facebook, youtube, twitter, website);
    }
}
