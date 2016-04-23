package com.loguapp.logu_java;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by brettalcox on 4/22/16.
 */
public class LiftChartActivity extends Activity {

    private final Prefs preferences = new Prefs();
    private LineChart mChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lift_graph);

        mChart = (LineChart) findViewById(R.id.lift_graph);
        YAxis y = mChart.getAxisLeft();
        y.setAxisMinValue(0);

        new GraphData().execute();

        mChart.animateY(1000);

        // dont forget to refresh the drawing
        mChart.invalidate();
    }

    private void setData(String[] date, float[] weight) {

        final Bundle extras = getIntent().getExtras();

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < date.length; i++) {
            xVals.add(date[i]);
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < weight.length; i++) {
            yVals.add(new Entry(weight[i], i));
        }

        LineDataSet set1;

        set1 = new LineDataSet(yVals, extras.get("Lift").toString());

        set1.setDrawCubic(true);
        set1.setCubicIntensity(0.2f);
        set1.setDrawCircles(false);
        set1.setDrawCircleHole(false);
        set1.setColor(Color.rgb(0, 152,255));
        set1.setLineWidth(1f);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);
        set1.setFillColor(Color.rgb(0, 152, 255));


        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        LineData data = new LineData(xVals, dataSets);

        mChart.setData(data);
    }

    public class GraphData extends AsyncTask<Void, Void, JSONObject[]> {
        @Override
        protected JSONObject[] doInBackground(Void... params) {

            JSONObject[] dash_data = {};
            final Bundle extras = getIntent().getExtras();

            try {
                String url = "https://loguapp.com/lift_graph.php";
                URL obj = new URL(url);
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("accept", "application/json");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                String username = preferences.getUsername(LiftChartActivity.this);
                System.out.println(username);

                String urlParameters = "username=" + username + "&lift=" + extras.get("Lift").toString();

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

            for (int i = 0; i < dash_data.length; i++) {
                System.out.println(dash_data[i]);
            }
            return dash_data;
        }

        @Override
        protected void onPostExecute(JSONObject[] user_data) {

            String[] dates = new String[user_data.length];
            float[] weights = new float[user_data.length];

            for (int i = 0; i < user_data.length; ++i) {
                try {
                    dates[i] = user_data[i].get("date").toString();
                    weights[i] = Float.parseFloat(user_data[i].get("weight").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            setData(dates, weights);

        }
    }
}
