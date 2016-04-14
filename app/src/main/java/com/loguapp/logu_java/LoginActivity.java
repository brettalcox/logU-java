package com.loguapp.logu_java;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by BA042808 on 4/5/2016.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private final Prefs preferences = new Prefs();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        System.out.println("butts");
        if (preferences.getSessionStatus(LoginActivity.this) == 1) {
            Intent i = new Intent(getBaseContext(), Dashboard.class);
            startActivity(i);
        }
    }

    @Override
    public void onClick(View v) {
        System.out.println("help");
        switch (v.getId()) {
            case R.id.loginButton: {

                EditText username = (EditText) findViewById(R.id.usernameField);
                EditText password = (EditText) findViewById(R.id.passwordField);

                if (username.getText().toString().trim().length() <= 0 || password.getText().toString().trim().length() <= 0) {
                    System.out.println("You didn't fill out all fields!");
                    loginFieldsError();
                } else {
                    preferences.setUsername(this, username.getText().toString());

                    String loginQuery = "username=" + username.getText().toString() + "&password=" + password.getText().toString();
                    new UserLogin().execute(loginQuery);
                }

                break;
            }
            case R.id.signUpButton: {

            }
        }
    }

    public void loginError() {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Login Failed!")
                .setMessage("Username/Password Incorrect")
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void loginFieldsError() {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Login Failed!")
                .setMessage("Fill out both fields.")
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public class UserLogin extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String stringResponse = "";

            try {
                String url = "https://loguapp.com/swift_login.php";
                URL obj = new URL(url);
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("accept", "application/json");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                String urlParameters = params[0];

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

                stringResponse = response.toString();
                stringResponse = stringResponse.trim();
                System.out.println(stringResponse);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return stringResponse;
        }

        @Override
        protected void onPostExecute(String loginResponse) {
            System.out.println("the value trying to login " + loginResponse + " the length is: " + loginResponse.length());
            int loginCode = Integer.parseInt(loginResponse);
            if (loginCode == 1) {

                preferences.userLoggedIn(LoginActivity.this);

                System.out.println(preferences.getSessionStatus(LoginActivity.this));

                Intent i = new Intent(getBaseContext(), Dashboard.class);
                startActivity(i);
            } else {
                loginError();
            }
        }
    }
}
