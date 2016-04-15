package com.loguapp.logu_java;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by BA042808 on 4/15/2016.
 */
public class ClusteringActivity extends CommunityActivity {

    ClusterManager<MapItemCluster> mClusterManager;
    private JSONObject[] gpsCoords;

    protected void setUpClusterer() {
        // Declare a variable for the cluster manager.

        // Position the map.
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 2));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MapItemCluster>(this, getMap());

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        getMap().setOnCameraChangeListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {

        try {
            gpsCoords = new GymMapMarkers().execute("https://loguapp.com/request_coords.php").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        int lat;
        int lng;

        for (int i = 0; i < gpsCoords.length; i++) {
            try {
                lat = gpsCoords[i].getInt("latitude");
                lng = gpsCoords[i].getInt("longitude");
                MapItemCluster item = new MapItemCluster(lat, lng);
                mClusterManager.addItem(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class GymMapMarkers extends AsyncTask<String, Void, JSONObject[]> {
        @Override
        protected JSONObject[] doInBackground(String... url) {

            JSONObject[] gps_coords = {};

            try {
                URL obj = new URL(url[0]);
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("accept", "application/json");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                String urlParameters = "none=none";

                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONArray json_data = new JSONArray(new String(response.toString()));
                JSONObject object = new JSONObject();

                JSONObject[] user_data = new JSONObject[json_data.length()];

                for(int i=0; i < json_data.length(); i++)
                {
                    object = json_data.getJSONObject(i);
                    user_data[i] = object;
                }
                gps_coords = user_data;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return gps_coords;
        }
    }
}
