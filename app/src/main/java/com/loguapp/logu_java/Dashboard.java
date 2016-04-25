package com.loguapp.logu_java;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    static final int DASH_UPDATE_RESULT = 0;
    static final int STATS_UPDATE_RESULT = 1;
    static final int UPDATE_LIFT_RESULT = 5;
    static final int COMM_UPDATE_RESULT = 6;

    ListView lview;
    ListViewAdapter lviewAdapter;
    private final Prefs preferences = new Prefs();

    private Boolean shouldUpdateStats = true;
    private Boolean shouldUpdateCommStats = true;

    private String[] statsCache;
    private String[] commCache;
    private String[] muscleCat;
    private String[] weightedVal;

    private String[] intentIntensity;
    private String[] intentNotes;
    private String[] intentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();
        System.out.println(preferences.getLat(Dashboard.this));
        System.out.println(preferences.getLon(Dashboard.this));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void init() {
        try {
            new LiftData().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        preferences.userLoggedOut(Dashboard.this);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DASH_UPDATE_RESULT) {
            if (resultCode == RESULT_OK) {
                System.out.println("Passing back intent extras worked");

                try {
                    if (new LiftData().execute().get().length > 0) {
                        showSnackOnDash("Lift Logged Successfully");
                        shouldUpdateStats = true;
                        shouldUpdateCommStats = true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                shouldUpdateStats = true;
                shouldUpdateCommStats = true;
            } else {
                //User didn't log a lift
            }
        }

        if (requestCode == STATS_UPDATE_RESULT) {
            if (resultCode == RESULT_OK) {
                statsCache = data.getStringArrayExtra("statsCache");
                muscleCat = data.getStringArrayExtra("muscleCat");
                weightedVal = data.getStringArrayExtra("weightedVal");
                shouldUpdateStats = false;
                System.out.println("Passing back cached stats worked");
            } else {
                System.out.println("Passing back cache didn't work");
            }
        }

        if (requestCode == UPDATE_LIFT_RESULT) {
            if (resultCode == RESULT_OK) {
                try {
                    if (new LiftData().execute().get().length > 0) {
                        showSnackOnDash("Lift Updated Successfully");
                        shouldUpdateStats = true;
                        shouldUpdateCommStats = true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                shouldUpdateStats = true;
                shouldUpdateCommStats = true;
            } else {
                //User didn't update lift
            }
        }

        if (requestCode == COMM_UPDATE_RESULT) {
            if (resultCode == RESULT_OK) {
                commCache = data.getStringArrayExtra("commCache");
                shouldUpdateCommStats = false;
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button: {
                System.out.println("Community page");
                Intent i = new Intent(getBaseContext(), ClusteringActivity.class);
                i.putExtra("commCache", commCache);
                i.putExtra("shouldUpdate", shouldUpdateCommStats);
                startActivityForResult(i, COMM_UPDATE_RESULT);
                break;
            }
            case R.id.button2: {
                System.out.println("User Stats page");
                Intent i = new Intent(getBaseContext(), UserStatsActivity.class);
                i.putExtra("statsCache", statsCache);
                i.putExtra("muscleCat", muscleCat);
                i.putExtra("weightedVal", weightedVal);
                i.putExtra("shouldUpdate", shouldUpdateStats);

                startActivityForResult(i, STATS_UPDATE_RESULT);
                break;
            }
            case R.id.button3: {
                System.out.println("Settings page");
                Intent i = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(i);
                break;
            }
            case R.id.button4: {
                System.out.println("Log lift page");
                Intent i = new Intent(getBaseContext(), LogLiftActivity.class);
                startActivityForResult(i, DASH_UPDATE_RESULT);
                break;
            }
        }
    }

    protected void setLviewListener() {
        lview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getBaseContext(), UpdateLiftActivity.class);
                i.putExtra("Date", lviewAdapter.date[position]);
                i.putExtra("Lift", lviewAdapter.lift[position]);
                i.putExtra("Set_Rep", lviewAdapter.set_rep[position]);
                i.putExtra("Weight", lviewAdapter.weight[position]);
                i.putExtra("Intensity", intentIntensity[position]);
                i.putExtra("Notes", intentNotes[position]);
                i.putExtra("ID", intentID[position]);
                startActivityForResult(i, UPDATE_LIFT_RESULT);
            }
        });
    }

    protected void showSnackOnDash(String param) {
        Snackbar snackbar = Snackbar
                .make(getCurrentFocus(), param, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }

    private class LiftData extends AsyncTask<Void, Void, JSONObject[]> {
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

                String username = preferences.getUsername(Dashboard.this);
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

            return dash_data;
        }

        @Override
        protected void onPostExecute(JSONObject[] user_data) {

            String[] dates = new String[user_data.length];
            String[] lifts = new String[user_data.length];
            String[] set_reps = new String[user_data.length];
            String[] weights = new String[user_data.length];
            String[] intensity = new String[user_data.length];
            String[] notes = new String[user_data.length];
            String[] id = new String[user_data.length];

            for (int i = 0; i < user_data.length; ++i) {
                try {
                    dates[i] = user_data[i].get("date").toString();
                    lifts[i] = user_data[i].get("lift").toString();
                    set_reps[i] = user_data[i].get("sets").toString() + "x" + user_data[i].get("reps").toString();
                    weights[i] = user_data[i].get("weight").toString();
                    intensity[i] = user_data[i].get("intensity").toString();
                    notes[i] = user_data[i].get("notes").toString();
                    id[i] = user_data[i].get("id").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (intensity != null) {
                intentIntensity = intensity;
            }

            if (notes != null) {
                intentNotes = notes;
            }

            if (id != null) {
                intentID = id;
            }

            lview = (ListView) findViewById(R.id.listView);
            lviewAdapter = new ListViewAdapter(Dashboard.this, dates, lifts, set_reps, weights);

            System.out.println("adapter => " + lviewAdapter.getCount());

            lview.setAdapter(lviewAdapter);
            setLviewListener();
        }
    }
}

