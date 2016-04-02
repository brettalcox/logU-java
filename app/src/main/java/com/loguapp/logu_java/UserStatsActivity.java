package com.loguapp.logu_java;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

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
 * Created by BA042808 on 3/31/2016.
 */
public class UserStatsActivity extends Activity {

    private RadarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userstats);
        init();
    }

    public void init() {

        chart = (RadarChart) findViewById(R.id.chart);

        chart.setDescription("");

        chart.setWebLineWidth(1.5f);
        chart.setWebLineWidthInner(0.75f);
        chart.setWebAlpha(100);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it

        // set the marker to the chart

        chart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(9f);


        YAxis yAxis = chart.getYAxis();
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(0f);
        yAxis.setAxisMinValue(0f);
        yAxis.setDrawLabels(false);

        new LiftData().execute();

    }

    public void setData(String[] xValues, String[] yValues) {

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        for (int i = 0; i < yValues.length; i++) {
            yVals1.add(new Entry((float) (Float.valueOf(yValues[i])), i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xValues.length; i++)
            xVals.add(xValues[i % xValues.length]);

        RadarDataSet set1 = new RadarDataSet(yVals1, "");
        set1.setColor(Color.rgb(0, 152, 255));
        set1.setFillColor(Color.rgb(0, 152, 255));
        set1.setDrawFilled(true);
        set1.setLineWidth(2f);


        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);

        RadarData data = new RadarData(xVals, sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);

        chart.setData(data);
        chart.getLegend().setEnabled(false);
        chart.invalidate();
    }

    public class LiftData extends AsyncTask<Void, Void, JSONObject[]> {
        @Override
        protected JSONObject[] doInBackground(Void... params) {

            JSONObject[] dash_data = {};

            try {
                String url = "https://loguapp.com/radar_graph.php";
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

            String[] muscles = new String[user_data.length];
            String[] values = new String[user_data.length];

            for (int i = 0; i < user_data.length; ++i) {
                try {
                    muscles[i] = user_data[i].get("category").toString();
                    values[i] = user_data[i].get("weighted").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            setData(muscles, values);
        }
    }
}
