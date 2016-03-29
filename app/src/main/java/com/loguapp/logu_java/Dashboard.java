package com.loguapp.logu_java;

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

import javax.net.ssl.HttpsURLConnection;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();

        new Thread(new Runnable() {
            public void run() {
                try {
                    sendPost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void init() {
        TableLayout ll = (TableLayout) findViewById(R.id.table_view);

        for (int i = 0; i < 2; i++) {

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            CheckBox checkBox = new CheckBox(this);

            //ImageButton addBtn = new ImageButton(this);
            //addBtn.setImageResource(R.drawable.add);
            TextView tv = new TextView(this);
            //ImageButton minusBtn = new ImageButton(this);
            //minusBtn.setImageResource(R.drawable.minus);
            TextView qty = new TextView(this);
            checkBox.setText("hello");
            qty.setText("10");
            row.addView(checkBox);
            //row.addView(minusBtn);
            row.addView(qty);
            //row.addView(addBtn);
            ll.addView(row, i);
        }
    }

    private void sendPost() throws Exception {

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

        JSONObject json_data = new JSONObject(response.toString());
        //JSONArray json_data = new JSONArray(response);

        for (int i = 0; i < json_data.length(); i++) {
            //System.out.println(json_data.getSt);
            System.out.println(json_data.get("lift"));
        }
    }
}

