package com.loguapp.logu_java;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by BA042808 on 3/31/2016.
 */
public abstract class CommunityActivity extends FragmentActivity implements OnMapReadyCallback{

    final Prefs preferences = new Prefs();
    ListView lview;
    StaticListViewAdapter lviewAdapter;

    private GoogleMap mMap;
    private JSONObject[] commStats;
    private String[] commCache = new String[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);

        Bundle extras = getIntent().getExtras();
        if (extras.getBoolean("shouldUpdate")) {
            makeNetworkCalls();
        } else {
            useCachedValues(extras);
            System.out.println("Trying to use cached values");
        }

        //Set up map for pins/cluster
        MapFragment mapFragment = new MapFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mapView, mapFragment);
        transaction.commit();
        mapFragment.getMapAsync(this);

        //Set up static list (menu options)
        String[] staticList = {"Weekly Poundage Graph", "Weekly Poundage Data", "Targeted Muscle Graph"};
        lview = (ListView) findViewById(R.id.staticCommListView);
        lviewAdapter = new StaticListViewAdapter(CommunityActivity.this, staticList);
        lview.setAdapter(lviewAdapter);
        setListViewHeightBasedOnChildren(lview);
    }

    @Override
    public void onBackPressed() {
        //Sending back cached data
        Intent intent = new Intent(CommunityActivity.this, Dashboard.class);
        intent.putExtra("commCache", commCache);

        setResult(RESULT_OK, intent);
        finish();
    }

    protected static void setListViewHeightBasedOnChildren(ListView listView) {
        //Resizing listview rows to play nice with the rest of the layout
        StaticListViewAdapter listAdapter = (StaticListViewAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        //Map is ready for clusters
        mMap = map;
        setUpClusterer();
    }

    protected GoogleMap getMap() {
        return mMap;
    }

    protected abstract void setUpClusterer();

    protected void makeNetworkCalls() {
        //grab data from server
        try {
            commStats = new CommData().execute("https://loguapp.com/community_stats.php").get();
            setCommStats(commStats);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    protected void setCommStats(JSONObject[] data) {
        //Use data from http request to set values on screen
        TextView poundage = (TextView) findViewById(R.id.commPoundage);
        TextView totalLifts = (TextView) findViewById(R.id.commLifts);
        TextView favLift= (TextView) findViewById(R.id.commFav);
        TextView totalSets = (TextView) findViewById(R.id.commSets);
        TextView totalReps = (TextView) findViewById(R.id.commReps);
        TextView avgReps = (TextView) findViewById(R.id.commAvg);

        try {
            poundage.setText(data[0].get("poundage").toString());
            totalLifts.setText(data[0].get("total_lifts").toString());
            favLift.setText(data[0].get("favorite").toString());
            totalSets.setText(data[0].get("total_sets").toString());
            totalReps.setText(data[0].get("total_reps").toString());
            avgReps.setText(data[0].get("average_reps").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cacheValues(data);
    }

    protected void useCachedValues(Bundle extras) {
        //When a user hasn't logged/updated lift/updated settings, use cached values.
        TextView poundage = (TextView) findViewById(R.id.commPoundage);
        TextView totalLifts = (TextView) findViewById(R.id.commLifts);
        TextView favLift= (TextView) findViewById(R.id.commFav);
        TextView totalSets = (TextView) findViewById(R.id.commSets);
        TextView totalReps = (TextView) findViewById(R.id.commReps);
        TextView avgReps = (TextView) findViewById(R.id.commAvg);

        if (extras.getStringArray("commCache") != null) {
            String[] commData = extras.getStringArray("commCache");

            poundage.setText(commData[0]);
            totalLifts.setText(commData[1]);
            favLift.setText(commData[2]);
            totalSets.setText(commData[3]);
            totalReps.setText(commData[4]);
            avgReps.setText(commData[5]);

            commCache = commData;
        }
    }

    protected void cacheValues(JSONObject[] data) {
        //caching values to send back in intent
        try {
            commCache[0] = data[0].get("poundage").toString();
            commCache[1] = data[0].get("total_lifts").toString();
            commCache[2] = data[0].get("favorite").toString();
            commCache[3] = data[0].get("total_sets").toString();
            commCache[4] = data[0].get("total_reps").toString();
            commCache[5] = data[0].get("average_reps").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //async class to deal with http requests.
    private class CommData extends AsyncTask<String, Void, JSONObject[]> {
        @Override
        protected JSONObject[] doInBackground(String... url) {

            JSONObject[] dash_data = {};

            try {
                URL obj = new URL(url[0]);
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("accept", "application/json");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                String username = preferences.getUsername(CommunityActivity.this);

                String urlParameters = "username=" + username;

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

                for (int i = 0; i < json_data.length(); i++) {
                    object = json_data.getJSONObject(i);
                    user_data[i] = object;
                }
                dash_data = user_data;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return dash_data;
        }
    }
}
