package com.loguapp.logu_java;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by BA042808 on 3/31/2016.
 */
public class LogLiftActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loglift);
        init();
    }
    JSONObject[] butts;
    public void init() {
        TextView text = (TextView) findViewById(R.id.textView5);
        System.out.println(text.getText() + "is the text that is currently displayed.");
        new LiftData().execute(butts);
    }

    public class LiftData extends AsyncTask<JSONObject[], Void, Void> {
        @Override
        protected Void doInBackground(JSONObject[]... params) {

            JSONObject[] dash_data = {};
            dash_data = params[0];

            try {
                String url = "https://loguapp.com/swift2.php";
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
