package com.loguapp.logu_java;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by BA042808 on 4/20/2016.
 */
public class GraphListViewActivity extends Activity {

    ListView lview;
    GraphListViewAdapter lviewAdapter;

    private final Prefs preferences = new Prefs();
    private static final int LIFT_GRAPH = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphlist);
        init();
    }

    protected void init() {
        new LiftNames().execute();
    }

    protected void setLviewListener() {
        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getBaseContext(), LiftChartActivity.class);
                i.putExtra("Lift", lviewAdapter.lift[position]);
                startActivityForResult(i, LIFT_GRAPH);
            }
        });
    }

    private class LiftNames extends AsyncTask<Void, Void, JSONObject[]> {
        @Override
        protected JSONObject[] doInBackground(Void... params) {

            JSONObject[] dash_data = {};

            try {
                String url = "https://loguapp.com/swift8.php";
                URL obj = new URL(url);
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("accept", "application/json");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                String username = preferences.getUsername(GraphListViewActivity.this);
                System.out.println(username);

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

                for(int i=0; i < json_data.length(); i++)
                {
                    object = json_data.getJSONObject(i);
                    user_data[i] = object;
                }
                dash_data = user_data;
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < dash_data.length; i++) {
                System.out.println(dash_data[i]);
            }
            return dash_data;
        }

        @Override
        protected void onPostExecute(JSONObject[] user_data) {

            String[] lifts = new String[user_data.length];

            for (int i = 0; i < user_data.length; ++i) {
                try {
                    lifts[i] = user_data[i].get("lift").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            lview = (ListView) findViewById(R.id.graphList);
            lviewAdapter = new GraphListViewAdapter(GraphListViewActivity.this, lifts);

            System.out.println("adapter => " + lviewAdapter.getCount());

            lview.setAdapter(lviewAdapter);
            setLviewListener();
        }
    }
}
