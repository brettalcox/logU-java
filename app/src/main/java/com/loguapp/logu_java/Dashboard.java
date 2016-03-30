package com.loguapp.logu_java;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Dashboard extends AppCompatActivity {

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

                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("accept", "application/json");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                String urlParameters = "username=brettalcox";

                // Send post request
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

                //print result
                //System.out.println(response.toString());

                //JSONObject json_data = new JSONObject(response.toString());
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

            System.out.println(user_data.length + " is the length");
            TableLayout ll = (TableLayout) findViewById(R.id.table_view);

            try {
                for (int i = 0; i < user_data.length; i++) {

                    TableRow row = new TableRow(Dashboard.this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);

                    TextView liftData = new TextView(Dashboard.this);
                    TextView setRepData = new TextView(Dashboard.this);
                    TextView weightData = new TextView(Dashboard.this);
                    liftData.setText(user_data[i].get("lift").toString());
                    setRepData.setText(user_data[i].get("sets").toString() + "x" + user_data[i].get("reps").toString());
                    weightData.setText(user_data[i].get("weight").toString());
                    row.addView(liftData);
                    row.addView(setRepData);
                    row.addView(weightData);
                    //row.addView(addBtn);
                    ll.addView(row, i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

