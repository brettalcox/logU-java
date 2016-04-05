package com.loguapp.logu_java;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by BA042808 on 3/31/2016.
 */
public class UserStatsActivity extends Activity {

    private RadarChart chart;
    private JSONObject[] targetedMuscle;
    private JSONObject[] wilkScore;
    private JSONObject[] repAverage;
    private JSONObject[] wilksRank;
    private JSONObject[] avgFreq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userstats);
        init();
    }

    public void init() {

        formatChart();

        try {
            targetedMuscle = new LiftData().execute("https://loguapp.com/radar_graph.php").get();
            setTargetedMuscle(targetedMuscle);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            wilkScore = new LiftData().execute("https://loguapp.com/wilks_score.php").get();
            setWilkScore(wilkScore);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            repAverage = new LiftData().execute("https://loguapp.com/rep_average.php").get();
            setRepAverage(repAverage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            wilksRank = new LiftData().execute("https://loguapp.com/swift_wilks_percentile.php").get();
            setWilksRank(wilksRank);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            avgFreq = new LiftData().execute("https://loguapp.com/average_frequency.php").get();
            setAvgFreq(avgFreq);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public void formatChart() {
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

    public void setTargetedMuscle(JSONObject[] data) {
        String[] muscles = new String[data.length];
        String[] values = new String[data.length];

        for (int i = 0; i < data.length; ++i) {
            try {
                muscles[i] = data[i].get("category").toString();
                values[i] = data[i].get("weighted").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setData(muscles, values);
    }

    public void setWilkScore(JSONObject[] data) {
        String[] values = new String[data.length];

        for (int i = 0; i < data.length; ++i) {
            try {
                values[i] = data[i].get("wilks_coeff").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        TextView wilkLabel = (TextView) findViewById(R.id.wilkScoreLabel);
        wilkLabel.setText(values[0]);
    }

    public void setRepAverage(JSONObject[] data) {
        String[] avg_reps = new String[data.length];
        String[] reps = new String[data.length];
        String[] sets = new String[data.length];
        String[] lifts = new String[data.length];
        String[] favLift = new String[data.length];

        for (int i = 0; i < data.length; ++i) {
            try {
                avg_reps[i] = data[i].get("average_reps").toString();
                reps[i] = data[i].get("total_reps").toString();
                sets[i] = data[i].get("total_sets").toString();
                lifts[i] = data[i].get("total_lifts").toString();
                favLift[i] = data[i].get("count").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        TextView avgRepLabel = (TextView) findViewById(R.id.avgRepsLabel);
        TextView repLabel = (TextView) findViewById(R.id.totalRepsLabel);
        TextView setLabel = (TextView) findViewById(R.id.totalSetsLabel);
        TextView liftLabel = (TextView) findViewById(R.id.liftCountLabel);
        TextView favoriteLift = (TextView) findViewById(R.id.favLiftLabel);

        avgRepLabel.setText(avg_reps[0]);
        repLabel.setText(reps[0]);
        setLabel.setText(sets[0]);
        liftLabel.setText(lifts[0]);
        favoriteLift.setText(favLift[0]);
    }

    public void setWilksRank(JSONObject[] data) {
        String[] values = new String[data.length];

        for (int i = 0; i < data.length; ++i) {
            try {
                values[i] = data[i].get("rank").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        TextView rankLabel = (TextView) findViewById(R.id.wilksRankLabel);
        rankLabel.setText(values[0]);
    }

    public void setAvgFreq(JSONObject[] data) {
        String[] values = new String[data.length];

        for (int i = 0; i < data.length; ++i) {
            try {
                values[i] = data[i].get("average_freq").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        TextView avgFreqLabel = (TextView) findViewById(R.id.avgFreqLabel);
        avgFreqLabel.setText(values[0]);
    }

    public class LiftData extends AsyncTask<String, Void, JSONObject[]> {
        @Override
        protected JSONObject[] doInBackground(String... url) {

            JSONObject[] dash_data = {};

            try {
                URL obj = new URL(url[0]);
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
    }
}
