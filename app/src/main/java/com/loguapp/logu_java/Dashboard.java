package com.loguapp.logu_java;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Dashboard extends AppCompatActivity {


    ListView lview;
    ListViewAdapter lviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();
    }

    public void init() {
        try {
            new LiftData().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class LiftData extends AsyncTask<Void, Void, JSONObject[]> {
        @Override
        protected JSONObject[] doInBackground(Void... params) {

            JSONObject[] dash_data = {};

            try {
                String url = "https://loguapp.com/swift6.php";
                URL obj = new URL(url);
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("accept", "application/json");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                String urlParameters = "username=brettalcox";

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

            return dash_data;
        }

        @Override
        protected void onPostExecute(JSONObject[] user_data) {

            String[] dates = new String[user_data.length];
            String[] lifts = new String[user_data.length];
            String[] set_reps = new String[user_data.length];
            String[] weights = new String[user_data.length];

            for (int i = 0; i < user_data.length; ++i) {
                try {
                    dates[i] = user_data[i].get("date").toString();
                    lifts[i] = user_data[i].get("lift").toString();
                    set_reps[i] = user_data[i].get("sets").toString() + "x" + user_data[i].get("reps").toString();
                    weights[i] = user_data[i].get("weight").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            lview = (ListView) findViewById(R.id.listView);
            lviewAdapter = new ListViewAdapter(Dashboard.this, dates, lifts, set_reps, weights);

            System.out.println("adapter => "+lviewAdapter.getCount());

            lview.setAdapter(lviewAdapter);
        }
    }
}

